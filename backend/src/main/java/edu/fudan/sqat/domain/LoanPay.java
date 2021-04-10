package edu.fudan.sqat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


//    罚金a  还款金额b   实际c    c<a
//
//        a-c b
//                （a-c+b）*0。05    (b + a-c+b)


@Entity
public class LoanPay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long loanId;

    private Double amount;

    private Double fine;

    private Integer stage;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;

    // 判断有没有换完 最后一期money Paid==最后一期amount+fine
    private Double moneyPaid;

    public LoanPay() {
    }

    public LoanPay(Long loanId, Double amount, Double fine, Integer stage, Date start, Date end, Double moneyPaid) {
        this.loanId = loanId;
        this.amount = amount;
        this.fine = fine;
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.moneyPaid = moneyPaid;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFine() {
        return fine;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Double getMoneyPaid() {
        return moneyPaid;
    }

    public void setMoneyPaid(Double moneyPaid) {
        this.moneyPaid = moneyPaid;
    }

}
