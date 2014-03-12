package com.braintreegateway.exceptions;

public class NotFoundException extends BraintreeException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super();
    }
}
