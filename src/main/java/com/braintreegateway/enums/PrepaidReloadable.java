package com.braintreegateway.enums;

public enum PrepaidReloadable {
    YES("Yes"),
    NO("No"),
    UNKNOWN("Unknown");

    private final String value;

    PrepaidReloadable(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}