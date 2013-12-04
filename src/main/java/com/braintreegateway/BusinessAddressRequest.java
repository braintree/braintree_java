package com.braintreegateway;

public class BusinessAddressRequest extends AddressRequest {
    private BusinessRequest parent;

    public BusinessAddressRequest(BusinessRequest parent) {
        super();
        this.parent = parent;
    }

    public BusinessRequest done() {
        return this.parent;
    }

    @Override
    public BusinessAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public BusinessAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public BusinessAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public BusinessAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public BusinessAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    @Override
    public BusinessAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public BusinessAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public BusinessAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public BusinessAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public BusinessAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public BusinessAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public BusinessAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
}
