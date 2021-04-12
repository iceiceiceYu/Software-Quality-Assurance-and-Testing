package edu.fudan.sqat.exception;

public class PartialRepayException extends RuntimeException {
    private static final long serialVersionUID = -6124750305924344775L;

    public PartialRepayException() {
        super(" Partial repayment amount cannot be greater than full repayment!");
    }
}
