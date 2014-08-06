package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;

public final class TransactionDetails {
    private final String id;
    private final BigDecimal amount;

    public TransactionDetails(NodeWrapper node) {
        id = node.findString("id");
        amount = node.findBigDecimal("amount");
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
