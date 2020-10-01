package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link CreditCard CreditCards}.
 */
public class RiskDataCustomerRequest extends Request {
    private String customerBrowser;
    private String customerIP;
    private CustomerRequest parent;

    public RiskDataCustomerRequest() {
    }

    public RiskDataCustomerRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public CustomerRequest done() {
        return parent;
    }

    public RiskDataCustomerRequest customerBrowser(String browser) {
        this.customerBrowser = browser;
        return this;
    }

    public RiskDataCustomerRequest customerIP(String ip) {
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
        return new RequestBuilder(root)
            .addElement("customerBrowser", customerBrowser)
            .addElement("customerIP", customerIP);
    }
}
