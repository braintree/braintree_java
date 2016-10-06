package com.braintreegateway;

public class PaymentMethodGrantOptionsRequest extends Request {
    private String sharedPaymentMethodToken;
    private String allowVaulting;
    private String includeBillingPostalCode;
    private String revokeAfter;

    public PaymentMethodGrantOptionsRequest() {
    }

    protected PaymentMethodGrantOptionsRequest sharedPaymentMethodToken(String sharedPaymentMethodToken) {
        this.sharedPaymentMethodToken = sharedPaymentMethodToken;
        return this;
    }

    public PaymentMethodGrantOptionsRequest  allowVaulting(String allowVaulting) {
        this.allowVaulting = allowVaulting;
        return this;
    }

    public PaymentMethodGrantOptionsRequest includeBillingPostalCode(String includeBillingPostalCode) {
        this.includeBillingPostalCode = includeBillingPostalCode;
        return this;
    }

    public PaymentMethodGrantOptionsRequest revokeAfter(String revokeAfter) {
        this.revokeAfter = revokeAfter;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("payment-method").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("shared-payment-method-token", sharedPaymentMethodToken).
            addElement("allow-vaulting", allowVaulting).
            addElement("include-billing-postal-code", includeBillingPostalCode).
            addElement("revoke-after", revokeAfter);

        return builder;
    }
}


