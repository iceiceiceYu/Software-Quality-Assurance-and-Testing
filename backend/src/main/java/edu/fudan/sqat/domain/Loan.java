package edu.fudan.sqat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zyl
 */
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private Long accountId;
    private Double amount;
    private Integer stageCount;
    private Double interest;
    private Boolean isPaidOff;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<LoanPay> loanPays = new ArrayList<>();

    public Loan() {
    }

    public Loan(Long id, Double amount, Integer stageCount, Double interest, Boolean isPaidOff) {
        this.accountId = id;
        this.amount = amount;
        this.stageCount = stageCount;
        this.interest = interest;
        this.isPaidOff = isPaidOff;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getStageCount() {
        return stageCount;
    }

    public void setStageCount(Integer stageCount) {
        this.stageCount = stageCount;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Boolean getPaidOff() {
        return isPaidOff;
    }

    public void setPaidOff(Boolean paidOff) {
        isPaidOff = paidOff;
    }

    public List<LoanPay> getLoanPays() {
        return loanPays;
    }

    public void setLoanPays(List<LoanPay> loanPays) {
        this.loanPays = loanPays;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", stageCount=" + stageCount +
                ", interest=" + interest +
                ", isPaidOff=" + isPaidOff +
                ", loanPays=" + loanPays +
                '}';
    }
}
