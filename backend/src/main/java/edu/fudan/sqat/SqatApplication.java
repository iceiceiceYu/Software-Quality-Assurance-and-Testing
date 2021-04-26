package edu.fudan.sqat;

import edu.fudan.sqat.controller.request.RepaymentRequest;
import edu.fudan.sqat.domain.*;
import edu.fudan.sqat.repository.*;
import edu.fudan.sqat.service.RepayService;
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
                LoanAndLoanPayLoader(loanRepository, loanPayRepository);
                PurchaseLoader(purchaseRepository);
                TransactionLoader(transactionRepository, accountRepository);
                UserLoader(userRepository);

                //testing req1
/*
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Account account1 = accountRepository.findAccountByIDCode("12345");
                Loan loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                RepayService repayService=new RepayService(accountRepository,loanPayRepository,loanRepository,transactionRepository);

                System.out.println("before account's total : " + account1.getTotal());
                RepaymentRequest repaymentRequest=new RepaymentRequest(loan1.getId(),1100.0,0,format.parse("2021-02-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));

                account1 = accountRepository.findAccountByIDCode("12345");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                System.out.println("after then account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                    System.out.println("start :"+format.format(loanPay.getStart()));
                    System.out.println("end :"+format.format(loanPay.getEnd()));
                }

                repaymentRequest=new RepaymentRequest(loan1.getId(),1000.0,0,format.parse("2021-03-12 01:01:01"));
                System.out.println("2222222222"+repayService.repayment(repaymentRequest));

                account1 = accountRepository.findAccountByIDCode("12345");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                System.out.println("after then account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                }

                repaymentRequest=new RepaymentRequest(loan1.getId(),1205.0,0,format.parse("2021-04-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));

                account1 = accountRepository.findAccountByIDCode("12345");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                System.out.println("after then account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                }


                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

                System.out.println(loan1.getPaidOff());
*/
                /*
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Account account1 = accountRepository.findAccountByIDCode("12345");
                Loan loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                RepayService repayService=new RepayService(accountRepository,loanPayRepository,loanRepository,transactionRepository);

                System.out.println("before account's total : " + account1.getTotal());
                RepaymentRequest repaymentRequest=new RepaymentRequest(loan1.getId(),2000.0,0,format.parse("2021-03-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));

                repaymentRequest=new RepaymentRequest(loan1.getId(),1000.0,0,format.parse("2021-03-09 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));

                account1 = accountRepository.findAccountByIDCode("12345");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                System.out.println("after then account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                }

                repaymentRequest=new RepaymentRequest(loan1.getId(),1100.0,0,format.parse("2021-04-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));

                account1 = accountRepository.findAccountByIDCode("12345");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                System.out.println("after then account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                }


                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

                System.out.println(loan1.getPaidOff());
*/
/*
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Account account1 = accountRepository.findAccountByIDCode("12345");
                Loan loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                RepayService repayService=new RepayService(accountRepository,loanPayRepository,loanRepository,transactionRepository);
                RepaymentRequest repaymentRequest=new RepaymentRequest(loan1.getId(),2000.0,1,format.parse("2021-02-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));
                System.out.println("after then account's total : " + account1.getTotal());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                }

                 repaymentRequest=new RepaymentRequest(loan1.getId(),1000.0,0,format.parse("2021-04-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));
                account1 = accountRepository.findAccountByIDCode("12345");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

                System.out.println("after then account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                }
                System.out.println(accountRepository.findAccountByIDCode("12345").getTotal());
*/
                /*
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Account account1 = accountRepository.findAccountByIDCode("12345");
                Loan loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                RepayService repayService=new RepayService(accountRepository,loanPayRepository,loanRepository,transactionRepository);

                System.out.println("before account's total : " + account1.getTotal());
                RepaymentRequest repaymentRequest=new RepaymentRequest(loan1.getId(),null,1,format.parse("2021-03-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));

                repaymentRequest=new RepaymentRequest(loan1.getId(),null,1,format.parse("2021-04-03 01:01:01"));
                System.out.println(repayService.repayment(repaymentRequest));

                account1 = accountRepository.findAccountByIDCode("12345");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();
                System.out.println("after then account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                for(LoanPay loanPay:loan1.getLoanPays()) {
                    System.out.println("loanpay 应还金额:"+loanPay.getAmount());
                    System.out.println("----loanpay 's detail : -----");
                    System.out.println("Fine : " + loanPay.getFine());
                    System.out.println("AfterPaidFine : " + loanPay.getFineAfterPaid());
                    System.out.println("MoneyPaid : " + loanPay.getMoneyPaid());
                }
                System.out.println(loan1.getPaidOff());

                 */



                //testing req2
/*
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

                Account account1 = accountRepository.findAccountByIDCode("12345");
                Loan loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

                System.out.println("-12345- account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                System.out.println(loan1.getPaidOff());

                account1 = accountRepository.findAccountByIDCode("23456");
                loan1 = loanRepository.findLoanByAccountId(account1.getId()).iterator().next();

                System.out.println("-23456- account's total : " + account1.getTotal());
                System.out.println("loan's loanpay size : " + loan1.getLoanPays().size());
                System.out.println(loan1.getPaidOff());

 */

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
                account = new Account("22222", 4000.0);
                accountRepository.save(account);
                account = new Account("00000", 1000000.0);
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
                client = new Client("22222", "test05", "male", 20);
                clientRepository.save(client);
                client = new Client("00000", "test06", "male", 50);
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

            private void LoanAndLoanPayLoader(LoanRepository loanRepository, LoanPayRepository loanPayRepository) throws ParseException {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Account account;
                Loan loan;
                LoanPay loanPay;

                account = accountRepository.findAccountByIDCode("12345");
                loan = new Loan(account.getId(), 3000.0, 3, 0.1, false);
                loanRepository.save(loan);
                loanPay = new LoanPay(loan.getId(), 3000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-02 01:01:01"), format.parse("2021-03-02 01:01:01"), 0.0, 0.0);
                loanPayRepository.save(loanPay);
                loan.getLoanPays().add(loanPay);
                loanRepository.save(loan);

                account = accountRepository.findAccountByIDCode("22222");
                loan = new Loan(account.getId(), 5000.0, 3, 0.1, false);
                loanRepository.save(loan);
                loanPay = new LoanPay(loan.getId(), 5000 * (1 + 0.1) / 3, 0.0, 1, format.parse("2021-02-03 01:01:01"), format.parse("2021-03-03 01:01:01"), 0.0, 0.0);
                loanPayRepository.save(loanPay);
                loan.getLoanPays().add(loanPay);
                loanRepository.save(loan);
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
