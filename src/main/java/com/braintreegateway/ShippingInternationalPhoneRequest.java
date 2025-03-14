package com.braintreegateway;

public class ShippingInternationalPhoneRequest extends AddressInternationalPhoneRequest {
    private PayPalPaymentResourceShippingRequest parent;
    private String countryCode;
    private String nationalNumber;

    public ShippingInternationalPhoneRequest(PayPalPaymentResourceShippingRequest parent) {
        super();
        this.parent = parent;
    }

    public PayPalPaymentResourceShippingRequest done() {
        return parent;
    }

    public ShippingInternationalPhoneRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public ShippingInternationalPhoneRequest nationalNumber(String nationalNumber) {
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