package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class VenmoProfileData {
    private Address billingAddress;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address shippingAddress;
    private String username;

    public VenmoProfileData(NodeWrapper node) {
        this.username = node.findString("username");
        this.firstName = node.findString("first-name");
        this.lastName = node.findString("last-name");
        this.phoneNumber = node.findString("phone-number");
        this.email = node.findString("email");

        NodeWrapper billingAddress = node.findFirst("billing-address");
        if (billingAddress != null) {
            this.billingAddress = new Address(billingAddress);
        }

        NodeWrapper shippingAddress = node.findFirst("shipping-address");
        if (shippingAddress != null) {
            this.shippingAddress = new Address(shippingAddress);
        }
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

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }
 }
