package edu.fudan.sqat.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RepaymentRequest {
    private Long loanId;
    private Double money;
    private Integer type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date currentTime;

    public RepaymentRequest() {
    }

    public RepaymentRequest(Long loanId, Double money, Integer type,Date currentTime) {
        this.loanId = loanId;
        this.money = money;
        this.type = type;
        this.currentTime=currentTime;
    }

    @Override
    public String toString() {
        return "RepaymentRequest{" +
                "loanId=" + loanId +
                ", money=" + money +
                ", type=" + type +
                ", currentTime=" + currentTime +
                '}';
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
