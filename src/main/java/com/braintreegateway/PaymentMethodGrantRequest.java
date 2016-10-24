package com.braintreegateway;

public class PaymentMethodGrantRequest extends Request {
    private String sharedPaymentMethodToken;
    private boolean allowVaulting;
    private boolean includeBillingPostalCode;
    private String revokeAfter;

    public PaymentMethodGrantRequest() {
    }

    protected PaymentMethodGrantRequest sharedPaymentMethodToken(String sharedPaymentMethodToken) {
        this.sharedPaymentMethodToken = sharedPaymentMethodToken;
        return this;
    }

    public PaymentMethodGrantRequest  allowVaulting(boolean allowVaulting) {
        this.allowVaulting = allowVaulting;
        return this;
    }

    public PaymentMethodGrantRequest includeBillingPostalCode(boolean includeBillingPostalCode) {
        this.includeBillingPostalCode = includeBillingPostalCode;
        return this;
    }

    public PaymentMethodGrantRequest revokeAfter(String revokeAfter) {
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


