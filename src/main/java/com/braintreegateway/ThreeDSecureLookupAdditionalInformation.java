package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import com.braintreegateway.exceptions.UnexpectedException;
import com.fasterxml.jackson.jr.ob.JSON;

public class ThreeDSecureLookupAdditionalInformation {
    private ThreeDSecureLookupAddress billingAddress;
    private String email;

    public ThreeDSecureLookupAdditionalInformation() {
        this.billingAddress = new ThreeDSecureLookupAddress();
    }

    public ThreeDSecureLookupAdditionalInformation billingAddress(ThreeDSecureLookupAddress billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation email(String email) {
        this.email = email;
        return this;
    }

    public String toJSON() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        jsonMap.put("email", email);
        jsonMap.put("billingGivenName", billingAddress.getGivenName());
        jsonMap.put("billingSurname", billingAddress.getSurname());
        jsonMap.put("billingPhoneNumber", billingAddress.getPhoneNumber());
        jsonMap.put("billingCity", billingAddress.getLocality());
        jsonMap.put("billingCountryCode", billingAddress.getCountryCodeAlpha2());
        jsonMap.put("billingLine1", billingAddress.getStreetAddress());
        jsonMap.put("billingLine2", billingAddress.getExtendedAddress());
        jsonMap.put("billingPostalCode", billingAddress.getPostalCode());
        jsonMap.put("billingState", billingAddress.getRegion());

        while (jsonMap.values().remove(null));

        try {
            return JSON.std.asString(jsonMap);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }
}
