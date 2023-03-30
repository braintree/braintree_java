package com.braintreegateway.exceptions;

public class ClientTokenGenerationException extends RuntimeException{
    public ClientTokenGenerationException(final String message) {
        super(message);
    }
}
