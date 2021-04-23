package edu.fudan.sqat.service;


import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.domain.Client;
import edu.fudan.sqat.domain.Loan;
import edu.fudan.sqat.domain.LoanPay;
import edu.fudan.sqat.repository.AccountRepository;
import edu.fudan.sqat.repository.ClientRepository;
import edu.fudan.sqat.repository.LoanPayRepository;
import edu.fudan.sqat.repository.LoanRepository;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.rules.*;

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


    @Test
    void loanInfo() throws Exception {
        Client client =new Client("123","name","male",18);
        clientRepository.save(client);
        Account account =new Account("123");
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("123");
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
            assertEquals(e.getMessage(), "the loan id doesn't exist!");
        }
    }

    @Test
    void repayment(){

    }

    @Test
    void findCurrentLoanPay(){

    }

    @Test
    void realPartialPayment(){

    }

    @Test
    void addNewLoanPay(){

    }

    @Test
    void fullPayInTime(){

    }

    @Test
    void addDate(){
        Date now=new Date();
        Date after=repayService.addDate(now,30);
        assertEquals(30,(after.getTime()-now.getTime())/(24*60*60*1000));
    }

    @Test
    void autoRepayment(){

    }


}