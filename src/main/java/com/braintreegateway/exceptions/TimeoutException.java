package com.braintreegateway.exceptions;

public class TimeoutException extends BraintreeException {
    private static final long serialVersionUID = 1L;

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
