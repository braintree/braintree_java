package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class VenmoProfileData {
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public VenmoProfileData(NodeWrapper node) {
        this.username = node.findString("username");
        this.firstName = node.findString("first-name");
        this.lastName = node.findString("last-name");
        this.phoneNumber = node.findString("phone-number");
        this.email = node.findString("email");
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
 }
