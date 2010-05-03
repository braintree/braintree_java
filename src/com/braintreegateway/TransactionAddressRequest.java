package com.braintreegateway;

public class TransactionAddressRequest extends AddressRequest {

    private TransactionRequest parent;

    public TransactionAddressRequest(TransactionRequest parent, String tagName) {
        this.parent = parent;
        this.tagName = tagName;
    }

    public TransactionAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    public TransactionAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public TransactionAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    public TransactionAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    public TransactionAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    public TransactionAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    public TransactionAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    public TransactionAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    public TransactionAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }
}
