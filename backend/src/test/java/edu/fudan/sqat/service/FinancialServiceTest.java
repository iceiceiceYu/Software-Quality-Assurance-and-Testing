package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.*;
import edu.fudan.sqat.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FinancialServiceTest {

    @Autowired
    private FinancialService financialService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private FinancialProductRepository financialProductRepository;

    @Autowired
    private LoanPayRepository loanPayRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        Account account = new Account("99999", 1000000.0);
        accountRepository.save(account);
        Client client = new Client("99999", "code test01", "male", 30);
        clientRepository.save(client);
        account = new Account("00091", 100.0);
        accountRepository.save(account);
        client = new Client("00091", "code test02", "male", 30);
        clientRepository.save(client);

        FinancialProduct financialProduct;
        financialProduct = new FinancialProduct("test stock", "stock", 500.0);
        financialProductRepository.save(financialProduct);
        financialProduct = new FinancialProduct("test fund", "fund", 50000.0);
        financialProductRepository.save(financialProduct);
        financialProduct = new FinancialProduct("test deposit", "deposit", 5000.0);
        financialProductRepository.save(financialProduct);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Purchase purchase;
        purchase = new Purchase("99999", "test stock", "stock", 100, format.parse("2021-04-01"), 50000.0, 0.0);
        purchaseRepository.save(purchase);
        purchase = new Purchase("99999", "test fund", "fund", null, format.parse("2021-04-01"), 50000.0, 0.0);
        purchaseRepository.save(purchase);
        purchase = new Purchase("99999", "test deposit", "deposit", null, format.parse("2021-04-01"), 5000.0, 0.0);
        purchaseRepository.save(purchase);
        purchase = new Purchase("99999", "null product", "null", null, format.parse("2021-04-02"), 0.0, 0.0);
        purchaseRepository.save(purchase);
    }

    @AfterEach
    void afterEach() {
        transactionRepository.deleteAll();
        Account account = accountRepository.findAccountByIDCode("99999");
        accountRepository.delete(account);
        Client client = clientRepository.findClientByIDCode("99999");
        clientRepository.delete(client);
        account = accountRepository.findAccountByIDCode("00091");
        accountRepository.delete(account);
        client = clientRepository.findClientByIDCode("00091");
        clientRepository.delete(client);

        FinancialProduct financialProduct;
        financialProduct = financialProductRepository.findFinancialProductByName("test stock");
        financialProductRepository.delete(financialProduct);
        financialProduct = financialProductRepository.findFinancialProductByName("test fund");
        financialProductRepository.delete(financialProduct);
        financialProduct = financialProductRepository.findFinancialProductByName("test deposit");
        financialProductRepository.delete(financialProduct);

        purchaseRepository.deleteAll();
    }

    @Test
    void increase() {
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
        assertEquals("success", financialService.increase());
    }

    @Test
    void purchaseInfo() {
        assertNotNull(financialService.purchaseInfo("99999"));
        assertEquals(4, financialService.purchaseInfo("99999").size());
    }

    @Test
    void accountLevel() {
        assertEquals(3, financialService.accountLevel("22222"));
        assertEquals(2, financialService.accountLevel("12345"));
        assertEquals(1, financialService.accountLevel("00000"));
    }

    @Test
    void allInfo() {
        assertNotNull(financialService.allInfo());
    }

    @Test
    void purchaseProduct() throws Exception {
        assertEquals("success", financialService.purchaseProduct("99999", "test stock", "stock", 100, new Date()));
        assertEquals("cannot pay financial product", financialService.purchaseProduct("00091", "test fund", "fund", null, new Date()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Client client = new Client("00092", "test member", "male", 25);
        clientRepository.save(client);
        Account account = new Account("00092", 5.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("00092");
        Loan loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        LoanPay loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 5.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);
        assertEquals("cannot pay financial product", financialService.purchaseProduct("00092", "test fund", "fund", null, new Date()));
        client = new Client("00093", "test member", "male", 25);
        clientRepository.save(client);
        account = new Account("00093", 2.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("00093");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 5.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);
        assertEquals("cannot pay fine", financialService.purchaseProduct("00092", "test fund", "fund", null, new Date()));
    }

    @Test
    void checkFine() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Client client = new Client("00094", "test member", "male", 25);
        clientRepository.save(client);
        Account account = new Account("00094", 5.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("00094");
        Loan loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        LoanPay loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 5.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);
        assertEquals(5.0, financialService.checkFine(account.getId()));

        client = new Client("00095", "test member", "male", 25);
        clientRepository.save(client);
        account = new Account("00095", 5.0);
        accountRepository.save(account);
        account = accountRepository.findAccountByIDCode("00095");
        loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
        loanRepository.save(loan);
        loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
        loanPayRepository.save(loanPay);
        loan.getLoanPays().add(loanPay);
        loanRepository.save(loan);
        assertEquals(0.0, financialService.checkFine(account.getId()));
    }
}
