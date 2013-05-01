package com.braintreegateway.exceptions;

public class UnexpectedException extends BraintreeException {

    private static final long serialVersionUID = 1L;

    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
