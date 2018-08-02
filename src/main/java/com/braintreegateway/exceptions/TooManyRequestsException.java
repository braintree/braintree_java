package com.braintreegateway.exceptions;

public class TooManyRequestsException extends BraintreeException {
    private static final long serialVersionUID = 1L;

    public TooManyRequestsException() {
        super();
    }

    public TooManyRequestsException(String message) {
        super(message);
    }

}
