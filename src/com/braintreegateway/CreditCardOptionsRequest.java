package com.braintreegateway;

import com.braintreegateway.util.QueryString;

public class CreditCardOptionsRequest extends Request {
    private CreditCardRequest parent;
    private String verificationMerchantAccountId;
    private boolean verifyCard;
    private boolean makeDefault;

    public CreditCardOptionsRequest(CreditCardRequest parent) {
        this.parent = parent;
    }

    public CreditCardRequest done() {
        return parent;
    }

    public CreditCardOptionsRequest verificationMerchantAccountId(String verificationMerchantAccountId) {
        this.verificationMerchantAccountId = verificationMerchantAccountId;
        return this;
    }

    public CreditCardOptionsRequest verifyCard(boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<options>");
        builder.append(buildXMLElement("verifyCard", verifyCard));
        builder.append(buildXMLElement("verificationMerchantAccountId", verificationMerchantAccountId));
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
            append(parentBracketChildString(root, "verification_merchant_account_id"), verificationMerchantAccountId).
            toString();
    }

    public String toQueryString() {
        return toQueryString("options");
    }

    public CreditCardOptionsRequest makeDefault(boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }
}
