package com.braintreegateway;

import com.braintreegateway.Transaction.Source;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public class StatusEvent {

    private BigDecimal amount;
    private Status status;
    private Calendar timestamp;
    private Source source;
    private String user;

    public StatusEvent(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        status = EnumUtils.findByName(Status.class, node.findString("status"));
        timestamp = node.findDateTime("timestamp");
        source = EnumUtils.findByName(Source.class, node.findString("transaction-source"));
        user = node.findString("user");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Status getStatus() {
        return status;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public Source getSource() {
        return source;
    }

    public String getUser() {
        return user;
    }
}
