package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public class AuthorizationAdjustment {
    private BigDecimal amount;
    private Boolean success;
    private Calendar timestamp;

    public AuthorizationAdjustment(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        success = node.findBoolean("success");
        timestamp = node.findDateTime("timestamp");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Boolean isSuccess() {
        return success;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }
}
