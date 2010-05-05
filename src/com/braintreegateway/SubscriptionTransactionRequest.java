package com.braintreegateway;

import java.math.BigDecimal;

import com.braintreegateway.Transaction.Type;

public class SubscriptionTransactionRequest extends Request {

    private BigDecimal amount;
    private String subscriptionId;

    public SubscriptionTransactionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toQueryString(String parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toQueryString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<transaction>");
        builder.append(buildXMLElement("amount", amount));
        builder.append(buildXMLElement("subscriptionId", subscriptionId));
        builder.append(buildXMLElement("type", Type.SALE.toString().toLowerCase()));
        builder.append("</transaction>");
        return builder.toString();
    }

    public SubscriptionTransactionRequest subscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

}
