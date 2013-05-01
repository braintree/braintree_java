package com.braintreegateway.exceptions;

public class AuthorizationException extends BraintreeException {
    private static final long serialVersionUID = 1L;
    
    public AuthorizationException(String message) {
        super(message);
    }
}
