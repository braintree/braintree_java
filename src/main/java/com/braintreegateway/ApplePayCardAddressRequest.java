package com.braintreegateway;


public class ApplePayCardAddressRequest extends AddressRequest {

    private ApplePayCardRequest parent;

    public ApplePayCardAddressRequest(ApplePayCardRequest parent) {
        this.parent = parent;
        this.tagName = "billingAddress";
    }

    @Override
    public ApplePayCardAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }
    
    @Override
    public ApplePayCardAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public ApplePayCardRequest done() {
        return parent;
    }

    @Override
    public ApplePayCardAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest phoneNumber(String phoneNumber) {
        super.phoneNumber(phoneNumber);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public ApplePayCardAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
    
    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root);
    }
}