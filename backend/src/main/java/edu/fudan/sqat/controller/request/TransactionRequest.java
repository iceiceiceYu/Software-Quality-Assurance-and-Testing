package edu.fudan.sqat.controller.request;

public class TransactionRequest {
    private String start;
    private String end;

    public TransactionRequest() {
    }

    public TransactionRequest(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
