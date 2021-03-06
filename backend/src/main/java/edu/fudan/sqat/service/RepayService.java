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
    private final LoanPayRepository loanPayRepository;
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public RepayService(AccountRepository accountRepository,
                        LoanPayRepository loanPayRepository,
                        LoanRepository loanRepository,
                        TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.loanPayRepository = loanPayRepository;
        this.loanRepository = loanRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Loan> loanInfo(Long id) throws Exception {
        if(!accountRepository.findById(id).isPresent()){
            throw new Exception("the loan id doesn't exist!");
        }

        return (List<Loan>) loanRepository.findLoanByAccountId(id);
    }


    public String repayment(RepaymentRequest repaymentRequest) throws Exception {
        /*
        每次都是还完了这一期的 如果还有下一期 新建下一期的进list
         */
        if(!loanRepository.findById(repaymentRequest.getLoanId()).isPresent()){
            throw new Exception("the loan id doesn't exist!");
        }
        Loan loan=loanRepository.findById(repaymentRequest.getLoanId()).get();
        if(!accountRepository.findById(loan.getAccountId()).isPresent()){
            throw new Exception("the account id doesn't exist!");
        }
        Account account=accountRepository.findById(loan.getAccountId()).get();

        Integer type=repaymentRequest.getType();
        Double money=repaymentRequest.getMoney();
        LoanPay currentPay=loan.getLoanPays().get(loan.getLoanPays().size()-1);
        Date currentTime=repaymentRequest.getCurrentTime();

        if(type==1){
            //全额还款
            //是这笔贷款这一期全部还清

            money=account.getTotal();

            if(Math.abs(money-currentPay.getAmount()+currentPay.getFine())>0.00001&&money<currentPay.getAmount()+currentPay.getFine()){
                //此时即便过期 那么真正的期数的钱一定比currentPay时要多，如果过期的那个loanPay都还不了更不可能还"本期"的
                //因此可以直接 returnError
                return "Error";
            }
            else{
                if(currentTime.compareTo(currentPay.getEnd())<=0) {
                    //按时还清
                    System.out.println("按时还清");

                    fullPayInTime(loan, account, currentPay);
                    return "Success";
                }else{
                    //如果本期超时，那么本期不能还了 去还当前时刻所在的那一期

                    currentPay = findCurrentLoanPay(loan, currentPay, currentTime);

                    //此时的currentPay为当前时间处于的那一期
                    if(money<currentPay.getAmount()+currentPay.getFine()) {
                        System.out.println(" 此时的currentPay为当前时间处于的那一期 --hit true--");
                        return "Error";
                    }
                    else{
                        //按时还清
                        fullPayInTime(loan, account, currentPay);
                        return "Success";
                    }

                }
            }
        }else if(type==0){
            //部分还款
            if(money>account.getTotal())
                throw new Exception("You don't have so much money in the account!");

            if(currentTime.compareTo(currentPay.getEnd())<=0) {

                if (money >currentPay.getAmount() + currentPay.getFine())
                    return "Error";

                realPartialPayment(loan, account, money, currentPay);

                return "Success";

            } else{
                //过期的 部分还款
                //如果本期超时，那么本期不能还了 去还当前时刻所在的那一期

                currentPay = findCurrentLoanPay(loan, currentPay, currentTime);

                //此时的currentPay为当前时间处于的那一期
                if (money >currentPay.getAmount() + currentPay.getFine())
                    return "Error";

                //本期的部分还款
                realPartialPayment(loan, account, money, currentPay);
                return "Success";
            }


        }else{
            throw new Exception("type is invalid!");
        }


    }

    private LoanPay findCurrentLoanPay(Loan loan, LoanPay currentPay, Date currentTime) {
        while ((loan.getStageCount() > currentPay.getStage()) && (currentTime.compareTo(currentPay.getEnd()) > 0)) {
            ////初始化时 fineAfterPaid总与fine相同
            //上一期的未还款金额 (currentPay.getAmount()-currentPay.getMoneyPaid())+(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount())
            LoanPay newPay = new LoanPay(loan.getId(), (currentPay.getAmount() - currentPay.getMoneyPaid()) + (loan.getAmount() * (1 + loan.getInterest())) / loan.getStageCount(),
                    (currentPay.getAmount() - currentPay.getMoneyPaid()) * 0.05 + currentPay.getFineAfterPaid(), loan.getLoanPays().size() + 1,
                    currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d, (currentPay.getAmount() - currentPay.getMoneyPaid()) * 0.05 + currentPay.getFineAfterPaid());

            loanPayRepository.save(newPay);
            List<LoanPay> loanPays = loan.getLoanPays();
            loanPays.add(newPay);
            loan.setLoanPays(loanPays);
            loanRepository.save(loan);

            currentPay = loan.getLoanPays().get(loan.getLoanPays().size() - 1);

        }
        return currentPay;
    }

    private void realPartialPayment(Loan loan, Account account, Double money, LoanPay currentPay) {
        //本期的部分还款
        if (money > currentPay.getAmount() - currentPay.getMoneyPaid() + currentPay.getFineAfterPaid()||Math.abs(currentPay.getAmount() - currentPay.getMoneyPaid() + currentPay.getFineAfterPaid()-money)<0.00001) {
            //相当于全额还清
            System.out.println(Math.abs(currentPay.getAmount() - currentPay.getMoneyPaid() + currentPay.getFineAfterPaid()-money)<0.00001);
            System.out.println(money > currentPay.getAmount() - currentPay.getMoneyPaid() + currentPay.getFineAfterPaid());
            Double pay = (currentPay.getAmount() - currentPay.getMoneyPaid()) + currentPay.getFineAfterPaid();
            currentPay.setMoneyPaid(currentPay.getAmount());
            currentPay.setFineAfterPaid(0d);
            loanPayRepository.save(currentPay);

            //加上新的一期
            addNewLoanPay(loan, currentPay);

            account.setTotal(account.getTotal() - pay);
            accountRepository.save(account);

            Transaction transaction = new Transaction(account,
                    -pay, account.getTotal(), "Loan Pay Outlay", new Date());
            transactionRepository.save(transaction);
            if (loan.getLoanPays().get(loan.getLoanPays().size() - 1).getAmount() - loan.getLoanPays().get(loan.getLoanPays().size() - 1).getMoneyPaid() + loan.getLoanPays().get(loan.getLoanPays().size() - 1).getFineAfterPaid() < 0.00001 && loan.getStageCount() == currentPay.getStage()) {
                loan.setPaidOff(true);
                loanRepository.save(loan);
            }else
                System.out.println("本期的部分还款 没有全部还清");

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

                System.out.println("account.getTotal::"+account.getTotal());

                Transaction transaction = new Transaction(account,
                        -money, account.getTotal(), "Loan Pay Outlay", new Date());
                transactionRepository.save(transaction);

               //这里一定不会是付完使得loan全部付清 如果全部付清的话 那么就是相当于全额还款

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

                //因为这里罚金都付不起 因此必不可能还清整笔贷款

            }
        }
    }

    private void addNewLoanPay(Loan loan, LoanPay currentPay) {
        if (loan.getStageCount() > currentPay.getStage()) {

            LoanPay newPay = new LoanPay(loan.getId(), (loan.getAmount() * (1 + loan.getInterest())) / loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                    currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d, 0d);
            loanPayRepository.save(newPay);
            List<LoanPay> loanPays = loan.getLoanPays();
            loanPays.add(newPay);
            loan.setLoanPays(loanPays);
            loanRepository.save(loan);
        }
        // 调用add new loan之后都会进行判断 因此这里可以不判断
    }

    private void fullPayInTime(Loan loan, Account account, LoanPay currentPay) {
        currentPay.setMoneyPaid(currentPay.getAmount());
        currentPay.setFineAfterPaid(0d);
        loanPayRepository.save(currentPay);

        //加上新的一期
        addNewLoanPay(loan, currentPay);

        account.setTotal(account.getTotal() - currentPay.getAmount() - currentPay.getFine());
        accountRepository.save(account);

        Transaction transaction = new Transaction(account,
                -currentPay.getAmount() - currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
        transactionRepository.save(transaction);

        if (loan.getLoanPays().get(loan.getLoanPays().size() - 1).getAmount() - loan.getLoanPays().get(loan.getLoanPays().size() - 1).getMoneyPaid() + loan.getLoanPays().get(loan.getLoanPays().size() - 1).getFineAfterPaid() < 0.00001 && loan.getStageCount() == currentPay.getStage()) {
            loan.setPaidOff(true);
            loanRepository.save(loan);
        }else{
            System.out.println("-----full time in pay false----");
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
            } else
                System.out.println("不满足超时且未还清");
        }
        //全是不同的贷款账户中的最近一个LoanPay
        for(LoanPay loanPay:targetList){
            LoanPay currentPay=loanPay;
            Loan loan=loanRepository.findById(currentPay.getLoanId()).get();
            Account account=accountRepository.findById(loan.getAccountId()).get();

            currentPay = findCurrentLoanPay(loan, currentPay, currentTime);
            //此时的currentPay为当前时间处于的那一期
            System.out.println(currentPay.getFine());
            if(currentPay.getFine()>0){
                //判断账户欠款中是否包含罚金
                //判断账户余额是否大于罚金余额
                if(account.getTotal()>currentPay.getFine()){
                    System.out.println("账户余额大于罚金余额");
                    //从账户中扣除罚金
                    account.setTotal(account.getTotal()-currentPay.getFine());
                    accountRepository.save(account);
                    currentPay.setFineAfterPaid(0d);
                    loanPayRepository.save(loanPay);
                    Transaction transaction = new Transaction(account,
                            -currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                }else{
                    System.out.println("账户余额小于罚金余额");
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
                        System.out.println("new new new!!!!!");
                        LoanPay newPay = new LoanPay(loan.getId(),(loan.getAmount()*(1+loan.getInterest()))/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                        loanPayRepository.save(newPay);
                        List<LoanPay> loanPays = loan.getLoanPays();
                        loanPays.add(newPay);
                        loan.setLoanPays(loanPays);
                        loanRepository.save(loan);
                    } else {
                        //说明最后一期已经还清
                        loan.setPaidOff(true);
                        loanRepository.save(loan);
                    }

                }
            }else {
                System.out.println("无罚金");
                if(account.getTotal()>currentPay.getAmount()){
                    //判断账户余额是否大于欠款金额
                    account.setTotal(account.getTotal()-currentPay.getAmount());
                    accountRepository.save(account);
                    currentPay.setMoneyPaid(currentPay.getAmount());
                    loanPayRepository.save(currentPay); Transaction transaction = new Transaction(account,
                            -currentPay.getAmount(), account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                    //已过期无罚金 说明已经到了最后一个loanpay 不用新建这个贷款的期
                    //说明最后一期已经还清
                    loan.setPaidOff(true);
                    loanRepository.save(loan);


                }
            }

        }

        return null;
    }
}
