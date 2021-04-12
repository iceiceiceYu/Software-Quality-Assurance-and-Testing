package edu.fudan.sqat.service;

import edu.fudan.sqat.controller.request.RepaymentRequest;
import edu.fudan.sqat.domain.*;
import edu.fudan.sqat.domain.Loan;
import edu.fudan.sqat.exception.PartialRepayException;
import edu.fudan.sqat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class RepayService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final LoanPayRepository loanPayRepository;
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public RepayService(AccountRepository accountRepository,
                        ClientRepository clientRepository,
                        LoanPayRepository loanPayRepository,
                        LoanRepository loanRepository,
                        TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.loanPayRepository = loanPayRepository;
        this.loanRepository = loanRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Loan> loanInfo(Long id) throws Exception {
        if(accountRepository.findById(id).get()==null){
            throw new Exception("the loan id doesn't exist!");
        }

        return (List<Loan>) loanRepository.findLoanByAccountId(id);
    }


    public String repayment(RepaymentRequest repaymentRequest) throws Exception {
        /*
        每次都是还完了这一期的 如果还有下一期 新建下一期的进list
         */
        Loan loan=loanRepository.findById(repaymentRequest.getLoanId()).get();
        Account account=accountRepository.findById(loan.getAccountId()).get();
        if(loan==null){
            throw new Exception("the loan id doesn't exist!");
        }
        if(account==null){
            throw new Exception("the account id doesn't exist!");
        }
        Integer type=repaymentRequest.getType();
        Double money=repaymentRequest.getMoney();
        LoanPay currentPay=loan.getLoanPays().get(loan.getLoanPays().size()-1);
        Date currentTime=repaymentRequest.getCurrentTime();

        if(type==1){
            //全额还款
            //是这笔贷款这一期全部还清
            System.out.println("全额还款");
            if(money<currentPay.getAmount()+currentPay.getFine()){
                //此时即便过期 那么真正的期数的钱一定比currentPay时要多，如果过期的那个loanPay都还不了更不可能还"本期"的
                //因此可以直接 returnError
                return "Error";
            }
            else{
                if(currentTime.compareTo(currentPay.getEnd())<=0) {
                    //按时还清
                    System.out.println("按时还清");
                    currentPay.setMoneyPaid(currentPay.getAmount());
                    currentPay.setFineAfterPaid(0d);
                    loanPayRepository.save(currentPay);
                    //加上新的一期
                    if (loan.getStageCount() > currentPay.getStage()) {

                        LoanPay newPay = new LoanPay(loan.getId(), (loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                        loanPayRepository.save(newPay);
                        List<LoanPay> loanPays = loan.getLoanPays();
                        loanPays.add(newPay);
                        loan.setLoanPays(loanPays);
                        loanRepository.save(loan);
                    }else{
                        //说明最后一期已经还清

                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                    }


                    account.setTotal(account.getTotal() - currentPay.getAmount() - currentPay.getFine());
                    accountRepository.save(account);

                    Transaction transaction = new Transaction(account,
                            -currentPay.getAmount() - currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                    if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                        loan.setPaidOff(true);
                        loanRepository.save(loan);
                    }
                    return "Success";
                }else{
                    //如果本期超时，那么本期不能还了 去还当前时刻所在的那一期

                    while ((loan.getStageCount() > currentPay.getStage())&&(currentTime.compareTo(currentPay.getEnd())>0)) {
                        ////初始化时 fineAfterPaid总与fine相同
                        LoanPay newPay = new LoanPay(loan.getId(), (currentPay.getAmount()-currentPay.getMoneyPaid())+(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(), (currentPay.getAmount()-currentPay.getMoneyPaid())*0.05+currentPay.getFineAfterPaid(), loan.getLoanPays().size() + 1,
                                currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,(currentPay.getAmount()-currentPay.getMoneyPaid())*0.05+currentPay.getFineAfterPaid());

                        loanPayRepository.save(newPay);
                        List<LoanPay> loanPays = loan.getLoanPays();
                        loanPays.add(newPay);
                        loan.setLoanPays(loanPays);
                        loanRepository.save(loan);

                        currentPay=loan.getLoanPays().get(loan.getLoanPays().size()-1);

                    }
                    //此时的currentPay为当前时间处于的那一期
                    if(money<currentPay.getAmount()+currentPay.getFine()) {
                        return "Error";
                    }
                    else{
                        //按时还清
                        currentPay.setMoneyPaid(currentPay.getAmount());
                        currentPay.setFineAfterPaid(0d);
                        loanPayRepository.save(currentPay);
                        //加上新的一期
                        if (loan.getStageCount() > currentPay.getStage()) {

                            LoanPay newPay = new LoanPay(loan.getId(), (loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                    currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                            loanPayRepository.save(newPay);
                            List<LoanPay> loanPays = loan.getLoanPays();
                            loanPays.add(newPay);
                            loan.setLoanPays(loanPays);
                            loanRepository.save(loan);
                        }else{
                            //说明最后一期已经还清
                            if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001) {
                                loan.setPaidOff(true);
                                loanRepository.save(loan);
                            }
                        }


                        account.setTotal(account.getTotal() - currentPay.getAmount() - currentPay.getFine());
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -currentPay.getAmount() - currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);


                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                        return "Success";
                    }

                }
            }
        }else if(type==0){
            //部分还款
            if(currentTime.compareTo(currentPay.getEnd())<=0) {

                if (money >currentPay.getAmount() + currentPay.getFine())
                    throw new PartialRepayException();
                //本期的部分还款
                if (money >= currentPay.getAmount()-currentPay.getMoneyPaid() + currentPay.getFineAfterPaid()) {
                    //相当于全额还清
                    Double pay=(currentPay.getAmount()-currentPay.getMoneyPaid()) +currentPay.getFineAfterPaid();
                    currentPay.setMoneyPaid(currentPay.getAmount());
                    currentPay.setFineAfterPaid(0d);
                    loanPayRepository.save(currentPay);
                    //加上新的一期
                    if (loan.getStageCount() > currentPay.getStage()) {

                        LoanPay newPay = new LoanPay(loan.getId(),(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                        loanPayRepository.save(newPay);
                        List<LoanPay> loanPays = loan.getLoanPays();
                        loanPays.add(newPay);
                        loan.setLoanPays(loanPays);
                        loanRepository.save(loan);
                    }else{
                        //说明最后一期已经还清
                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                    }

                    account.setTotal(account.getTotal() - pay);
                    accountRepository.save(account);

                    Transaction transaction = new Transaction(account,
                            - pay, account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                    if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                        loan.setPaidOff(true);
                        loanRepository.save(loan);
                    }
                    return "Success";
                } else {
                    //部分还款
                    if (money >= currentPay.getFineAfterPaid()) {
                        //先还罚金
                        currentPay.setMoneyPaid(money - currentPay.getFineAfterPaid()+ currentPay.getMoneyPaid());
                        currentPay.setFineAfterPaid(0d);
                        loanPayRepository.save(currentPay);

                        //未过期的部分还款不应该新建新的期，因为有可能之后还会还
                        //之前全额还款本期已经全部结束 不能再还了 或者本期已经全部还清

                        account.setTotal(account.getTotal() - money);
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -money, account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);

                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                        return "Success";


                    } else {
                        //罚金都不够
                        //当前moneyPaid为零 因为连罚金都没有还够
                        currentPay.setFineAfterPaid(currentPay.getFine() - money);
                        loanPayRepository.save(currentPay);

                        account.setTotal(account.getTotal() - money);
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -money, account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);

                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                        return "Success";
                    }



                }

            } else{
                //过期的 部分还款
                //如果本期超时，那么本期不能还了 去还当前时刻所在的那一期

                while ((loan.getStageCount() > currentPay.getStage())&&(currentTime.compareTo(currentPay.getEnd())>0)) {
                    ////初始化时 fineAfterPaid总与fine相同
                    //上一期的未还款金额 (currentPay.getAmount()-currentPay.getMoneyPaid())+(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount())
                    LoanPay newPay = new LoanPay(loan.getId(), (currentPay.getAmount()-currentPay.getMoneyPaid())+(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(),
                            (currentPay.getAmount()-currentPay.getMoneyPaid())*0.05+currentPay.getFineAfterPaid(), loan.getLoanPays().size() + 1,
                            currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,(currentPay.getAmount()-currentPay.getMoneyPaid())*0.05+currentPay.getFineAfterPaid());

                    loanPayRepository.save(newPay);
                    List<LoanPay> loanPays = loan.getLoanPays();
                    loanPays.add(newPay);
                    loan.setLoanPays(loanPays);
                    loanRepository.save(loan);

                    currentPay=loan.getLoanPays().get(loan.getLoanPays().size()-1);

                }

                //此时的currentPay为当前时间处于的那一期
                if (money >currentPay.getAmount() + currentPay.getFine())
                    throw new PartialRepayException();

                //本期的部分还款
                if (money >= currentPay.getAmount()-currentPay.getMoneyPaid() + currentPay.getFineAfterPaid()) {
                    //相当于全额还清
                    Double pay=(currentPay.getAmount()-currentPay.getMoneyPaid()) +currentPay.getFineAfterPaid();
                    currentPay.setMoneyPaid(currentPay.getAmount());
                    currentPay.setFineAfterPaid(0d);
                    loanPayRepository.save(currentPay);
                    //加上新的一期
                    if (loan.getStageCount() > currentPay.getStage()) {

                        LoanPay newPay = new LoanPay(loan.getId(),(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                        loanPayRepository.save(newPay);
                        List<LoanPay> loanPays = loan.getLoanPays();
                        loanPays.add(newPay);
                        loan.setLoanPays(loanPays);
                        loanRepository.save(loan);
                    }else{
                        //说明最后一期已经还清
                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                    }

                    account.setTotal(account.getTotal() - pay);
                    accountRepository.save(account);

                    Transaction transaction = new Transaction(account,
                            - pay, account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                    if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                        loan.setPaidOff(true);
                        loanRepository.save(loan);
                    }
                    return "Success";
                } else {
                    //部分还款
                    if (money >= currentPay.getFineAfterPaid()) {
                        //先还罚金
                        currentPay.setMoneyPaid(money - currentPay.getFineAfterPaid() + currentPay.getMoneyPaid());
                        currentPay.setFineAfterPaid(0d);
                        loanPayRepository.save(currentPay);

                        //未过期的部分还款不应该新建新的期，因为有可能之后还会还
                        //之前全额还款本期已经全部结束 不能再还了 或者本期已经全部还清

                        account.setTotal(account.getTotal() - money);
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -money, account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);

                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                        return "Success";


                    } else {
                        //罚金都不够
                        //当前moneyPaid为零 因为连罚金都没有还够
                        currentPay.setFineAfterPaid(currentPay.getFine() - money);
                        loanPayRepository.save(currentPay);

                        account.setTotal(account.getTotal() - money);
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -money, account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);

                        if(loan.getLoanPays().get(loan.getLoanPays().size()-1).getAmount()-loan.getLoanPays().get(loan.getLoanPays().size()-1).getMoneyPaid()+loan.getLoanPays().get(loan.getLoanPays().size()-1).getFineAfterPaid()<0.00001&&loan.getStageCount() ==currentPay.getStage()) {
                            loan.setPaidOff(true);
                            loanRepository.save(loan);
                        }
                        return "Success";
                    }

                }
                }

        }else{
            throw new Exception("type is invalid!");
        }


    }

    public static Date addDate(Date date,long day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    public String autoRepayment() {
        /*
        每次都是还完了这一期的 如果还有下一期 新建下一期的进list
         */
        Date currentTime=new Date();
        //系统逐户扫描
        List<LoanPay> loanPayList= (List<LoanPay>) loanPayRepository.findAll();
        List<LoanPay> targetList=new ArrayList<LoanPay>();
        for(LoanPay loanPay:loanPayList){
            Loan loan=loanRepository.findById(loanPay.getLoanId()).get();
            if(currentTime.compareTo(loanPay.getEnd())>0 && (loanPay.getMoneyPaid()<loanPay.getAmount() || loanPay.getFineAfterPaid()>0)){
                //满足超时且未还清
                if(loan.getLoanPays().get(loan.getLoanPays().size()-1).equals(loanPay))
                {
                    //只需要算一个贷款中最近的loanPay即可
                    targetList.add(loanPay);
                }
            }
        }
        //全是不同的贷款账户中的最近一个LoanPay
        for(LoanPay loanPay:targetList){
            LoanPay currentPay=loanPay;
            Loan loan=loanRepository.findById(currentPay.getLoanId()).get();
            Account account=accountRepository.findById(loan.getAccountId()).get();

            while ((loan.getStageCount() > currentPay.getStage())&&(currentTime.compareTo(currentPay.getEnd())>0)) {
                ////初始化时 fineAfterPaid总与fine相同
                //上一期的未还款金额 (currentPay.getAmount()-currentPay.getMoneyPaid())+(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount())
                LoanPay newPay = new LoanPay(loan.getId(), (currentPay.getAmount()-currentPay.getMoneyPaid())+(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(),
                        (currentPay.getAmount()-currentPay.getMoneyPaid())*0.05+currentPay.getFineAfterPaid(), loan.getLoanPays().size() + 1,
                        currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,(currentPay.getAmount()-currentPay.getMoneyPaid())*0.05+currentPay.getFineAfterPaid());

                loanPayRepository.save(newPay);
                List<LoanPay> loanPays = loan.getLoanPays();
                loanPays.add(newPay);
                loan.setLoanPays(loanPays);
                loanRepository.save(loan);

                currentPay=loan.getLoanPays().get(loan.getLoanPays().size()-1);

            }
            //此时的currentPay为当前时间处于的那一期
            if(currentPay.getFine()>0){
                //判断账户欠款中是否包含罚金
                //判断账户余额是否大于罚金余额
                if(account.getTotal()>currentPay.getFine()){
                    //从账户中扣除罚金
                    account.setTotal(account.getTotal()-currentPay.getFine());
                    accountRepository.save(account);
                    currentPay.setFineAfterPaid(0d);
                    loanPayRepository.save(loanPay);
                    Transaction transaction = new Transaction(account,
                            -currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                }
                if(account.getTotal()>currentPay.getAmount()){
                    //判断账户余额是否大于欠款金额
                    account.setTotal(account.getTotal()-currentPay.getAmount());
                    accountRepository.save(account);
                    currentPay.setMoneyPaid(currentPay.getAmount());
                    loanPayRepository.save(currentPay); Transaction transaction = new Transaction(account,
                            -currentPay.getAmount(), account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                    //新建这个贷款的期
                    if (loan.getStageCount() > currentPay.getStage()) {

                        LoanPay newPay = new LoanPay(loan.getId(),(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                        loanPayRepository.save(newPay);
                        List<LoanPay> loanPays = loan.getLoanPays();
                        loanPays.add(newPay);
                        loan.setLoanPays(loanPays);
                        loanRepository.save(loan);
                    }else{
                        //说明最后一期已经还清
                        loan.setPaidOff(true);
                        loanRepository.save(loan);
                    }

                }
            }

        }

        return null;
    }
}
