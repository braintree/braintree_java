package com.braintreegateway.exceptions;

public class ServerException extends BraintreeException {
    private static final long serialVersionUID = 1L;

    public ServerException(String message) {
        super(message);
    }

    public ServerException() {
        super();
    }
}
