package com.braintreegateway;


public class AndroidPayCardAddressRequest extends AddressRequest {

    private AndroidPayCardRequest parent;

    public AndroidPayCardAddressRequest(AndroidPayCardRequest parent) {
        this.parent = parent;
        this.tagName = "billingAddress";
    }

    @Override
    public AndroidPayCardAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }
    
    @Override
    public AndroidPayCardAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public AndroidPayCardRequest done() {
        return parent;
    }

    @Override
    public AndroidPayCardAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest phoneNumber(String phoneNumber) {
        super.phoneNumber(phoneNumber);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public AndroidPayCardAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
    
    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root);
    }
}