package com.braintreegateway;

import java.math.BigDecimal;

public class TransactionCloneRequest extends Request {
    private BigDecimal amount;

    public TransactionCloneRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("transactionClone").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("amount", amount);

        return builder;
    }
}
