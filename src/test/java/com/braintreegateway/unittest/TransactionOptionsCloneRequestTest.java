package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionCloneRequest;
import com.braintreegateway.TransactionOptionsCloneRequest;

public class TransactionOptionsCloneRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        TransactionCloneRequest parent = new TransactionCloneRequest();
        TransactionOptionsCloneRequest request = new TransactionOptionsCloneRequest(parent);

        TransactionCloneRequest returned = request.submitForSettlement(true).done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<submitForSettlement>true</submitForSettlement>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("options%5Bsubmit_for_settlement%5D=true"), queryString);
    }
}
