package com.braintreegateway;

import java.math.BigDecimal;

public class TransactionCloneRequest extends Request {
    private BigDecimal amount;
    private String channel;
    private TransactionOptionsCloneRequest transactionOptionsCloneRequest;

    public TransactionCloneRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionCloneRequest channel(String channel) {
        this.channel = channel;
        return this;
    }

    public TransactionOptionsCloneRequest options() {
        transactionOptionsCloneRequest = new TransactionOptionsCloneRequest(this);
        return transactionOptionsCloneRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("transactionClone").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("amount", amount).
            addElement("channel", channel).
            addElement("options", transactionOptionsCloneRequest);

        return builder;
    }
}
