package com.braintreegateway.unittest;

import com.braintreegateway.TransactionSearchRequest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionSearchRequestTest {

    @Test
    public void sepaDirectPayPalV2OrderIdTest() {
        TransactionSearchRequest transactionSearchRequest = new TransactionSearchRequest();

        String expectedXml = "<search><sepa_debit_paypal_v2_order_id><is>hello</is></sepa_debit_paypal_v2_order_id></search>";
        assertEquals(expectedXml, transactionSearchRequest.sepaDirectDebitPayPalV2OrderId().is("hello").toXML());
    }
}
