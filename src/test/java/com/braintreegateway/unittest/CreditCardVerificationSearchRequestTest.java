package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCard;
import com.braintreegateway.CreditCardVerification;
import com.braintreegateway.CreditCardVerificationSearchRequest;

public class CreditCardVerificationSearchRequestTest {

    @Test
    public void buildsSearchCriteriaForAllFields() {
        Calendar now = Calendar.getInstance();
        CreditCardVerificationSearchRequest search = new CreditCardVerificationSearchRequest();

        search.id().is("a-verification-id");
        search.creditCardCardholderName().is("Dan Schulman");
        search.creditCardExpirationDate().is("05/2025");
        search.creditCardNumber().startsWith("4111");
        search.ids().is("a-verification-id");
        search.creditCardCardType().is(CreditCard.CardType.VISA);
        search.createdAt().greaterThanOrEqualTo(now);
        search.status().is(CreditCardVerification.Status.VERIFIED);
        search.billingPostalCode().is("60622");
        search.customerEmail().is("dan@example.com");
        search.customerId().is("a-customer-id");
        search.paymentMethodToken().is("a-token");

        String xml = search.toXML();
        assertTrue(xml.contains("<id><is>a-verification-id</is></id>"), xml);
        assertTrue(xml.contains("<credit_card_cardholder_name><is>Dan Schulman</is></credit_card_cardholder_name>"), xml);
        assertTrue(xml.contains("<credit_card_expiration_date>"), xml);
        assertTrue(xml.contains("<credit_card_number><starts_with>4111</starts_with></credit_card_number>"), xml);
        assertTrue(xml.contains("<ids type=\"array\">"), xml);
        assertTrue(xml.contains("<credit_card_card_type type=\"array\">"), xml);
        assertTrue(xml.contains("<created_at>"), xml);
        assertTrue(xml.contains("<status type=\"array\">"), xml);
        assertTrue(xml.contains("<billing_address_details_postal_code><is>60622</is></billing_address_details_postal_code>"), xml);
        assertTrue(xml.contains("<customer_email><is>dan@example.com</is></customer_email>"), xml);
        assertTrue(xml.contains("<customer_id><is>a-customer-id</is></customer_id>"), xml);
        assertTrue(xml.contains("<payment_method_token><is>a-token</is></payment_method_token>"), xml);
    }
}
