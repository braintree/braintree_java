package com.braintreegateway;

import java.math.BigDecimal;

public class ModificationRequest extends Request {

    private BigDecimal amount;
    private String existingId;
    private ModificationsRequest parent;

    public ModificationRequest(ModificationsRequest parent) {
        this.parent = parent;
    }

    public ModificationRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ModificationRequest existingId(String existingId) {
        this.existingId = existingId;
        return this;
    }

    public ModificationsRequest done() {
        return parent;
    }

    @Override
    public String toQueryString() {
        return "not implemented";
    }

    @Override
    public String toQueryString(String root) {
        return "not implemented";
    }

    @Override
    public String toXML() {
        return RequestBuilder.wrapInXMLTag("update", buildRequest("modification").toXML(), "array");
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("amount", amount).
            addElement("existingId", existingId);
    }
}
