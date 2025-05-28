package com.braintreegateway.enums;

public enum Consumer {
    YES("Yes"),
    NO("No"),
    UNKNOWN("Unknown");

    private final String value;

    Consumer(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}