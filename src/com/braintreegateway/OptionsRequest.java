package com.braintreegateway;

import com.braintreegateway.util.QueryString;

public class OptionsRequest extends Request {
    private CreditCardRequest parent;
    private String verifyCard;

    public OptionsRequest(CreditCardRequest parent) {
        this.parent = parent;
    }

    public CreditCardRequest done() {
        return parent;
    }

    public OptionsRequest verifyCard(String verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<options>");
        builder.append(buildXMLElement("verifyCard", verifyCard));
        builder.append("</options>");
        return builder.toString();
    }

    public String toQueryString(String root) {
        return new QueryString().
            append(parentBracketChildString(root, "verify_card"), verifyCard).
            toString();
    }

    public String toQueryString() {
        return toQueryString("options");
    }
}
