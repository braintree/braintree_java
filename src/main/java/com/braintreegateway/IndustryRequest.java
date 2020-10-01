package com.braintreegateway;

public abstract class IndustryRequest extends Request {

    protected Transaction.IndustryType industryType;
    protected TransactionIndustryDataRequest data;

    public IndustryRequest() {
        super();
    }

    public IndustryRequest industryType(Transaction.IndustryType industryType) {
        this.industryType = industryType;
        return this;
    }

    public TransactionIndustryDataRequest data() {
        this.data = new TransactionIndustryDataRequest(this);
        return data;
    }

    @Override
    public String toXML() {
        return buildRequest("industry").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
                .addElement("industryType", industryType)
                .addElement("data", data);
    }
}
