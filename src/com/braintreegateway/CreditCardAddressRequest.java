package com.braintreegateway;

public class CreditCardAddressRequest extends AddressRequest {

    private CreditCardRequest parent;
    private CreditCardAddressOptionsRequest optionsRequest;

    public CreditCardAddressRequest(CreditCardRequest parent) {
        this.parent = parent;
        this.tagName = "billingAddress";
    }

    public CreditCardAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    public CreditCardAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public CreditCardRequest done() {
        return parent;
    }

    public CreditCardAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    public CreditCardAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    public CreditCardAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    public CreditCardAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    public CreditCardAddressOptionsRequest options() {
        this.optionsRequest = new CreditCardAddressOptionsRequest(this);
        return optionsRequest;
    }

    @Override
    protected String optionsXML() {
        return buildXMLElement(optionsRequest);
    }

    public CreditCardAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    public CreditCardAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    public CreditCardAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
}
