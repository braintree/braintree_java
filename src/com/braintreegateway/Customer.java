package com.braintreegateway;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.braintreegateway.util.NodeWrapper;

public class Customer {

    private Calendar createdAt;
    private Calendar updatedAt;
    private String company;
    private String email;
    private String fax;
    private String firstName;
    private String id;
    private String lastName;
    private String phone;
    private String website;
    private Map<String, String> customFields;
    private List<CreditCard> creditCards;
    private List<Address> addresses;

    public Customer(NodeWrapper node) {
        id = node.findString("id");
        firstName = node.findString("first-name");
        lastName = node.findString("last-name");
        company = node.findString("company");
        email = node.findString("email");
        fax = node.findString("fax");
        phone = node.findString("phone");
        website = node.findString("website");
        createdAt = node.findDateTime("created-at");
        updatedAt = node.findDateTime("updated-at");
        customFields = node.findMap("custom-field");
        creditCards = new ArrayList<CreditCard>();
        for (NodeWrapper creditCardResponse : node.findAll("credit-cards/credit-card")) {
            creditCards.add(new CreditCard(creditCardResponse));
        }
        addresses = new ArrayList<Address>();
        for (NodeWrapper addressResponse : node.findAll("addresses/address")) {
            addresses.add(new Address(addressResponse));
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public List<CreditCard> getCreditCards() {
        return Collections.unmodifiableList(creditCards);
    }
}
