package com.braintreegateway.enums;

public enum ThreeDSecurePassThruNetwork {
    EFTPOS("eftpos"),
    MASTER_CARD("Mastercard"),
    VISA("Visa");

    private final String value;

    ThreeDSecurePassThruNetwork(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
