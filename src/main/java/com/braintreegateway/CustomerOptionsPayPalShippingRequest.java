package com.braintreegateway;

public class CustomerOptionsPayPalShippingRequest extends AddressRequest {

    private CustomerOptionsPayPalRequest parent;

    public CustomerOptionsPayPalShippingRequest(CustomerOptionsPayPalRequest parent) {
        this.parent = parent;
        this.tagName = "shipping";
    }

    @Override
    public CustomerOptionsPayPalShippingRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public CustomerOptionsPayPalShippingRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }

    public CustomerOptionsPayPalRequest done() {
        return parent;
    }
}
