package com.braintreegateway;

public class CustomerInternationalPhoneRequest extends Request {
    private CustomerRequest parent;
    private String countryCode;
    private String nationalNumber;

    public CustomerInternationalPhoneRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public CustomerRequest done() {
        return parent;
    }

    public CustomerInternationalPhoneRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public CustomerInternationalPhoneRequest nationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("internationalPhone").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("countryCode", countryCode)
            .addElement("nationalNumber", nationalNumber);
    }
}
