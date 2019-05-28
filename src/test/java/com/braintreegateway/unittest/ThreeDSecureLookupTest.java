package com.braintreegateway.unittest;

import com.braintreegateway.ThreeDSecureLookup;
import com.braintreegateway.ThreeDSecureLookupRequest;
import com.braintreegateway.exceptions.BraintreeException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ThreeDSecureLookupTest {

    @Test
    public void setsPropertiesFromHash() {
        Map<String, String> lookupResponse = new HashMap<String, String>();
        lookupResponse.put("acsUrl", "https://braintreepayments.com");
        lookupResponse.put("threeDSecureVersion", "2.0");
        lookupResponse.put("transactionId", "123-txn-id");
        lookupResponse.put("unused", "value");

        ThreeDSecureLookup lookup = new ThreeDSecureLookup(lookupResponse);

        assertEquals("https://braintreepayments.com", lookup.getAcsUrl());
        assertEquals("2.0", lookup.getThreeDSecureVersion());
        assertEquals("123-txn-id", lookup.getTransactionId());
    }
}
