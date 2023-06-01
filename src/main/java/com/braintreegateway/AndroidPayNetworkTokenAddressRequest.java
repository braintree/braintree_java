package com.braintreegateway;

public class AndroidPayNetworkTokenAddressRequest extends AddressRequest {

    private AndroidPayNetworkTokenRequest parent;

    public AndroidPayNetworkTokenAddressRequest(AndroidPayNetworkTokenRequest parent) {
        this.parent = parent;
        this.tagName = "billingAddress";
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }
    
    @Override
    public AndroidPayNetworkTokenAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public AndroidPayNetworkTokenRequest done() {
        return parent;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest phoneNumber(String phoneNumber) {
        super.phoneNumber(phoneNumber);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public AndroidPayNetworkTokenAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root);
    }
}
