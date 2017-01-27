package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.util.Calendar;

public class AchMandate {

    private String text;
    private Calendar acceptedAt;

    public AchMandate(NodeWrapper node) {
        text = node.findString("text");
        acceptedAt = node.findDate("accepted-at");
    }

    public String getText() {
        return text;
    }

    public Calendar getAcceptedAt() {
        return acceptedAt;
    }
}
