package com.braintreegateway;

public class NestedAddressRequest<T> extends AddressRequest {

    private T parent;

    public NestedAddressRequest(T parent) {
        this.parent = parent;
    }

    public NestedAddressRequest<T> company(String company) {
        super.company(company);
        return this;
    }

    public NestedAddressRequest<T> countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public NestedAddressRequest<T> extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    public NestedAddressRequest<T> firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    public NestedAddressRequest<T> lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    public NestedAddressRequest<T> locality(String locality) {
        super.locality(locality);
        return this;
    }

    public NestedAddressRequest<T> postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    public NestedAddressRequest<T> region(String region) {
        super.region(region);
        return this;
    }

    public NestedAddressRequest<T> streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }

    public T done() {
        return parent;
    }
}
