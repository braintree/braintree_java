package com.braintreegateway;

public class PaymentMethodAddressRequest extends AddressRequest {

    private PaymentMethodRequest parent;
    protected Request optionsRequest;

    public PaymentMethodAddressRequest(PaymentMethodRequest parent) {
        this.parent = parent;
        this.tagName = "billingAddress";
    }

    @Override
    public PaymentMethodAddressRequest company(String company) {
        super.company(company);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest phoneNumber(String phoneNumber) {
        super.phoneNumber(phoneNumber);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        super.countryCodeAlpha3(countryCodeAlpha3);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        super.countryCodeNumeric(countryCodeNumeric);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest countryName(String countryName) {
        super.countryName(countryName);
        return this;
    }

    public PaymentMethodRequest done() {
        return parent;
    }

    @Override
    public PaymentMethodAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest firstName(String firstName) {
        super.firstName(firstName);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest lastName(String lastName) {
        super.lastName(lastName);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    public PaymentMethodAddressOptionsRequest options() {
        this.optionsRequest = new PaymentMethodAddressOptionsRequest(this);
        return (PaymentMethodAddressOptionsRequest) optionsRequest;
    }

    @Override
    public PaymentMethodAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        return super.buildRequest(root)
            .addElement("options", optionsRequest);
    }

    @Override
    public PaymentMethodAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public PaymentMethodAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    }
}
