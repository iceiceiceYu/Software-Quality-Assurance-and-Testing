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
    private Double fineAfterPaid;
    private Integer stage;  //1 2 3

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;

    // 判断有没有还完 最后一期money Paid==最后一期amount 且fineAfterPaid==0
    //moneyPaid 只算还款部分的moneyPaid
    private Double moneyPaid;

    public LoanPay() {
    }

    public LoanPay(Long loanId, Double amount, Double fine, Integer stage, Date start, Date end, Double moneyPaid,Double fineAfterPaid) {
        this.loanId = loanId;
        this.amount = amount;
        this.fine = fine;
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.moneyPaid = moneyPaid;
        //初始化时 fineAfterPaid总与fine相同
        this.fineAfterPaid=fineAfterPaid;
    }

    //重写equals方法, 最佳实践就是如下这种判断顺序:
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LoanPay))
            return false;
        if (obj == this)
            return true;
        return this.getId().equals(((LoanPay) obj).getId());
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

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
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

    public Double getFineAfterPaid() {
        return fineAfterPaid;
    }

    public void setFineAfterPaid(Double fineAfterPaid) {
        this.fineAfterPaid = fineAfterPaid;
    }
}
