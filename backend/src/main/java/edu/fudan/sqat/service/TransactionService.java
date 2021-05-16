package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.Transaction;
import edu.fudan.sqat.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> find(String start, String end) throws ParseException {
        if (start == null && end == null) {
            return findAll();
        }
        if (start != null && end != null) {
            return findBetween(start, end);
        }
        return null;
    }

    public List<Transaction> findAll() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public List<Transaction> findBetween(String start, String end) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = format.parse(start + " 00:00:00");
        Date endDate = format.parse(end + " 23:59:59");
        System.out.println(startDate);
        System.out.println(endDate);
        return (List<Transaction>) transactionRepository.findTransactionByTimeBetween(startDate, endDate);
    }
}
