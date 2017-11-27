package com.braintreegateway;

public class PaymentMethodOptionsPayPalShippingRequest extends AddressRequest {

    private PaymentMethodOptionsPayPalRequest parent;

    public PaymentMethodOptionsPayPalShippingRequest(PaymentMethodOptionsPayPalRequest parent) {
        this.parent = parent;
        this.tagName = "shipping";
    }

    public PaymentMethodOptionsPayPalRequest done() {
        return parent;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public PaymentMethodOptionsPayPalShippingRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
}
