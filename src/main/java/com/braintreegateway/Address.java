package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

/**
 * An address can belong to:
 * <ul>
 * <li>a CreditCard as the billing address
 * <li>a Customer as an address
 * <li>a Transaction as a billing or shipping address
 * </ul>
 *
 */
public class Address {

    private String company;
    private String countryCodeAlpha2;
    private String countryCodeAlpha3;
    private String countryCodeNumeric;
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
    private String recipientName; // only for PayPal PayerInfo object
    private String streetAddress;
    private Calendar updatedAt;

    public Address(NodeWrapper node) {
        company = node.findString("company");
        countryCodeAlpha2 = node.findString("country-code-alpha2");
        countryCodeAlpha3 = node.findString("country-code-alpha3");
        countryCodeNumeric = node.findString("country-code-numeric");
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

        // PayPal PayerInfo aliases
        recipientName = node.findString("recipient-name");
        if (streetAddress == null) {
            streetAddress = node.findString("line1");
        }
        if (locality == null) {
            locality = node.findString("city");
        }
        if (region == null) {
            region = node.findString("state");
        }
        if (countryCodeAlpha2 == null) {
           countryCodeAlpha2 = node.findString("country-code");
        }
    }

    public String getCompany() {
        return company;
    }

    public String getCountryCodeAlpha2() {
        return countryCodeAlpha2;
    }

    public String getCountryCodeAlpha3() {
        return countryCodeAlpha3;
    }

    public String getCountryCodeNumeric() {
        return countryCodeNumeric;
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

    // Alias methods for PayPal PayerInfo addresses
    public String getRecipientName() {
        if (recipientName != null) {
            return recipientName;
        }

        return getFirstName() + " " + getLastName();
    }
}
