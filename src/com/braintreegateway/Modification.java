package com.braintreegateway;

import java.math.BigDecimal;

import com.braintreegateway.util.NodeWrapper;

public class Modification {
    private BigDecimal amount;
    private String id;
    private boolean neverExpires;
    private Integer numberOfBillingCycles;
    private Integer quantity;

    public Modification(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        id = node.findString("id");
        neverExpires = node.findBoolean("never-expires");
        numberOfBillingCycles = node.findInteger("number-of-billing-cycles");
        quantity = node.findInteger("quantity");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public Integer getNumberOfBillingCycles() {
        return numberOfBillingCycles;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean neverExpires() {
        return neverExpires;
    }
}
