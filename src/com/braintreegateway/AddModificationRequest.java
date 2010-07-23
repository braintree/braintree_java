package com.braintreegateway;

import java.math.BigDecimal;

public class AddModificationRequest extends ModificationRequest {

    private String id;

    public AddModificationRequest(ModificationsRequest parent) {
        super(parent);
    }

    @Override
    public AddModificationRequest amount(BigDecimal amount) {
        super.amount(amount);
        return this;
    }

    public AddModificationRequest id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public AddModificationRequest quantity(int quantity) {
        super.quantity(quantity);
        return this;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root).addElement("id", id);
    }
}
