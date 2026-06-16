package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.UsBankAccountVerificationSearchRequest;

public class EndsWithNodeTest {

    @Test
    public void endsWithSerializesCorrectCriteria() {
        UsBankAccountVerificationSearchRequest search = new UsBankAccountVerificationSearchRequest();
        search.accountNumber().endsWith("1234");

        String xml = search.toXML();
        assertTrue(xml.contains("<account_number><ends_with>1234</ends_with></account_number>"), xml);
    }
}
