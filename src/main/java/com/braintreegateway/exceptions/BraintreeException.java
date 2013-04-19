package com.braintreegateway.exceptions;

public class BraintreeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BraintreeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BraintreeException(String message) {
        super(message);
    }

    public BraintreeException() {
        super();
    }
}
