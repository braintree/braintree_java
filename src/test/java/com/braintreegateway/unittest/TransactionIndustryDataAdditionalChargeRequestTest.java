package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionIndustryDataAdditionalChargeRequest;
import com.braintreegateway.TransactionIndustryDataRequest;

public class TransactionIndustryDataAdditionalChargeRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        TransactionIndustryDataRequest parent = new TransactionIndustryDataRequest(null);
        TransactionIndustryDataAdditionalChargeRequest request =
                new TransactionIndustryDataAdditionalChargeRequest(parent);

        TransactionIndustryDataRequest returned = request
                .kind(TransactionIndustryDataAdditionalChargeRequest.Kind.MINI_BAR)
                .amount(new BigDecimal("12.34"))
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<item>"), xml);
        assertTrue(xml.contains("<kind>mini_bar</kind>"), xml);
        assertTrue(xml.contains("<amount>12.34</amount>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("mini_bar"), queryString);
    }
}
