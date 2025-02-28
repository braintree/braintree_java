package com.braintreegateway;

public class PayPalPaymentResourceShippingRequest extends AddressRequest {

    private PayPalPaymentResourceRequest parent;
    private ShippingInternationalPhoneRequest internationalPhoneRequest;

    public PayPalPaymentResourceShippingRequest(PayPalPaymentResourceRequest parent) {
        this.parent = parent;
        this.tagName = "shipping";
    }

     @Override
    public PayPalPaymentResourceShippingRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public ShippingInternationalPhoneRequest internationalPhone() {
        internationalPhoneRequest = new ShippingInternationalPhoneRequest(this);
        return this.internationalPhoneRequest;
    }

    @Override
    public PayPalPaymentResourceShippingRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public PayPalPaymentResourceShippingRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }

    public PayPalPaymentResourceRequest done() {
        return parent;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        RequestBuilder requestBuilder = super.buildRequest(root);
        if (internationalPhoneRequest != null) {
            requestBuilder = requestBuilder.addElement("internationalPhone", internationalPhoneRequest);
        }
        return requestBuilder;
    }

}
