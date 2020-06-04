package com.braintreegateway;

import java.math.BigDecimal;

public class AuthenticationInsightOptionsRequest extends Request {
    private PaymentMethodNonceRequest parent;
    private BigDecimal amount;

    public AuthenticationInsightOptionsRequest(PaymentMethodNonceRequest parent) {
        this.parent = parent;
    }

    public AuthenticationInsightOptionsRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PaymentMethodNonceRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("authenticationInsightOptions").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("AuthenticationInsightOptions");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).addElement("amount", amount);
    }
}
