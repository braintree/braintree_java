package com.braintreegateway;

public class CreditCardAddressOptionsRequest extends Request {

    private CreditCardAddressRequest parent;
    private boolean updateExisting;

    public CreditCardAddressOptionsRequest(CreditCardAddressRequest parent) {
        this.parent = parent;
    }

    public CreditCardAddressRequest done() {
        return parent;
    }

    public String toQueryString() {
        return toQueryString("options");
    }

    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }
    
    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }
    
    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("updateExisting", updateExisting);
    }
    
    
    public CreditCardAddressOptionsRequest updateExisting(boolean updateExisting) {
        this.updateExisting = updateExisting;
        return this;
    }

}
