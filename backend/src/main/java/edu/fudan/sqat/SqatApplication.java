package edu.fudan.sqat;

import edu.fudan.sqat.domain.*;
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
                                        FinancialProductRepository financialProductRepository,
                                        LoanPayRepository loanPayRepository,
                                        LoanRepository loanRepository,
                                        PurchaseRepository purchaseRepository,
                                        TransactionRepository transactionRepository,
                                        UserRepository userRepository) {
        return new ApplicationRunner() {

            @Override
            public void run(ApplicationArguments args) throws Exception {
                AccountLoader(accountRepository);
                ClientLoader(clientRepository);
                FinancialProductLoader(financialProductRepository);
                LoanPayLoader(loanPayRepository);
                LoanLoader(loanRepository);
                PurchaseLoader(purchaseRepository);
                TransactionLoader(transactionRepository, accountRepository);
                UserLoader(userRepository);
            }

            private void AccountLoader(AccountRepository accountRepository) {
                Account account;
                account = new Account("12345", 10000.0);
                accountRepository.save(account);
                account = new Account("23456", 20000.0);
                accountRepository.save(account);
                account = new Account("34567", 50000.0);
                accountRepository.save(account);
                account = new Account("11111", 500000.0);
                accountRepository.save(account);
            }

            private void ClientLoader(ClientRepository clientRepository) {
                Client client;
                client = new Client("12345", "test01", "male", 25);
                clientRepository.save(client);
                client = new Client("23456", "test02", "female", 30);
                clientRepository.save(client);
                client = new Client("34567", "test03", "female", 40);
                clientRepository.save(client);
                client = new Client("11111", "test04", "female", 45);
                clientRepository.save(client);
            }

            private void FinancialProductLoader(FinancialProductRepository financialProductRepository) {
                FinancialProduct financialProduct;
                financialProduct = new FinancialProduct("stock01", "stock", 100.0);
                financialProductRepository.save(financialProduct);
                financialProduct = new FinancialProduct("stock02", "stock", 200.0);
                financialProductRepository.save(financialProduct);
                financialProduct = new FinancialProduct("fund01", "fund", 10000.0);
                financialProductRepository.save(financialProduct);
                financialProduct = new FinancialProduct("fund02", "fund", 20000.0);
                financialProductRepository.save(financialProduct);
                financialProduct = new FinancialProduct("deposit01", "deposit", 1000.0);
                financialProductRepository.save(financialProduct);
                financialProduct = new FinancialProduct("deposit02", "deposit", 2000.0);
                financialProductRepository.save(financialProduct);
            }

            private void LoanPayLoader(LoanPayRepository loanPayRepository) {

            }

            private void LoanLoader(LoanRepository loanRepository) {

            }

            private void PurchaseLoader(PurchaseRepository purchaseRepository) {

            }

            private void TransactionLoader(TransactionRepository transactionRepository, AccountRepository accountRepository) throws ParseException {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Account account = accountRepository.findAccountByIDCode("12345");
                Transaction transaction;
                transaction = new Transaction(account, 30.0, 10030.0, "test1", format.parse("2021-04-01 00:00:00"));
                transactionRepository.save(transaction);
                transaction = new Transaction(account, 30.0, 10060.0, "test2", format.parse("2021-04-02 01:01:01"));
                transactionRepository.save(transaction);
                transaction = new Transaction(account, 30.0, 10090.0, "test3", format.parse("2021-04-03 23:59:59"));
                transactionRepository.save(transaction);
                transaction = new Transaction(account, 30.0, 10120.0, "test4", format.parse("2021-04-04 00:00:01"));
                transactionRepository.save(transaction);
            }

            private void UserLoader(UserRepository userRepository) {
                User user = new User("admin", "password", "admin");
                userRepository.save(user);
            }
        };
    }

}
