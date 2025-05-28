package com.braintreegateway.enums;

public enum Corporate {
    YES("Yes"),
    NO("No"),
    UNKNOWN("Unknown");

    private final String value;

    Corporate(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}