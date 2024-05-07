package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class InternationalPhone {
    private String countryCode;
    private String nationalNumber;

    public InternationalPhone(NodeWrapper node) {
        countryCode = node.findString("countryCode");
        nationalNumber = node.findString("nationalNumber");
    }

    public InternationalPhone(String countryCode, String nationalNumber) {
        this.countryCode = countryCode;
        this.nationalNumber = nationalNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }
}
