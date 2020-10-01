package com.braintreegateway;

import java.math.BigDecimal;

public class ModificationRequest extends Request {

    private BigDecimal amount;
    private Integer numberOfBillingCycles;
    private ModificationsRequest parent;
    private Integer quantity;
    private Boolean neverExpires;

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

    public ModificationRequest numberOfBillingCycles(Integer numberOfBillingCycles) {
        this.numberOfBillingCycles = numberOfBillingCycles;
        return this;
    }

    public ModificationRequest neverExpires(Boolean neverExpires) {
        this.neverExpires = neverExpires;
        return this;
    }

    public ModificationRequest quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("modification").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("amount", amount)
            .addElement("neverExpires", neverExpires)
            .addElement("numberOfBillingCycles", numberOfBillingCycles)
            .addElement("quantity", quantity);
    }
}
