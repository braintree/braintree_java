package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionOptionsAdyenRequest;
import com.braintreegateway.TransactionOptionsRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionOptionsAdyenRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        TransactionOptionsRequest parent = new TransactionOptionsRequest(new TransactionRequest());
        TransactionOptionsAdyenRequest request = new TransactionOptionsAdyenRequest(parent);

        TransactionOptionsRequest returned = request
                .overwriteBrand(true)
                .selectedBrand("visa")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<adyen>"), xml);
        assertTrue(xml.contains("<overwriteBrand>true</overwriteBrand>"), xml);
        assertTrue(xml.contains("<selectedBrand>visa</selectedBrand>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("adyen%5Boverwrite_brand%5D=true"), queryString);
        assertTrue(queryString.contains("adyen%5Bselected_brand%5D=visa"), queryString);
    }
}
