package com.braintreegateway;

import java.util.Calendar;

import com.braintreegateway.Dispute.Status;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public final class DisputeStatusHistory {

    private final Calendar effectiveDate;
    private final Calendar timestamp;
    private final Dispute.Status status;

    public DisputeStatusHistory(NodeWrapper node) {
        effectiveDate = node.findDate("timestamp");
        timestamp = node.findDateTime("timestamp");
        status = EnumUtils.findByName(Dispute.Status.class, node.findString("status"), Status.UNRECOGNIZED);
    }

    public Calendar getEffectiveDate() {
        return effectiveDate;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public Dispute.Status getStatus() {
        return status;
    }
}
