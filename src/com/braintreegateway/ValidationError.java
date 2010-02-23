package com.braintreegateway;

public class ValidationError {
    private String attribute;
    private ValidationErrorCode code;
    private String message;

    public ValidationError(String attribute, ValidationErrorCode code, String message) {
        this.attribute = attribute;
        this.code = code;
        this.message = message;
    }

    public String getAttribute() {
        return attribute;
    }

    public ValidationErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
