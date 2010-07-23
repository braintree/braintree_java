package com.braintreegateway;

import java.math.BigDecimal;

public class UpdateModificationRequest extends ModificationRequest {

    private String existingId;

    public UpdateModificationRequest(ModificationsRequest parent, String existingId) {
        super(parent);
        this.existingId = existingId;
    }

    @Override
    public UpdateModificationRequest amount(BigDecimal amount) {
        super.amount(amount);
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
