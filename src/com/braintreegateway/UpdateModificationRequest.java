package com.braintreegateway;

import java.math.BigDecimal;

public class UpdateModificationRequest extends Request {

    private BigDecimal amount;
    private String existingId;
    private ModificationsRequest parent;
    private Integer quantity;

    public UpdateModificationRequest(ModificationsRequest parent) {
        this.parent = parent;
    }

    public UpdateModificationRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public UpdateModificationRequest existingId(String existingId) {
        this.existingId = existingId;
        return this;
    }

    public ModificationsRequest done() {
        return parent;
    }

    public UpdateModificationRequest quantity(int quantity) {
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
            addElement("existingId", existingId).
            addElement("quantity", quantity);
    }
}
