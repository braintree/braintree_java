package com.braintreegateway;

import com.braintreegateway.MerchantAccount.FundingDestination;

public class FundingRequest extends Request {
    private FundingDestination destination;
    private String email;
    private String mobilePhone;
    private String routingNumber;
    private String accountNumber;
    private String descriptor;
    private MerchantAccountRequest parent;

    public FundingRequest(MerchantAccountRequest parent) {
        this.parent = parent;
    }

    public FundingRequest destination(FundingDestination destination) {
        this.destination = destination;
        return this;
    }

    public FundingRequest email(String email) {
        this.email = email;
        return this;
    }

    public FundingRequest mobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public FundingRequest routingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
        return this;
    }

    public FundingRequest accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public FundingRequest descriptor(String descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public MerchantAccountRequest done() {
        return this.parent;
    }

    @Override
    public String toQueryString() {
        return toQueryString("funding");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("funding").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
                .addElement("destination", destination)
                .addElement("email", email)
                .addElement("mobilePhone", mobilePhone)
                .addElement("routingNumber", routingNumber)
                .addElement("accountNumber", accountNumber)
                .addElement("descriptor", descriptor);
    }
}
