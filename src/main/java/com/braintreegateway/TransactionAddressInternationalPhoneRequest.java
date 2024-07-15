package com.braintreegateway;

public class TransactionAddressInternationalPhoneRequest extends AddressInternationalPhoneRequest {
    private TransactionAddressRequest parent;
    private String countryCode;
    private String nationalNumber;

    public TransactionAddressInternationalPhoneRequest(TransactionAddressRequest parent) {
        super();
        this.parent = parent;
    }

    public TransactionAddressRequest done() {
        return parent;
    }

    public TransactionAddressInternationalPhoneRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public TransactionAddressInternationalPhoneRequest nationalNumber(String nationalNumber) {
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
