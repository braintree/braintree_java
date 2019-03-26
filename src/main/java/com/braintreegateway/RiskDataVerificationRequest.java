package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link CreditCardVerification CreditCardVerifications}.
 */
public class RiskDataVerificationRequest extends Request {
    private String customerBrowser;
    private String customerIP;
    private CreditCardVerificationRequest parent;

    public RiskDataVerificationRequest() {
    }

    public RiskDataVerificationRequest(CreditCardVerificationRequest parent) {
        this.parent = parent;
    }

    public CreditCardVerificationRequest done() {
        return parent;
    }

    public RiskDataVerificationRequest customerBrowser(String browser) {
        this.customerBrowser = browser;
        return this;
    }

    public RiskDataVerificationRequest customerIP(String ip) {
        this.customerIP = ip;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("riskData").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("riskData");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("customerBrowser", customerBrowser).
            addElement("customerIP", customerIP);
    }
}
