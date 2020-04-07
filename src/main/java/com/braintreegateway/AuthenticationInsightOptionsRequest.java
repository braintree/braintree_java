package com.braintreegateway;

import java.math.BigDecimal;

public class AuthenticationInsightOptionsRequest extends Request {
    private PaymentMethodNonceRequest parent;
    private BigDecimal amount;
    private Boolean recurringCustomerConsent;
    private BigDecimal recurringMaxAmount;

    public AuthenticationInsightOptionsRequest(PaymentMethodNonceRequest parent) {
        this.parent = parent;
    }

    public AuthenticationInsightOptionsRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AuthenticationInsightOptionsRequest recurringCustomerConsent(Boolean recurringCustomerConsent) {
        this.recurringCustomerConsent = recurringCustomerConsent;
        return this;
    }

    public AuthenticationInsightOptionsRequest recurringMaxAmount(BigDecimal recurringMaxAmount) {
        this.recurringMaxAmount = recurringMaxAmount;
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
        return new RequestBuilder(root).
            addElement("amount", amount).
            addElement("recurringCustomerConsent", recurringCustomerConsent).
            addElement("recurringMaxAmount", recurringMaxAmount);
    }
}
