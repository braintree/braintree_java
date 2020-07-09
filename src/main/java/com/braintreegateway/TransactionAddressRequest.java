package com.braintreegateway;

public class TransactionAddressRequest extends AddressRequest {
    public enum ShippingMethod {
        SAME_DAY("same_day"),
        NEXT_DAY("next_day"),
        PRIORITY("priority"),
        GROUND("ground"),
        ELECTRONIC("electronic"),
        SHIP_TO_STORE("ship_to_store");

        private final String name;

        ShippingMethod(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private TransactionRequest parent;
    private ShippingMethod shippingMethod;

    public TransactionAddressRequest(TransactionRequest parent, String tagName) {
        this.parent = parent;
        this.tagName = tagName;
    }

    @Override
    public TransactionAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public TransactionAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    @Override
    public TransactionAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public TransactionAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public TransactionAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public TransactionAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public TransactionAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public TransactionAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public TransactionAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public TransactionAddressRequest phoneNumber(String phoneNumber) {
        super.phoneNumber(phoneNumber);
        return this;
    }

    @Override
    public TransactionAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public TransactionAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    public TransactionAddressRequest shippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
        return this;
    }

    @Override
    public TransactionAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        RequestBuilder requestBuilder = super.buildRequest(root);
        if (shippingMethod != null) {
            requestBuilder = requestBuilder.addElement("shippingMethod", shippingMethod);
        }
        return requestBuilder;
    }
}
