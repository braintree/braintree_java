package com.braintreegateway;

public class ApplicantDetailsAddressRequest extends AddressRequest {
    private ApplicantDetailsRequest parent;

    public ApplicantDetailsAddressRequest(ApplicantDetailsRequest parent) {
        super();
        this.parent = parent;
    }

    public ApplicantDetailsRequest done() {
        return this.parent;
    }

    @Override
    public ApplicantDetailsAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public ApplicantDetailsAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
}
