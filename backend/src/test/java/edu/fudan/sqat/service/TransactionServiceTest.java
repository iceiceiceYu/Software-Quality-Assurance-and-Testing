package edu.fudan.sqat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    void find() throws ParseException {
        assertNotNull(transactionService.find(null, null));
        assertEquals(4, transactionService.find(null, null).size());
        assertNotNull(transactionService.find("2021-04-01", "2021-04-02"));
        assertEquals(2, transactionService.find("2021-04-01", "2021-04-02").size());
    }

    @Test
    void findAll() {
        assertNotNull(transactionService.findAll());
    }

    @Test
    void findBetween() throws ParseException {
        assertNotNull(transactionService.findBetween("2021-04-01", "2021-04-04"));
    }
}
