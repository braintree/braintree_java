package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.braintreegateway.GrantedPaymentInstrumentUpdate;
import com.braintreegateway.util.SimpleNodeWrapper;

public class GrantedPaymentInstrumentUpdateTest {

    @Test
    public void parsesAllFieldsIncludingUpdatedFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<granted-payment-instrument-update>" +
                "<grant-owner-merchant-id>vczo7jqrpwrsi2px</grant-owner-merchant-id>" +
                "<grant-recipient-merchant-id>cf0i8wgarszuy6hc</grant-recipient-merchant-id>" +
                "<payment-method-nonce><nonce>ee257d98-de40-47e8-96b3-a6954ea7a9a4</nonce></payment-method-nonce>" +
                "<token>abc123z</token>" +
                "<updated-fields type=\"array\">" +
                "<item>expiration-month</item>" +
                "<item>expiration-year</item>" +
                "</updated-fields>" +
                "</granted-payment-instrument-update>");

        GrantedPaymentInstrumentUpdate update = new GrantedPaymentInstrumentUpdate(node);

        assertEquals("vczo7jqrpwrsi2px", update.getGrantOwnerMerchantId());
        assertEquals("cf0i8wgarszuy6hc", update.getGrantRecipientMerchantId());
        assertEquals("ee257d98-de40-47e8-96b3-a6954ea7a9a4", update.getPaymentMethodNonce());
        assertEquals("abc123z", update.getToken());

        List<String> updatedFields = update.getUpdatedFields();
        assertEquals(2, updatedFields.size());
        assertEquals("expiration-month", updatedFields.get(0));
        assertEquals("expiration-year", updatedFields.get(1));
    }

    @Test
    public void handlesMissingUpdatedFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<granted-payment-instrument-update>" +
                "<token>abc123z</token>" +
                "</granted-payment-instrument-update>");

        GrantedPaymentInstrumentUpdate update = new GrantedPaymentInstrumentUpdate(node);

        assertEquals("abc123z", update.getToken());
        assertTrue(update.getUpdatedFields().isEmpty());
    }
}
