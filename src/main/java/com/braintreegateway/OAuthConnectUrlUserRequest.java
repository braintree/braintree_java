package com.braintreegateway;

public class OAuthConnectUrlUserRequest extends Request {

    private OAuthConnectUrlRequest parentRequest;
    private String country;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String dobYear;
    private String dobMonth;
    private String dobDay;
    private String streetAddress;
    private String locality;
    private String region;
    private String postalCode;

    public OAuthConnectUrlUserRequest(OAuthConnectUrlRequest parent) {
        this.parentRequest = parent;
    }

    public OAuthConnectUrlUserRequest country(String country) {
        this.country = country;
        return this;
    }

    public OAuthConnectUrlUserRequest email(String email) {
        this.email = email;
        return this;
    }

    public OAuthConnectUrlUserRequest firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public OAuthConnectUrlUserRequest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public OAuthConnectUrlUserRequest phone(String phone) {
        this.phone = phone;
        return this;
    }

    public OAuthConnectUrlUserRequest dobYear(String dobYear) {
        this.dobYear = dobYear;
        return this;
    }

    public OAuthConnectUrlUserRequest dobMonth(String dobMonth) {
        this.dobMonth = dobMonth;
        return this;
    }

    public OAuthConnectUrlUserRequest dobDay(String dobDay) {
        this.dobDay = dobDay;
        return this;
    }

    public OAuthConnectUrlUserRequest streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public OAuthConnectUrlUserRequest locality(String locality) {
        this.locality = locality;
        return this;
    }

    public OAuthConnectUrlUserRequest region(String region) {
        this.region = region;
        return this;
    }

    public OAuthConnectUrlUserRequest postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    @Override
    public String toQueryString(String root) {
        RequestBuilder builder = new RequestBuilder("user").
            addElement("country", country).
            addElement("email", email).
            addElement("firstName", firstName).
            addElement("lastName", lastName).
            addElement("phone", phone).
            addElement("dobYear", dobYear).
            addElement("dobMonth", dobMonth).
            addElement("dobDay", dobDay).
            addElement("streetAddress", streetAddress).
            addElement("locality", locality).
            addElement("region", region).
            addElement("postalCode", postalCode);

        return builder.toQueryString();
    }

    public OAuthConnectUrlRequest done() {
        return parentRequest;
    }
}
