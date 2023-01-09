package com.braintreegateway;

public class SepaDirectDebitAccountRequest extends Request {
    private String token;
    private SepaDirectDebitAccountOptionsRequest optionsRequest;

    public SepaDirectDebitAccountRequest token(String token) {
        this.token = token;
        return this;
    }

    public SepaDirectDebitAccountOptionsRequest options() {
        this.optionsRequest = new SepaDirectDebitAccountOptionsRequest(this);
        return optionsRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("sepaDebitAccount").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("options", optionsRequest)
            .addElement("token", token);
    }
}
