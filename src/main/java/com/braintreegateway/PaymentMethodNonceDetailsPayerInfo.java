package com.braintreegateway;

import java.util.Map;
import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceDetailsPayerInfo {
    private String email;
    private String firstName;
    private String lastName;
    private String payerId;
    private String countryCode;
    private Address billingAddress;
    private Address shippingAddress;

    public PaymentMethodNonceDetailsPayerInfo(NodeWrapper node) {
        email = node.findString("email");
        firstName = node.findString("first-name");
        lastName = node.findString("last-name");
        payerId = node.findString("payer-id");
        countryCode = node.findString("country-code");

        NodeWrapper billingAddressNode = node.findFirst("billing-address");

        if (billingAddressNode != null && !billingAddressNode.isBlank()) {
            billingAddress = new Address(billingAddressNode);
        }

        NodeWrapper shippingAddressNode = node.findFirst("shipping-address");

        if (shippingAddressNode != null && !shippingAddressNode.isBlank()) {
            shippingAddress = new Address(shippingAddressNode);
        }
    }

    public PaymentMethodNonceDetailsPayerInfo(Map<String, String> map) {
        email = map.get("email");
        firstName = map.get("first-name");
        lastName = map.get("last-name");
        payerId = map.get("payer-id");
        countryCode = map.get("country-code");
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }
}
