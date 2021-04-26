package edu.fudan.sqat.service;


import edu.fudan.sqat.controller.request.RepaymentRequest;
import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.domain.Client;
import edu.fudan.sqat.domain.Loan;
import edu.fudan.sqat.domain.LoanPay;
import edu.fudan.sqat.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepayServiceTest {

    @Autowired
    private RepayService repayService;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private LoanPayRepository loanPayRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Test
    void loanInfo() throws Exception {
        Client client =new Client("zzz","name","male",18);
        clientRepository.save(client);
        Account account =new Account("zzz",1000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("zzz");
        Loan loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoanPay loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-04-02 01:01:01"), format.parse("2021-05-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);
        assertNotNull(repayService.loanInfo(account.getId()));

        //用于测试异常的出现
        try {
            repayService.loanInfo(9999999l);
            fail();
        } catch (Exception e) {
            assertEquals( "the loan id doesn't exist!",e.getMessage());
        }
    }


    @Test
    void repayment() throws Exception {
        //type不对
        try{
            RepaymentRequest repaymentRequest1=new RepaymentRequest(loanRepository.findLoanByAccountId(accountRepository.findAccountByIDCode("12345").getId()).iterator().next().getId(),777d,8,new Date());
            repayService.repayment(repaymentRequest1);
            fail();
        }catch (Exception e) {
            assertEquals("type is invalid!",e.getMessage());
        }

        //loan 不存在
        try {
            RepaymentRequest repaymentRequest1=new RepaymentRequest(9999999l,777d,0,new Date());
            repayService.repayment(repaymentRequest1);
            fail();
        } catch (Exception e) {
            assertEquals("the loan id doesn't exist!",e.getMessage());
        }

        //account不存在

        try {

            Loan loan = new Loan(99999l, 3000.0, 3, 0.1, false);
            loanRepository.save(loan);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LoanPay loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-04-02 01:01:01"), format.parse("2021-05-02 01:01:01"), 0.0, 0.0);
            loanPayRepository.save(loanPay);
            loan.getLoanPays().add(loanPay);
            loanRepository.save(loan);
            RepaymentRequest repaymentRequest1=new RepaymentRequest(loan.getId(),777d,0,new Date());
            repayService.repayment(repaymentRequest1);
            fail();
        } catch (Exception e) {
            assertEquals("the account id doesn't exist!",e.getMessage());
        }

        //loan存在  第一期还清 第二期还了部分 第三期部分还款全部还完
        Client client;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Account account;
        Loan loan;
        LoanPay loanPay;

        //此时用的账户是之前在sqatApplication建立的那个账户
        Account account1 = accountRepository.findAccountByIDCode("12345");
        Loan loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        RepayService repayService=new RepayService(accountRepository,loanPayRepository,loanRepository,transactionRepository);

        RepaymentRequest repaymentRequest=new RepaymentRequest(loan1.getId(),1100.0,0,format.parse("2021-02-03 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));

        repaymentRequest=new RepaymentRequest(loan1.getId(),1000.0,0,format.parse("2021-03-09 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));

        repaymentRequest=new RepaymentRequest(loan1.getId(),1205.0,0,format.parse("2021-04-03 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));

        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        assertTrue(loan1.getPaidOff());

        //loan存在 第一期部分还款大于全额
        client = new Client("1234", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("1234", 10.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("1234");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        account1 = accountRepository.findAccountByIDCode("1234");
        Double beforeAll=account1.getTotal();
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        repaymentRequest=new RepaymentRequest(loan1.getId(),1500.0,0,format.parse("2021-02-03 01:01:01"));
        try{
            repayService.repayment(repaymentRequest);
            fail();
        } catch (Exception e) {
            assertEquals("You don't have so much money in the account!",e.getMessage());
        }
        assertEquals(beforeAll,accountRepository.findAccountByIDCode("1234").getTotal());

        account1.setTotal(10000.0);
        accountRepository.save(account1);
        assertEquals("Error",repayService.repayment(repaymentRequest));

        repaymentRequest=new RepaymentRequest(loan1.getId(),10000.0,0,format.parse("2021-04-03 01:01:01"));
        assertEquals("Error",repayService.repayment(repaymentRequest));

        //全额付款 在当期全部还清
        repaymentRequest=new RepaymentRequest(loan1.getId(),10000.0,1,format.parse("2021-05-01 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        assertTrue(Math.abs(6535.0-accountRepository.findAccountByIDCode("1234").getTotal())<0.00001);

        //全额还款 还的钱并不够
        client = new Client("123", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("123", 10.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("123");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        account1 = accountRepository.findAccountByIDCode("123");
        beforeAll=account1.getTotal();
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        repaymentRequest=new RepaymentRequest(loan1.getId(),10.0,1,format.parse("2021-02-03 01:01:01"));
        assertEquals("Error",repayService.repayment(repaymentRequest));

        //全额还款 过期还清
        System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
        repaymentRequest=new RepaymentRequest(loan1.getId(),10.0,1,format.parse("2021-03-09 01:01:01"));
        assertEquals("Error",repayService.repayment(repaymentRequest));
        System.out.println(repayService.repayment(repaymentRequest));
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());

        account1.setTotal(10000.0);
        accountRepository.save(account1);
        repaymentRequest=new RepaymentRequest(loan1.getId(),10000.0,1,format.parse("2021-03-09 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        assertTrue(Math.abs(7745.0-accountRepository.findAccountByIDCode("123").getTotal())<0.00001);

        //这一次的第三期 部分还款 还两次
        repaymentRequest=new RepaymentRequest(loan1.getId(),100.0,0,format.parse("2021-04-09 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        assertFalse(loan1.getPaidOff());
        repaymentRequest=new RepaymentRequest(loan1.getId(),1000.0,0,format.parse("2021-04-09 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        assertTrue(loan1.getPaidOff());

        //部分还款 不够罚金
        client = new Client("12", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("12", 10000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("12");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        account1 = accountRepository.findAccountByIDCode("12");
        beforeAll=account1.getTotal();
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        repaymentRequest=new RepaymentRequest(loan1.getId(),10.0,0,format.parse("2021-03-09 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        account1 = accountRepository.findAccountByIDCode("12");
        System.out.println(account1.getTotal());
        assertTrue(Math.abs(9990.0-accountRepository.findAccountByIDCode("12").getTotal())<0.00001);
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        assertFalse(loan1.getPaidOff());

        //全额还款 第二期按时还清 但是总额没还完
        repaymentRequest=new RepaymentRequest(loan1.getId(),0.0,1,format.parse("2021-03-10 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        assertFalse(loan1.getPaidOff());

        //部分还款 第三期 还了一点点
        repaymentRequest=new RepaymentRequest(loan1.getId(),10.0,0,format.parse("2021-04-10 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        assertFalse(loan1.getPaidOff());

        //全额还款 付的钱够前一期的还款 但是因为过期 第二期的钱不够
        client = new Client("z123", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("z123", 1100.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("z123");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        account1 = accountRepository.findAccountByIDCode("z123");
        beforeAll=account1.getTotal();
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        repaymentRequest=new RepaymentRequest(loan1.getId(),1500.0,1,format.parse("2021-03-03 01:01:01"));
        assertEquals("Error",repayService.repayment(repaymentRequest));

        //全额还款
        client = new Client("zhangsan", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("zhangsan", 10000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("zhangsan");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        account1 = accountRepository.findAccountByIDCode("zhangsan");
        beforeAll=account1.getTotal();
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        repaymentRequest=new RepaymentRequest(loan1.getId(),1100.0,1,format.parse("2021-02-03 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));

        //第二期 相当于部分还款 但是没有全部还清
        System.out.println("第二期 相当于部分还款 但是没有全部还清");
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        System.out.println(loan1.getLoanPays().size());
        repaymentRequest=new RepaymentRequest(loan1.getId(),100.0,0,format.parse("2021-03-03 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        //第3期 相当于部分还款 但是全部还清
        System.out.println("第2期 相当于部分还款 但是全部还清");
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        System.out.println(loan1.getLoanPays().size());
        repaymentRequest=new RepaymentRequest(loan1.getId(),1000.0,0,format.parse("2021-03-03 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));

        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        System.out.println(loan1.getLoanPays().size());
        repaymentRequest=new RepaymentRequest(loan1.getId(),100.0,0,format.parse("2021-04-03 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));

        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        System.out.println(loan1.getLoanPays().size());
        repaymentRequest=new RepaymentRequest(loan1.getId(),1000.0,0,format.parse("2021-04-04 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
        /*
        for(LoanPay pay:loan1.getLoanPays()) {
            System.out.println("loanpay 应还金额:"+pay.getAmount());
            System.out.println("----loanpay 's detail : -----");
            System.out.println("Fine : " + pay.getFine());
            System.out.println("AfterPaidFine : " + pay.getFineAfterPaid());
            System.out.println("MoneyPaid : " + pay.getMoneyPaid());
        }
         */

        //full time in pay false

        client = new Client("zhangssan", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("zhangssan", 10000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("zhangssan");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        account1 = accountRepository.findAccountByIDCode("zhangssan");
        beforeAll=account1.getTotal();
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        repaymentRequest=new RepaymentRequest(loan1.getId(),1100.0,1,format.parse("2021-02-03 01:01:01"));
        assertEquals("Success",repayService.repayment(repaymentRequest));



    }


    @Test
    void addDate(){
        Date now=new Date();
        Date after=repayService.addDate(now,30);
        assertEquals(30,(after.getTime()-now.getTime())/(24*60*60*1000));
    }

    @Test
    void autoRepayment() throws Exception{

        // targetList为空
        System.out.println("---------" +
                "auto-----------");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Account account = accountRepository.findAccountByIDCode("23456");
        Loan loan=new Loan(account.getId(),10000.0,2,0.1,false);
        loanRepository.save(loan);
        LoanPay loanPay=new LoanPay(loan.getId(),10000*(1+0.1)/2,0d,1,format.parse("2021-03-02 01:01:01"),format.parse("2021-04-02 01:01:01"),0d,0d);
        System.out.println(loanPay.getLoanId());
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        RepayService repayService=new RepayService(accountRepository,loanPayRepository,loanRepository,transactionRepository);
        repayService.autoRepayment();

        Account account1 = accountRepository.findAccountByIDCode("23456");
        Loan loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        assertTrue( account1.getTotal()>0&&account1.getTotal()<20000.0);
        assertTrue( loan1.getLoanPays().size()==2);
        assertTrue(loan1.getPaidOff());



        Account account2 = accountRepository.findAccountByIDCode("12345");
        Loan loan2 = loanRepository.findLoanByAccountId(account2.getId()).iterator().next();
        loanRepository.save(loan2);
        account1.setTotal(10000.0);
        accountRepository.save(account1);
        loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

        // 账户欠款中不包含罚金，还款钱够
        Client client = new Client("zz12", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("zz12", 10000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("zz12");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000.0*(1+0.1)/3, 0.0, 1, format.parse("2021-04-02 01:01:01"), format.parse("2021-05-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        repayService.autoRepayment();


        // 账户欠款中包含罚金但钱够
        System.out.println("账户欠款中包含罚金但钱够");
        client = new Client("zz112", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("zz112", 10000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("zz112");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000.0*(1+0.1)/3, 0.0, 1, format.parse("2021-03-02 01:01:01"), format.parse("2021-04-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        repayService.autoRepayment();


        // 账户欠款中包含罚金但钱不够
        System.out.println("账户欠款中包含罚金但钱不够");
        client = new Client("zz1122", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("zz1122", 10.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("zz1122");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000.0*(1+0.1)/3, 0.0, 1, format.parse("2021-03-02 01:01:01"), format.parse("2021-04-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        repayService.autoRepayment();

        // 账户欠款中不包含罚金但还款钱不够
        client = new Client("zz1112", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("zz1112", 10.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("zz1112");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000.0*(1+0.1)/3, 0.0, 1, format.parse("2021-03-02 01:01:01"), format.parse("2021-04-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        repayService.autoRepayment();

        //no罚金 全还清
        client = new Client("wahaha", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("wahaha", 10000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("wahaha");
        loan = new Loan(account.getId(), 3000.0, 1, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000.0*(1+0.1)/1, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        repayService.autoRepayment();

        //no罚金 还不了
        client = new Client("waha", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("waha", 1000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("waha");
        loan = new Loan(account.getId(), 3000.0, 1, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000.0*(1+0.1)/1, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        repayService.autoRepayment();

        //未到期
        System.out.println("未到期");
        client = new Client("wahaa", "test01", "male", 25);
        clientRepository.save(client);
        account = new Account("wahaa", 1000.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("wahaa");
        loan = new Loan(account.getId(), 3000.0, 1, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000.0*(1+0.1)/1, 0.0, 1, format.parse("2021-04-02 01:01:01"), format.parse("2021-05-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);

        repayService.autoRepayment();

    }


}
