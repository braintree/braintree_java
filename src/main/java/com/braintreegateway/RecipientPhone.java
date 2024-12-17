package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class RecipientPhone {
    private String countryCode;
    private String nationalNumber;

    public RecipientPhone(NodeWrapper node){
        countryCode = node.findString("country-code"); 
        nationalNumber = node.findString("national-number");
    }

    public String getCountryCode(){
        return countryCode; 
    }

    public String getNationalNumber(){
        return nationalNumber;
    }
}
