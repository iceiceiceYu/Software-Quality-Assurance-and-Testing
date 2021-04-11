package edu.fudan.sqat.service;

import edu.fudan.sqat.controller.request.RepaymentRequest;
import edu.fudan.sqat.domain.*;
import edu.fudan.sqat.domain.Loan;
import edu.fudan.sqat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        return loanRepository.findByAccountId(id);
    }


    public String repayment(RepaymentRequest repaymentRequest) throws Exception {
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
            if(money<currentPay.getAmount()+currentPay.getFine()){
                //此时即便过期 那么真正的期数的钱一定比currentPay时要多，如果过期的那个loanPay都还不了更不可能还"本期"的
                //因此可以直接 returnError
                return "Error";
            }
            else{
                if(currentTime.compareTo(currentPay.getEnd())<=0) {
                    //按时还清
                    currentPay.setMoneyPaid(currentPay.getAmount());
                    currentPay.setFineAfterPaid(0d);
                    loanPayRepository.save(currentPay);
                    //加上新的一期
                    if (loan.getStageCount() > currentPay.getStage()) {

                        LoanPay newPay = new LoanPay(loan.getId(), loan.getAmount()/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                        loanPayRepository.save(newPay);
                        List<LoanPay> loanPays = loan.getLoanPays();
                        loanPays.add(newPay);
                        loan.setLoanPays(loanPays);
                        loanRepository.save(loan);
                    }


                    account.setTotal(account.getTotal() - currentPay.getAmount() - currentPay.getFine());
                    accountRepository.save(account);

                    Transaction transaction = new Transaction(account,
                            -currentPay.getAmount() - currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
                    transactionRepository.save(transaction);

                    return "Success";
                }else{
                    //如果本期超时，那么本期不能还了 去还当前时刻所在的那一期

                    while ((loan.getStageCount() > currentPay.getStage())&&(currentTime.compareTo(currentPay.getEnd())>0)) {
                        ////初始化时 fineAfterPaid总与fine相同
                       LoanPay newPay = new LoanPay(loan.getId(), (currentPay.getAmount()-currentPay.getMoneyPaid())+loan.getAmount()/loan.getStageCount(), (currentPay.getAmount()-currentPay.getMoneyPaid())*0.05+currentPay.getFineAfterPaid(), loan.getLoanPays().size() + 1,
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

                            LoanPay newPay = new LoanPay(loan.getId(), loan.getAmount()/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                    currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                            loanPayRepository.save(newPay);
                            List<LoanPay> loanPays = loan.getLoanPays();
                            loanPays.add(newPay);
                            loan.setLoanPays(loanPays);
                            loanRepository.save(loan);
                        }


                        account.setTotal(account.getTotal() - currentPay.getAmount() - currentPay.getFine());
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -currentPay.getAmount() - currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);

                        return "Success";
                    }

                }
            }
        }else if(type==0){
            //部分还款
                if(currentTime.compareTo(currentPay.getEnd())<=0) {

                    if (money >currentPay.getAmount() + currentPay.getFine())
                        throw new Exception("Partial repayment amount cannot be greater than full repayment!");

                //本期的部分还款
                    if (money == currentPay.getAmount() + currentPay.getFine()) {
                        //相当于全额还清
                        currentPay.setMoneyPaid(currentPay.getAmount());
                        currentPay.setFineAfterPaid(0d);
                        loanPayRepository.save(currentPay);
                        //加上新的一期
                        if (loan.getStageCount() > currentPay.getStage()) {

                            LoanPay newPay = new LoanPay(loan.getId(),loan.getAmount()/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                    currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                            loanPayRepository.save(newPay);
                            List<LoanPay> loanPays = loan.getLoanPays();
                            loanPays.add(newPay);
                            loan.setLoanPays(loanPays);
                            loanRepository.save(loan);
                        }

                        account.setTotal(account.getTotal() - currentPay.getAmount() - currentPay.getFine());
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -currentPay.getAmount() - currentPay.getFine(), account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);

                        return "Success";
                    } else {
                        //部分还款
                        if (money >= currentPay.getFine()) {
                            //先还罚金
                            currentPay.setMoneyPaid(money - currentPay.getFine());
                            currentPay.setFineAfterPaid(0d);
                            loanPayRepository.save(currentPay);

                            //未过期的部分还款不应该新建新的期，因为有可能之后还会还
                            //之前全额还款本期已经全部结束 不能再还了 或者本期已经全部还清

                            account.setTotal(account.getTotal() - money);
                            accountRepository.save(account);

                            Transaction transaction = new Transaction(account,
                                    -money, account.getTotal(), "Loan Pay Outlay", new Date());
                            transactionRepository.save(transaction);

                            return "Success";


                        } else {
                            //罚金都不够 那下一次的罚金等于这次所有未还的部分*0。05+未还的罚金
                            //当前moneyPaid为零 因为连罚金都没有还够
                            currentPay.setFineAfterPaid(currentPay.getFine() - money);
                            loanPayRepository.save(currentPay);

                            account.setTotal(account.getTotal() - money);
                            accountRepository.save(account);

                            Transaction transaction = new Transaction(account,
                                    -money, account.getTotal(), "Loan Pay Outlay", new Date());
                            transactionRepository.save(transaction);

                            return "Success";
                        }



                    }

            } else{
                    //过期的 部分还款
                    //如果本期超时，那么本期不能还了 去还当前时刻所在的那一期

                    while ((loan.getStageCount() > currentPay.getStage())&&(currentTime.compareTo(currentPay.getEnd())>0)) {
                        ////初始化时 fineAfterPaid总与fine相同
                        //上一期的未还款金额 (currentPay.getAmount()-currentPay.getMoneyPaid())+loan.getAmount()/loan.getStageCount())
                        LoanPay newPay = new LoanPay(loan.getId(), (currentPay.getAmount()-currentPay.getMoneyPaid())+loan.getAmount()/loan.getStageCount(),
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
                        throw new Exception("Partial repayment amount cannot be greater than full repayment!");

                    if (money == currentPay.getAmount() + currentPay.getFine()) {
                        //相当于全额还清
                        currentPay.setMoneyPaid(currentPay.getAmount());
                        currentPay.setFineAfterPaid(0d);
                        loanPayRepository.save(currentPay);
                        //加上新的一期
                        if (loan.getStageCount() > currentPay.getStage()) {

                            LoanPay newPay = new LoanPay(loan.getId(),loan.getAmount()/loan.getStageCount(), 0d, loan.getLoanPays().size() + 1,
                                    currentPay.getEnd(), addDate(currentPay.getEnd(), 30), 0d,0d);
                            loanPayRepository.save(newPay);
                            List<LoanPay> loanPays = loan.getLoanPays();
                            loanPays.add(newPay);
                            loan.setLoanPays(loanPays);
                            loanRepository.save(loan);
                        }

                        account.setTotal(account.getTotal() - money);
                        accountRepository.save(account);

                        Transaction transaction = new Transaction(account,
                                -money, account.getTotal(), "Loan Pay Outlay", new Date());
                        transactionRepository.save(transaction);

                        return "Success";
                    } else {
                        //部分还款
                        if (money >= currentPay.getFine()) {
                            //先还罚金
                            currentPay.setMoneyPaid(money - currentPay.getFine());
                            currentPay.setFineAfterPaid(0d);
                            loanPayRepository.save(currentPay);

                            //未过期的部分还款不应该新建新的期，因为有可能之后还会还
                            //之前全额还款本期已经全部结束 不能再还了 或者本期已经全部还清

                            account.setTotal(account.getTotal() - money);
                            accountRepository.save(account);

                            Transaction transaction = new Transaction(account,
                                    -money, account.getTotal(), "Loan Pay Outlay", new Date());
                            transactionRepository.save(transaction);

                            return "Success";


                        } else {
                            //罚金都不够 那下一次的罚金等于这次所有未还的部分*0。05+未还的罚金
                            //当前moneyPaid为零 因为连罚金都没有还够
                            currentPay.setFineAfterPaid(currentPay.getFine() - money);
                            loanPayRepository.save(currentPay);

                            account.setTotal(account.getTotal() - money);
                            accountRepository.save(account);

                            Transaction transaction = new Transaction(account,
                                    -money, account.getTotal(), "Loan Pay Outlay", new Date());
                            transactionRepository.save(transaction);

                            return "Success";
                        }



                    }

                }

        }else{
            throw new Exception("type is invalid!");
        }


    }

    public static Date addDate(Date date,long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    public String autoRepayment() {
        return null;
    }
}
