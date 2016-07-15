package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link CreditCard CreditCards}.
 */
public class RiskDataTransactionRequest extends Request {
    private String customerBrowser;
    private String customerIP;
    private TransactionRequest parent;

    public RiskDataTransactionRequest() {
    }

    public RiskDataTransactionRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionRequest done() {
        return parent;
    }

    public RiskDataTransactionRequest customerBrowser(String browser) {
        this.customerBrowser = browser;
        return this;
    }

    public RiskDataTransactionRequest customerIP(String ip) {
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
