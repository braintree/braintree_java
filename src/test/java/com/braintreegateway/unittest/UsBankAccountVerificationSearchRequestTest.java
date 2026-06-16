package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.braintreegateway.UsBankAccountVerification;
import com.braintreegateway.UsBankAccountVerificationSearchRequest;

public class UsBankAccountVerificationSearchRequestTest {

    @Test
    public void buildsSearchCriteriaForAllFields() {
        Calendar now = Calendar.getInstance();
        UsBankAccountVerificationSearchRequest search = new UsBankAccountVerificationSearchRequest();

        search.id().is("a-verification-id");
        search.accountHolderName().is("Dan Schulman");
        search.customerEmail().is("dan@example.com");
        search.customerId().is("a-customer-id");
        search.paymentMethodToken().is("a-token");
        search.routingNumber().is("021000021");
        search.ids().is("a-verification-id");
        search.status().is(UsBankAccountVerification.Status.VERIFIED);
        search.verificationMethod().is(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK);
        search.createdAt().greaterThanOrEqualTo(now);
        search.accountType().is("checking");
        search.accountNumber().endsWith("1234");

        String xml = search.toXML();
        assertTrue(xml.contains("<id><is>a-verification-id</is></id>"), xml);
        assertTrue(xml.contains("<account_holder_name><is>Dan Schulman</is></account_holder_name>"), xml);
        assertTrue(xml.contains("<customer_email><is>dan@example.com</is></customer_email>"), xml);
        assertTrue(xml.contains("<customer_id><is>a-customer-id</is></customer_id>"), xml);
        assertTrue(xml.contains("<payment_method_token><is>a-token</is></payment_method_token>"), xml);
        assertTrue(xml.contains("<routing_number><is>021000021</is></routing_number>"), xml);
        assertTrue(xml.contains("<ids type=\"array\">"), xml);
        assertTrue(xml.contains("<status type=\"array\">"), xml);
        assertTrue(xml.contains("<verification_method type=\"array\">"), xml);
        assertTrue(xml.contains("<created_at>"), xml);
        assertTrue(xml.contains("<account_type>"), xml);
        assertTrue(xml.contains("<account_number>"), xml);
    }
}
