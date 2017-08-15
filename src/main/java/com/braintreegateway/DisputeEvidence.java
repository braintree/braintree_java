package com.braintreegateway;

import com.braintreegateway.Dispute.Status;
import com.braintreegateway.util.NodeWrapper;

import java.util.Calendar;

public final class DisputeEvidence {

    private final Calendar createdAt;
    private final Calendar sentToProcessorAt;
    private final String comment;
    private final String id;
    private final String url;

    public DisputeEvidence(NodeWrapper node) {
        createdAt = node.findDateTime("created-at");
        sentToProcessorAt = node.findDate("sent-to-processor-at");
        comment = node.findString("comment");
        id = node.findString("id");
        url = node.findString("url");
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getSentToProcessorAt() {
        return sentToProcessorAt;
    }

    public String getComment() {
        return comment;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
