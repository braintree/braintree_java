package com.braintreegateway;

import com.braintreegateway.Dispute.Status;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public final class DisputeStatusHistory {

    private final Calendar disbursementDate;
    private final Calendar effectiveDate;
    private final Calendar timestamp;
    private final Dispute.Status status;

    public DisputeStatusHistory(NodeWrapper node) {
        disbursementDate = node.findDate("disbursement-date");
        effectiveDate = node.findDate("effective-date");
        timestamp = node.findDateTime("timestamp");
        status = EnumUtils.findByName(Dispute.Status.class, node.findString("status"), Status.UNRECOGNIZED);
    }

    public Calendar getEffectiveDate() {
        return effectiveDate;
    }

    public Calendar getDisbursementDate() {
        return disbursementDate;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public Dispute.Status getStatus() {
        return status;
    }
}
