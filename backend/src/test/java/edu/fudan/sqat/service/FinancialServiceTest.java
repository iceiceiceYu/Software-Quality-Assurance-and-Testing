package edu.fudan.sqat.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinancialServiceTest {

    @Autowired
    private FinancialService financialService;

    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void afterEach() {

    }

    @Test
    void increase() {
    }

    @Test
    void purchaseInfo() {
//        assertNotNull();
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
