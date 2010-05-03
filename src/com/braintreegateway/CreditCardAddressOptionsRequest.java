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

    @Override
    public String toQueryString(String parent) {
        return "";
    }

    @Override
    public String toQueryString() {
        return "";
    }

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<options>");
        builder.append(buildXMLElement("updateExisting", updateExisting));
        builder.append("</options>");
        return builder.toString();
    }

    public CreditCardAddressOptionsRequest updateExisting(boolean updateExisting) {
        this.updateExisting = updateExisting;
        return this;
    }

}
