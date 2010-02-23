package com.braintreegateway;

import java.util.Calendar;

import com.braintreegateway.util.NodeWrapper;

public class Address {

    private String company;
    private String countryName;
    private Calendar createdAt;
    private String customerId;
    private String extendedAddress;
    private String firstName;
    private String id;
    private String lastName;
    private String locality;
    private String postalCode;
    private String region;
    private String streetAddress;
    private Calendar updatedAt;

    public Address(NodeWrapper node) {
        company = node.findString("company");
        countryName = node.findString("country-name");
        createdAt = node.findDateTime("created-at");
        customerId = node.findString("customer-id");
        extendedAddress = node.findString("extended-address");
        firstName = node.findString("first-name");
        id = node.findString("id");
        lastName = node.findString("last-name");
        locality = node.findString("locality");
        postalCode = node.findString("postal-code");
        region = node.findString("region");
        streetAddress = node.findString("street-address");
        updatedAt = node.findDateTime("updated-at");
    }

    public String getCompany() {
        return company;
    }

    public String getCountryName() {
        return countryName;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getExtendedAddress() {
        return extendedAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getRegion() {
        return region;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }
}
