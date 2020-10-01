package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link CreditCard CreditCards}.
 */
public class RiskDataTransactionRequest extends Request {
    private String customerBrowser;
    private String customerDeviceId;
    private String customerIP;
    private String customerLocationZip;
    private Integer customerTenure;
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

    public RiskDataTransactionRequest customerDeviceId(String deviceId) {
        this.customerDeviceId = deviceId;
        return this;
    }

    public RiskDataTransactionRequest customerIP(String ip) {
        this.customerIP = ip;
        return this;
    }

    public RiskDataTransactionRequest customerLocationZip(String locationZip) {
        this.customerLocationZip = locationZip;
        return this;
    }

    public RiskDataTransactionRequest customerTenure(Integer tenure) {
        this.customerTenure = tenure;
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
            .addElement("customerDeviceId", customerDeviceId)
            .addElement("customerIP", customerIP)
            .addElement("customerLocationZip", customerLocationZip)
            .addElement("customerTenure", customerTenure);
    }
}
