package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionOptionsAmexRewardsRequest;
import com.braintreegateway.TransactionOptionsRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionOptionsAmexRewardsRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        TransactionOptionsRequest parent = new TransactionOptionsRequest(new TransactionRequest());
        TransactionOptionsAmexRewardsRequest request = new TransactionOptionsAmexRewardsRequest(parent);

        TransactionOptionsRequest returned = request
                .requestId("a-request-id")
                .points("100")
                .currencyAmount("10.00")
                .currencyIsoCode("USD")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertAll("amex rewards xml",
                () -> assertTrue(xml.contains("<amex-rewards>"), xml),
                () -> assertTrue(xml.contains("<request-id>a-request-id</request-id>"), xml),
                () -> assertTrue(xml.contains("<points>100</points>"), xml),
                () -> assertTrue(xml.contains("<currency-amount>10.00</currency-amount>"), xml),
                () -> assertTrue(xml.contains("<currency-iso-code>USD</currency-iso-code>"), xml));

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("a-request-id"), queryString);
    }
}
