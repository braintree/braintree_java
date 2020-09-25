package com.braintreegateway;

import com.braintreegateway.Transaction.Type;
import java.math.BigDecimal;

public class SubscriptionTransactionRequest extends Request {

    private BigDecimal amount;
    private String subscriptionId;
    private SubscriptionTransactionOptionsRequest subscriptionTransactionOptionsRequest;

    public SubscriptionTransactionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public SubscriptionTransactionRequest subscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public SubscriptionTransactionOptionsRequest options() {
        subscriptionTransactionOptionsRequest = new SubscriptionTransactionOptionsRequest(this);
        return subscriptionTransactionOptionsRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("transaction").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("amount", amount).
            addElement("subscriptionId", subscriptionId).
            addElement("options", subscriptionTransactionOptionsRequest).
            addElement("type", Type.SALE.toString().toLowerCase());
    }
}
