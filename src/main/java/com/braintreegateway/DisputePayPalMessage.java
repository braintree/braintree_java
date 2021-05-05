package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public final class DisputePayPalMessage {

    private final String message;
    private final String sender;
    private final Calendar sentAt;

    public DisputePayPalMessage(NodeWrapper node) {
        message = node.findString("message");
        sender = node.findString("sender");
        sentAt = node.findDateTime("sent-at");
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public Calendar getSentAt() {
        return sentAt;
    }
}
