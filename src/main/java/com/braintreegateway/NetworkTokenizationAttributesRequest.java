package com.braintreegateway;

public class NetworkTokenizationAttributesRequest extends Request {
    private String cryptogram;
    private String ecommerceIndicator;
    private String tokenRequestorId;
    private TransactionCreditCardRequest parent;

    public NetworkTokenizationAttributesRequest(TransactionCreditCardRequest parent) {
        this.parent = parent;
    }

    public NetworkTokenizationAttributesRequest cryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
        return this;
    }

    public NetworkTokenizationAttributesRequest ecommerceIndicator(String ecommerceIndicator) {
        this.ecommerceIndicator = ecommerceIndicator;
        return this;
    }

    public NetworkTokenizationAttributesRequest tokenRequestorId(String tokenRequestorId) {
        this.tokenRequestorId = tokenRequestorId;
        return this;
    }

    public TransactionCreditCardRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("networkTokenizationAttributes").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toQueryString() {
        return toQueryString("networkTokenizationAttributes");
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("cryptogram", cryptogram)
            .addElement("ecommerceIndicator", ecommerceIndicator)
            .addElement("tokenRequestorId", tokenRequestorId);
    }
}
