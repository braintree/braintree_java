package com.braintreegateway.exceptions;

public class InvalidSignatureException extends BraintreeException {
    private static final long serialVersionUID = 1L;

    public InvalidSignatureException(String message) {
        super(message);
    }

    public InvalidSignatureException() {
        super();
    }
}
