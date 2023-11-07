package com.braintreegateway;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionIndustryDataRequestTest {

    @Test
    void countryCode() {
        TransactionIndustryDataRequest transactionIndustryDataRequest = new TransactionIndustryDataRequest(null);
        String expectedXml = "<data><countryCode>US</countryCode></data>";
        assertEquals(expectedXml, transactionIndustryDataRequest.countryCode("US").toXML());
    }

    @Test
    void dateOfBirth() {
        TransactionIndustryDataRequest transactionIndustryDataRequest = new TransactionIndustryDataRequest(null);
        String expectedXml = "<data><dateOfBirth>2012-12-12</dateOfBirth></data>";
        assertEquals(expectedXml, transactionIndustryDataRequest.dateOfBirth("2012-12-12").toXML());
    }
}