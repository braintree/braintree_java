package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CustomerSearchRequest;

public class CustomerSearchRequestTest {

    @Test
    public void buildsSearchCriteriaForAllFields() {
        Calendar now = Calendar.getInstance();
        CustomerSearchRequest search = new CustomerSearchRequest();

        search.addressCountryName().is("United States of America");
        search.addressExtendedAddress().is("Suite 200");
        search.addressFirstName().is("Dan");
        search.addressLastName().is("Schulman");
        search.addressLocality().is("Chicago");
        search.addressPostalCode().is("60622");
        search.addressRegion().is("IL");
        search.addressStreetAddress().is("123 Main St");
        search.cardholderName().is("Dan Schulman");
        search.company().is("Braintree");
        search.creditCardExpirationDate().is("05/2025");
        search.email().is("dan@example.com");
        search.fax().is("3125559999");
        search.firstName().is("Dan");
        search.id().is("customer-id");
        search.lastName().is("Schulman");
        search.paymentMethodToken().is("a-token");
        search.paypalAccountEmail().is("dan@paypal.com");
        search.phone().is("3125551234");
        search.website().is("https://example.com");
        search.paymentMethodTokenWithDuplicates().is("a-token");
        search.ids().is("customer-id");
        search.creditCardNumber().startsWith("4111");
        search.createdAt().greaterThanOrEqualTo(now);

        String xml = search.toXML();
        // Assert every criterion is serialized as its own element (full <field> tag, not a bare substring).
        assertAll("customer search criteria",
                () -> assertTrue(xml.contains("<address_country_name>"), xml),
                () -> assertTrue(xml.contains("<address_extended_address>"), xml),
                () -> assertTrue(xml.contains("<address_first_name>"), xml),
                () -> assertTrue(xml.contains("<address_last_name>"), xml),
                () -> assertTrue(xml.contains("<address_locality>"), xml),
                () -> assertTrue(xml.contains("<address_postal_code>"), xml),
                () -> assertTrue(xml.contains("<address_region>"), xml),
                () -> assertTrue(xml.contains("<address_street_address>"), xml),
                () -> assertTrue(xml.contains("<cardholder_name>"), xml),
                () -> assertTrue(xml.contains("<company>"), xml),
                () -> assertTrue(xml.contains("<credit_card_expiration_date>"), xml),
                () -> assertTrue(xml.contains("<email>"), xml),
                () -> assertTrue(xml.contains("<fax>"), xml),
                () -> assertTrue(xml.contains("<first_name>"), xml),
                () -> assertTrue(xml.contains("<id>"), xml),
                () -> assertTrue(xml.contains("<last_name>"), xml),
                () -> assertTrue(xml.contains("<payment_method_token>"), xml),
                () -> assertTrue(xml.contains("<paypal_account_email>"), xml),
                () -> assertTrue(xml.contains("<phone>"), xml),
                () -> assertTrue(xml.contains("<website>"), xml),
                () -> assertTrue(xml.contains("<payment_method_token_with_duplicates>"), xml),
                () -> assertTrue(xml.contains("<ids type=\"array\"><item>customer-id</item></ids>"), xml),
                () -> assertTrue(xml.contains("<credit_card_number><starts_with>4111</starts_with></credit_card_number>"), xml),
                () -> assertTrue(xml.contains("<created_at>"), xml));
    }
}
