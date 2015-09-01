package com.braintreegateway;

public class TransactionOptionsAmexRewardsRequest extends Request {
    private TransactionOptionsRequest parent;
    private String points;
    private String currencyAmount;
    private String currencyIsoCode;
    private String requestId;

    public TransactionOptionsAmexRewardsRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsAmexRewardsRequest points(String points) {
        this.points = points;
        return this;
    }

    public TransactionOptionsAmexRewardsRequest currencyAmount(String currencyAmount) {
        this.currencyAmount = currencyAmount;
        return this;
    }

    public TransactionOptionsAmexRewardsRequest currencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
        return this;
    }

    public TransactionOptionsAmexRewardsRequest requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("amex-rewards").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("amex-rewards");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("request-id", requestId).
            addElement("points", points).
            addElement("currency-amount", currencyAmount).
            addElement("currency-iso-code", currencyIsoCode);
    }
}
