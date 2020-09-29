package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link Address Addresses}.
 */
public class AddressRequest extends Request {
    private String countryCodeAlpha2;
    private String countryCodeAlpha3;
    private String countryCodeNumeric;
    private String countryName;
    private String extendedAddress;
    private String firstName;
    private String lastName;
    private String locality;
    private String phoneNumber;
    private String postalCode;
    private String region;
    private String streetAddress;
    private String company;
    protected String tagName;

    public AddressRequest() {
        this.tagName = "address";
    }

    public AddressRequest company(String company) {
        this.company = company;
        return this;
    }
    
    public AddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        this.countryCodeAlpha2 = countryCodeAlpha2;
        return this;
    }
    
    public AddressRequest countryCodeAlpha3(String countryCodeAlpha3) {
        this.countryCodeAlpha3 = countryCodeAlpha3;
        return this;
    }
    
    public AddressRequest countryCodeNumeric(String countryCodeNumeric) {
        this.countryCodeNumeric = countryCodeNumeric;
        return this;
    }

    public AddressRequest countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public AddressRequest extendedAddress(String extendedAddress) {
        this.extendedAddress = extendedAddress;
        return this;
    }

    public AddressRequest firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AddressRequest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AddressRequest locality(String locality) {
        this.locality = locality;
        return this;
    }

    public AddressRequest phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public AddressRequest postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public AddressRequest region(String region) {
        this.region = region;
        return this;
    }

    public AddressRequest streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    @Override
    public String toQueryString() {
        return toQueryString(this.tagName);
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }
    
    @Override
    public String toXML() {
        return buildRequest(this.tagName).toXML();
    }
    
    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("firstName", firstName)
            .addElement("lastName", lastName)
            .addElement("company", company)
            .addElement("countryName", countryName)
            .addElement("countryCodeAlpha2", countryCodeAlpha2)
            .addElement("countryCodeAlpha3", countryCodeAlpha3)
            .addElement("countryCodeNumeric", countryCodeNumeric)
            .addElement("extendedAddress", extendedAddress)
            .addElement("locality", locality)
            .addElement("phoneNumber", phoneNumber)
            .addElement("postalCode", postalCode)
            .addElement("region", region)
            .addElement("streetAddress", streetAddress);
    }
}
