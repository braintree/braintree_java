package com.braintreegateway;

import java.math.BigDecimal;

/**
 * Provides a fluent interface to build up refund requests around {@link Transaction Transactions}.
 */
public class TransactionRefundRequest extends Request {
    private BigDecimal amount;
    private String orderId;

    public TransactionRefundRequest() {
    }

    public TransactionRefundRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionRefundRequest orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    @Override
    public String toQueryString() {
        return toQueryString("transaction");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("transaction").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("amount", amount).
            addElement("orderId", orderId);

        return builder;
    }
}
