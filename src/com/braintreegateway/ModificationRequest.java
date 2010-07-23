package com.braintreegateway;

import java.math.BigDecimal;

public class ModificationRequest extends Request {

    private BigDecimal amount;
    private ModificationsRequest parent;
    private Integer quantity;

    public ModificationRequest(ModificationsRequest parent) {
        this.parent = parent;
    }

    public ModificationRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ModificationsRequest done() {
        return parent;
    }

    public ModificationRequest quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("modification").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("amount", amount).
            addElement("quantity", quantity);
    }
}
