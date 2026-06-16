package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.MerchantAccountCreateForCurrencyRequest;

public class MerchantAccountCreateForCurrencyRequestTest {

    @Test
    public void buildsXmlForAllFields() {
        String xml = new MerchantAccountCreateForCurrencyRequest()
                .currency("USD")
                .id("a-merchant-account-id")
                .toXML();

        assertTrue(xml.contains("<merchant_account>"), xml);
        assertTrue(xml.contains("<currency>USD</currency>"), xml);
        assertTrue(xml.contains("<id>a-merchant-account-id</id>"), xml);
    }
}
