package com.braintreegateway;

import com.braintreegateway.util.QueryString;

public class OptionsRequest extends Request {
    private CreditCardRequest parent;
    private boolean verifyCard;
    private boolean makeDefault;

    public OptionsRequest(CreditCardRequest parent) {
        this.parent = parent;
    }

    public CreditCardRequest done() {
        return parent;
    }

    public OptionsRequest verifyCard(boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<options>");
        builder.append(buildXMLElement("verifyCard", verifyCard));
        if (makeDefault) {
            builder.append(buildXMLElement("makeDefault", makeDefault));
        }
        builder.append("</options>");
        return builder.toString();
    }

    public String toQueryString(String root) {
        return new QueryString().
            append(parentBracketChildString(root, "make_default"), makeDefault).
            append(parentBracketChildString(root, "verify_card"), verifyCard).
            toString();
    }

    public String toQueryString() {
        return toQueryString("options");
    }

    public OptionsRequest makeDefault(boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }
}
