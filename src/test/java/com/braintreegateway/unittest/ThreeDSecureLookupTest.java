package com.braintreegateway.unittest;

import com.braintreegateway.ThreeDSecureLookup;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
