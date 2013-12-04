package com.braintreegateway;

public class IndividualAddressRequest extends AddressRequest {
    private IndividualRequest parent;

    public IndividualAddressRequest(IndividualRequest parent) {
        super();
        this.parent = parent;
    }

    public IndividualRequest done() {
        return this.parent;
    }

    @Override
    public IndividualAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public IndividualAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public IndividualAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public IndividualAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public IndividualAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    @Override
    public IndividualAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public IndividualAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public IndividualAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public IndividualAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public IndividualAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public IndividualAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public IndividualAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
}
