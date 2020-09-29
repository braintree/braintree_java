package com.braintreegateway;

public class MerchantAccountCreateForCurrencyRequest extends Request {

    private String currency;
    private String id;

    public MerchantAccountCreateForCurrencyRequest currency(String currency) {
        this.currency = currency;
        return this;
    }

    public MerchantAccountCreateForCurrencyRequest id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("merchant_account").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
                .addElement("currency", currency)
                .addElement("id", id);
    }

}
