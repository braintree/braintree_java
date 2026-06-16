package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.LocalPaymentCompleted;
import com.braintreegateway.util.SimpleNodeWrapper;

public class LocalPaymentCompletedTest {

    @Test
    public void parsesAllFieldsIncludingTransactionAndBlikAliases() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<local-payment>" +
                "<bic>a-bic</bic>" +
                "<iban-last-chars>1234</iban-last-chars>" +
                "<payer-id>a-payer-id</payer-id>" +
                "<payer-name>a-payer-name</payer-name>" +
                "<payment-id>a-payment-id</payment-id>" +
                "<payment-method-nonce>a-nonce</payment-method-nonce>" +
                "<blik-aliases type=\"array\">" +
                "<blik-alias><key>alias-key</key><label>alias-label</label></blik-alias>" +
                "</blik-aliases>" +
                "<transaction><id>transaction_id</id></transaction>" +
                "</local-payment>");

        LocalPaymentCompleted localPayment = new LocalPaymentCompleted(node);

        assertEquals("a-bic", localPayment.getBic());
        assertEquals("1234", localPayment.getIbanLastChars());
        assertEquals("a-payer-id", localPayment.getPayerId());
        assertEquals("a-payer-name", localPayment.getPayerName());
        assertEquals("a-payment-id", localPayment.getPaymentId());
        assertEquals("a-nonce", localPayment.getPaymentMethodNonce());

        assertEquals(1, localPayment.getBlikAlias().size());
        assertEquals("alias-key", localPayment.getBlikAlias().get(0).getKey());
        assertEquals("alias-label", localPayment.getBlikAlias().get(0).getLabel());

        assertEquals("transaction_id", localPayment.getTransaction().getId());
    }

    @Test
    public void handlesMissingTransactionAndBlikAliases() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<local-payment>" +
                "<payment-id>a-payment-id</payment-id>" +
                "</local-payment>");

        LocalPaymentCompleted localPayment = new LocalPaymentCompleted(node);

        assertEquals("a-payment-id", localPayment.getPaymentId());
        assertNull(localPayment.getTransaction());
        assertTrue(localPayment.getBlikAlias().isEmpty());
    }
}
