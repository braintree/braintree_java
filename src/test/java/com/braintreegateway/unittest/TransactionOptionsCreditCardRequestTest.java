package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionOptionsCreditCardRequest;
import com.braintreegateway.TransactionOptionsRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionOptionsCreditCardRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        TransactionOptionsRequest parent = new TransactionOptionsRequest(new TransactionRequest());
        TransactionOptionsCreditCardRequest request = new TransactionOptionsCreditCardRequest(parent);

        TransactionOptionsRequest returned = request
                .accountType("credit")
                .processDebitAsCredit(true)
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<creditCard>"), xml);
        assertTrue(xml.contains("<accountType>credit</accountType>"), xml);
        assertTrue(xml.contains("<processDebitAsCredit>true</processDebitAsCredit>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("credit_card%5Baccount_type%5D=credit"), queryString);
    }
}
