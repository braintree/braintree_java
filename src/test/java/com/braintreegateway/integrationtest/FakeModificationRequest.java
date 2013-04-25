package com.braintreegateway.integrationtest;

import java.math.BigDecimal;

import com.braintreegateway.*;

public class FakeModificationRequest extends Request {
    private BigDecimal amount;
    private String description;
    private String id;
    private String kind;
    private String name;
    private Boolean neverExpires;
    private Integer numberOfBillingCycles;
    private String planId;

    public FakeModificationRequest() {
    }

    public FakeModificationRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public FakeModificationRequest kind(String kind) {
        this.kind = kind;
        return this;
    }

    public FakeModificationRequest name(String name) {
        this.name = name;
        return this;
    }

    public FakeModificationRequest description(String description) {
        this.description = description;
        return this;
    }

    public FakeModificationRequest id(String id) {
        this.id = id;
        return this;
    }

    public FakeModificationRequest numberOfBillingCycles(Integer numberOfBillingCycles) {
        this.numberOfBillingCycles = numberOfBillingCycles;
        return this;
    }

    public FakeModificationRequest neverExpires(Boolean neverExpires) {
        this.neverExpires = neverExpires;
        return this;
    }

    public FakeModificationRequest planId(String planId) {
        this.planId = planId;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("modification").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("amount", amount).
            addElement("description", description).
            addElement("id", id).
            addElement("kind", kind).
            addElement("name", name).
            addElement("neverExpires", neverExpires).
            addElement("numberOfBillingCycles", numberOfBillingCycles).
            addElement("planId", planId);
    }
}
