package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionOptionsRequest;
import com.braintreegateway.TransactionOptionsVenmoRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionOptionsVenmoRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        TransactionOptionsRequest parent = new TransactionOptionsRequest(new TransactionRequest());
        TransactionOptionsVenmoRequest request = new TransactionOptionsVenmoRequest(parent);

        TransactionOptionsRequest returned = request.profileId("a-profile-id").done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<venmo>"), xml);
        assertTrue(xml.contains("<profile-id>a-profile-id</profile-id>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("venmo%5Bprofile_id%5D=a-profile-id"), queryString);
    }
}
