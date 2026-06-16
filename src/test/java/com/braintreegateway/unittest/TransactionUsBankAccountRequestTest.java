package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionRequest;
import com.braintreegateway.TransactionUsBankAccountRequest;

public class TransactionUsBankAccountRequestTest {

    @Test
    public void buildsXmlExposesGettersAndChainsBackToParent() {
        TransactionRequest parent = new TransactionRequest();
        TransactionUsBankAccountRequest request = new TransactionUsBankAccountRequest(parent);
        Calendar acceptedAt = Calendar.getInstance();

        TransactionRequest returned = request
                .achMandateText("I authorize this payment")
                .achMandateAcceptedAt(acceptedAt)
                .done();

        assertSame(parent, returned);

        assertAll("getters",
                () -> assertEquals("I authorize this payment", request.getAchMandateText()),
                () -> assertEquals(acceptedAt, request.getAchMandateAcceptedAt()));

        String xml = request.toXML();
        assertTrue(xml.contains("<usBankAccount>"), xml);
        assertTrue(xml.contains("<achMandateText>I authorize this payment</achMandateText>"), xml);
        assertTrue(xml.contains("<achMandateAcceptedAt type=\"datetime\">"), xml);
    }

    @Test
    public void toQueryStringIncludesAchMandateText() {
        String queryString = new TransactionUsBankAccountRequest(new TransactionRequest())
                .achMandateText("I authorize")
                .toQueryString();

        assertTrue(queryString.contains("us_bank_account%5Bach_mandate_text%5D=I+authorize"), queryString);
    }
}
