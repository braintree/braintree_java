package com.braintreegateway;

import java.math.BigDecimal;

import com.braintreegateway.util.NodeWrapper;

public class Modification {
    private BigDecimal amount;
    private Integer quantity;
    
    public Modification(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        quantity = node.findInteger("quantity");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
