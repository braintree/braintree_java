package com.braintreegateway;


public class CreditCardAddressRequest extends AddressRequest {

    private CreditCardRequest parent;
    protected Request optionsRequest;

    public CreditCardAddressRequest(CreditCardRequest parent) {
        this.parent = parent;
        this.tagName = "billingAddress";
    }

    @Override
    public CreditCardAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public CreditCardAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public CreditCardAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public CreditCardAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }
    
    @Override
    public CreditCardAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public CreditCardRequest done() {
        return parent;
    }

    @Override
    public CreditCardAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public CreditCardAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public CreditCardAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public CreditCardAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    public CreditCardAddressOptionsRequest options() {
        this.optionsRequest = new CreditCardAddressOptionsRequest(this);
        return (CreditCardAddressOptionsRequest) optionsRequest;
    }

    @Override
    public CreditCardAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }
    
    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root).
            addElement("options", optionsRequest);
    }

    @Override
    public CreditCardAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public CreditCardAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
}
