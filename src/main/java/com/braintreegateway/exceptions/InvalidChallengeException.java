package com.braintreegateway.exceptions;

public class InvalidChallengeException extends BraintreeException {
    public InvalidChallengeException(String message) {
        super(message);
    }

    public InvalidChallengeException() {
        super();
    }
}
