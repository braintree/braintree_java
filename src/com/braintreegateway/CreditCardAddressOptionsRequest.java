package com.braintreegateway;

import com.braintreegateway.util.QueryString;

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
    public String toQueryString(String root) {
        return new QueryString().
            append(parentBracketChildString(root, "update_existing"), updateExisting).
            toString();
    }

    @Override
    public String toQueryString() {
        return toQueryString("options");
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
