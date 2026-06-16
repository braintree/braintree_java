package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionOptionsRequest;
import com.braintreegateway.TransactionOptionsUsBankAccountRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionOptionsUsBankAccountRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        TransactionOptionsRequest parent = new TransactionOptionsRequest(new TransactionRequest());
        TransactionOptionsUsBankAccountRequest request =
                new TransactionOptionsUsBankAccountRequest(parent);

        TransactionOptionsRequest returned = request.achType("internet").done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<usBankAccount>"), xml);
        assertTrue(xml.contains("<achType>internet</achType>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("us_bank_account%5Bach_type%5D=internet"), queryString);
    }
}
