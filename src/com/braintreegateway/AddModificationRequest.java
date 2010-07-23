package com.braintreegateway;

import java.math.BigDecimal;

public class AddModificationRequest extends Request {

    private BigDecimal amount;
    private String id;
    private ModificationsRequest parent;
    private Integer quantity;

    public AddModificationRequest(ModificationsRequest parent) {
        this.parent = parent;
    }

    public AddModificationRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AddModificationRequest id(String id) {
        this.id = id;
        return this;
    }

    public ModificationsRequest done() {
        return parent;
    }

    public AddModificationRequest quantity(int quantity) {
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
            addElement("id", id).
            addElement("quantity", quantity);
    }
}
