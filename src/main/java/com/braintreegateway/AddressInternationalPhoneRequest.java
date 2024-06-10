package com.braintreegateway;

public class AddressInternationalPhoneRequest extends Request {
    private AddressRequest parent;
    private String countryCode;
    private String nationalNumber;

    public AddressInternationalPhoneRequest() {
        //left blank
    }

    public AddressInternationalPhoneRequest(AddressRequest parent) {
        this.parent = parent;
    }

    public AddressRequest done() {
        return parent;
    }

    public AddressInternationalPhoneRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public AddressInternationalPhoneRequest nationalNumber(String nationalNumber) {
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
