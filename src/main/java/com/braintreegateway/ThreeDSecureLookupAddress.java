package com.braintreegateway;

public class ThreeDSecureLookupAddress {
    private String countryCodeAlpha2;
    private String extendedAddress;
    private String givenName;
    private String locality;
    private String phoneNumber;
    private String postalCode;
    private String region;
    private String streetAddress;
    private String surname;

    public ThreeDSecureLookupAddress() {}

    public ThreeDSecureLookupAddress countryCodeAlpha2(String countryCodeAlpha2) {
        this.countryCodeAlpha2 = countryCodeAlpha2;
        return this;
    }

    public ThreeDSecureLookupAddress extendedAddress(String extendedAddress) {
        this.extendedAddress = extendedAddress;
        return this;
    }

    public ThreeDSecureLookupAddress givenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public ThreeDSecureLookupAddress locality(String locality) {
        this.locality = locality;
        return this;
    }

    public ThreeDSecureLookupAddress phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ThreeDSecureLookupAddress postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public ThreeDSecureLookupAddress region(String region) {
        this.region = region;
        return this;
    }

    public ThreeDSecureLookupAddress streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public ThreeDSecureLookupAddress surname(String surname) {
        this.surname = surname;
        return this;
    }


    public String getCountryCodeAlpha2() {
        return countryCodeAlpha2;
    }

    public String getExtendedAddress() {
        return extendedAddress;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getLocality() {
        return locality;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getRegion() {
        return region;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getSurname() {
        return surname;
    }
}
