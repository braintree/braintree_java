package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.InstallmentRequest;
import com.braintreegateway.TransactionRequest;

public class InstallmentRequestTest {

    @Test
    public void buildsXmlAndChainsBackToParent() {
        TransactionRequest parent = new TransactionRequest();
        InstallmentRequest request = new InstallmentRequest(parent);

        TransactionRequest returned = request.count(3).done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<installments>"), xml);
        assertTrue(xml.contains("<count>3</count>"), xml);
    }

    @Test
    public void toQueryStringIncludesCount() {
        InstallmentRequest request = new InstallmentRequest(new TransactionRequest());
        request.count(6);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("installments%5Bcount%5D=6"), queryString);
    }
}
