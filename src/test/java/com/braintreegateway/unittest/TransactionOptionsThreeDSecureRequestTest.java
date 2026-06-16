package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionOptionsRequest;
import com.braintreegateway.TransactionOptionsThreeDSecureRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionOptionsThreeDSecureRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        TransactionOptionsRequest parent = new TransactionOptionsRequest(new TransactionRequest());
        TransactionOptionsThreeDSecureRequest request = new TransactionOptionsThreeDSecureRequest(parent);

        TransactionOptionsRequest returned = request.required(true).done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<three-d-secure>"), xml);
        assertTrue(xml.contains("<required>true</required>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("three_d_secure%5Brequired%5D=true"), queryString);
    }
}
