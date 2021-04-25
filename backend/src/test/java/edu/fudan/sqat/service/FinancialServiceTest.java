package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.domain.Client;
import edu.fudan.sqat.domain.FinancialProduct;
import edu.fudan.sqat.domain.Purchase;
import edu.fudan.sqat.repository.*;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
    void beforeEach() {
        Account account = new Account("99999", 1000000.0);
        accountRepository.save(account);
        Client client = new Client("99999", "code test01", "male", 30);
        clientRepository.save(client);

        FinancialProduct financialProduct;
        financialProduct = new FinancialProduct("test stock", "stock", 500.0);
        financialProductRepository.save(financialProduct);
        financialProduct = new FinancialProduct("test fund", "fund", 50000.0);
        financialProductRepository.save(financialProduct);
        financialProduct = new FinancialProduct("test deposit", "deposit", 5000.0);
        financialProductRepository.save(financialProduct);

        Purchase purchase;
        purchase = new Purchase("99999", "test stock", "stock", 100, new Date(), 50000.0, 0.0);
        purchaseRepository.save(purchase);
        purchase = new Purchase("99999", "test fund", "fund", null, new Date(), 50000.0, 0.0);
        purchaseRepository.save(purchase);
        purchase = new Purchase("99999", "test deposit", "deposit", null, new Date(), 5000.0, 0.0);
        purchaseRepository.save(purchase);
    }

    @AfterEach
    void afterEach() {
        transactionRepository.deleteAll();
        Account account = accountRepository.findAccountByIDCode("99999");
        accountRepository.delete(account);
        Client client = clientRepository.findClientByIDCode("99999");
        clientRepository.delete(client);

        FinancialProduct financialProduct;
        financialProduct = financialProductRepository.findFinancialProductByName("test stock");
        financialProductRepository.delete(financialProduct);
        financialProduct = financialProductRepository.findFinancialProductByName("test fund");
        financialProductRepository.delete(financialProduct);
        financialProduct = financialProductRepository.findFinancialProductByName("test deposit");
        financialProductRepository.delete(financialProduct);

        Purchase purchase;
        purchase = purchaseRepository.findPurchaseByIDCodeAndName("99999", "test stock");
        purchaseRepository.delete(purchase);
        purchase = purchaseRepository.findPurchaseByIDCodeAndName("99999", "test fund");
        purchaseRepository.delete(purchase);
        purchase = purchaseRepository.findPurchaseByIDCodeAndName("99999", "test deposit");
        purchaseRepository.delete(purchase);
    }

    @Test
    void increase() {
        assertEquals("success", financialService.increase());
    }

    @Test
    void purchaseInfo() {
        assertNotNull(financialService.purchaseInfo("99999"));
        assertEquals(3, financialService.purchaseInfo("99999").size());
    }

    @Test
    void accountLevel() {

    }

    @Test
    void allInfo() {
        assertNotNull(financialService.allInfo());
    }

    @Test
    void purchaseProduct() {

    }

    @Test
    void checkFine() {

    }
}
