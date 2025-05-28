package com.braintreegateway.enums;

public enum Business {
    YES("Yes"),
    NO("No"),
    UNKNOWN("Unknown");

    private final String value;

    Business(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}