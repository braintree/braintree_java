package com.braintreegateway;

/**
 * Represents an validation error from the gateway.
 */
public class ValidationError {
    private String attribute;
    private ValidationErrorCode code;
    private String message;

    public ValidationError(String attribute, ValidationErrorCode code, String message) {
        this.attribute = attribute;
        this.code = code;
        this.message = message;
    }

    /**
     * Returns the attribute that this error references, e.g. amount or expirationDate.
     * @return the attribute.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Returns the {@link ValidationErrorCode} for the specific validation error.
     * @return a {@link ValidationErrorCode}.
     */
    public ValidationErrorCode getCode() {
        return code;
    }

    /**
     * Returns the message associated with the validation error.  Messages may change over time; rely on {@link #getCode()} for comparisons.
     * @return a String for the message.
     */
    public String getMessage() {
        return message;
    }

    public boolean equals(Object e) {
        ValidationError ve = (ValidationError)e;
        return attribute.equals(ve.attribute) && code.equals(ve.code) && message.equals(ve.message);
    }
}
