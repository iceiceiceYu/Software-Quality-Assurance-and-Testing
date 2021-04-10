package edu.fudan.sqat.controller.request;

public class RepaymentRequest {
    private Long loanId;
    private Double money;
    private Integer type;

    public RepaymentRequest() {
    }

    public RepaymentRequest(Long loanId, Double money, Integer type) {
        this.loanId = loanId;
        this.money = money;
        this.type = type;
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
}
