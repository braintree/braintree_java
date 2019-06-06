package com.braintreegateway;

import java.util.Map;
import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceDetailsPayerInfo {
    private String email;
    private String firstName;
    private String lastName;
    private String payerId;
    private String countryCode;
    // TODO coerce the address info to match the address object?
    // private Address billingAddress;
    // private Address shippingAddress;

    public PaymentMethodNonceDetailsPayerInfo(NodeWrapper node) {
        email = node.findString("email");
        firstName = node.findString("first-name");
        lastName = node.findString("last-name");
        payerId = node.findString("payer-id");
        countryCode = node.findString("country-code");
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
}
