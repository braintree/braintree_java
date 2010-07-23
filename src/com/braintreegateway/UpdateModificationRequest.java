package com.braintreegateway;

import java.math.BigDecimal;

public class UpdateModificationRequest extends ModificationRequest {

    private String existingId;

    public UpdateModificationRequest(ModificationsRequest parent) {
        super(parent);
    }

    @Override
    public UpdateModificationRequest amount(BigDecimal amount) {
        super.amount(amount);
        return this;
    }

    public UpdateModificationRequest existingId(String existingId) {
        this.existingId = existingId;
        return this;
    }

    @Override
    public UpdateModificationRequest quantity(int quantity) {
        super.quantity(quantity);
        return this;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root).addElement("existingId", existingId);
    }
}
