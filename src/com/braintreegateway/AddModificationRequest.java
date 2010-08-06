package com.braintreegateway;

import java.math.BigDecimal;

public class AddModificationRequest extends ModificationRequest {

    private String inheritedFromId;

    public AddModificationRequest(ModificationsRequest parent) {
        super(parent);
    }

    @Override
    public AddModificationRequest amount(BigDecimal amount) {
        super.amount(amount);
        return this;
    }

    public AddModificationRequest inheritedFromId(String inheritedFromId) {
        this.inheritedFromId = inheritedFromId;
        return this;
    }

    @Override
    public AddModificationRequest neverExpires(boolean neverExpires) {
        super.neverExpires(neverExpires);
        return this;
    }

    @Override
    public AddModificationRequest numberOfBillingCycles(Integer numberOfBillingCycles) {
        super.numberOfBillingCycles(numberOfBillingCycles);
        return this;
    }

    @Override
    public AddModificationRequest quantity(int quantity) {
        super.quantity(quantity);
        return this;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root).addElement("inheritedFromId", inheritedFromId);
    }
}
