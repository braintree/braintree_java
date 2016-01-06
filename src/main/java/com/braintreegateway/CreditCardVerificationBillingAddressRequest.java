package com.braintreegateway;

public class CreditCardVerificationBillingAddressRequest extends Request {
    private CreditCardVerificationCreditCardRequest parent;
    private String company;
    private String countryCodeAlpha2;
    private String countryCodeAlpha3;
    private String countryCodeNumeric;
    private String countryName;
    private String extendedAddress;
    private String firstName;
    private String lastName;
    private String locality;
    private String postalCode;
    private String region;
    private String streetAddress;

    public CreditCardVerificationBillingAddressRequest(CreditCardVerificationCreditCardRequest parent) {
        this.parent = parent;
    }

    public CreditCardVerificationBillingAddressRequest company(String company) {
        this.company = company;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        this.countryCodeAlpha2 = countryCodeAlpha2;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        this.countryCodeAlpha3 = countryCodeAlpha3;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest countryCodeNumeric(String countryCodeNumeric) {
        this.countryCodeNumeric = countryCodeNumeric;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest extendedAddress(String extendedAddress) {
        this.extendedAddress = extendedAddress;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest locality(String locality) {
        this.locality = locality;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest region(String region) {
        this.region = region;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public CreditCardVerificationCreditCardRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("billingAddress").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("company", company).
            addElement("countryCodeAlpha2", countryCodeAlpha2).
            addElement("countryCodeAlpha3", countryCodeAlpha3).
            addElement("countryCodeNumeric", countryCodeNumeric).
            addElement("countryName", countryName).
            addElement("extendedAddress", extendedAddress).
            addElement("firstName", firstName).
            addElement("lastName", lastName).
            addElement("locality", locality).
            addElement("postalCode", postalCode).
            addElement("region", region).
            addElement("streetAddress", streetAddress);
        return builder;
    }
}
