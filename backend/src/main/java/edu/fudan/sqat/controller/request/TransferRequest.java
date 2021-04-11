package edu.fudan.sqat.controller.request;

public class TransferRequest {
    private String fromIDCode;
    private String toIDCode;
    private Double amount;

    public TransferRequest() {
    }

    public TransferRequest(String fromIDCode, String toIDCode, Double amount) {
        this.fromIDCode = fromIDCode;
        this.toIDCode = toIDCode;
        this.amount = amount;
    }

    public String getFromIDCode() {
        return fromIDCode;
    }

    public void setFromIDCode(String fromIDCode) {
        this.fromIDCode = fromIDCode;
    }

    public String getToIDCode() {
        return toIDCode;
    }

    public void setToIDCode(String toIDCode) {
        this.toIDCode = toIDCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
