package edu.fudan.sqat;

import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.domain.Transaction;
import edu.fudan.sqat.domain.User;
import edu.fudan.sqat.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class SqatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqatApplication.class, args);
    }

    @Bean
    public ApplicationRunner dataLoader(AccountRepository accountRepository,
                                        ClientRepository clientRepository,
                                        LoanPayRepository loanPayRepository,
                                        LoanRepository loanRepository,
                                        TransactionRepository transactionRepository,
                                        UserRepository userRepository) {
        return new ApplicationRunner() {

            @Override
            public void run(ApplicationArguments args) throws Exception {
                AccountLoader();
                ClientLoader();
                LoanPayLoader();
                LoanLoader();
                TransactionLoader();
                UserLoader();
            }

            private void AccountLoader() {
                Account account = new Account("12345", 100.0);
                accountRepository.save(account);
            }

            private void ClientLoader() {

            }

            private void LoanPayLoader() {

            }

            private void LoanLoader() {

            }

            private void TransactionLoader() throws ParseException {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Account account = accountRepository.findAccountByIDCode("12345");
                Transaction transaction;
                transaction = new Transaction(account, 30.0, 1000.0, "test1", format.parse("2021-04-01 00:00:00"));
                transactionRepository.save(transaction);
                transaction = new Transaction(account, 30.0, 1000.0, "test2", format.parse("2021-04-02 01:01:01"));
                transactionRepository.save(transaction);
                transaction = new Transaction(account, 30.0, 1000.0, "test3", format.parse("2021-04-03 23:59:59"));
                transactionRepository.save(transaction);
                transaction = new Transaction(account, 30.0, 1000.0, "test3", format.parse("2021-04-04 00:00:01"));
                transactionRepository.save(transaction);
            }

            private void UserLoader() {
                User user = new User("admin", "password", "admin");
                userRepository.save(user);
            }
        };
    }

}
