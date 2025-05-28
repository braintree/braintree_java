package com.braintreegateway.enums;

public enum Purchase {
    YES("Yes"),
    NO("No"),
    UNKNOWN("Unknown");

    private final String value;

    Purchase(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}