package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PayPalHereDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class PayPalHereDetailsTest {

    @Test
    public void parsesAllFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<paypal-here>" +
                "<authorization-id>auth-id</authorization-id>" +
                "<capture-id>capture-id</capture-id>" +
                "<invoice-id>invoice-id</invoice-id>" +
                "<last-4>1234</last-4>" +
                "<payment-id>payment-id</payment-id>" +
                "<payment-type>a-payment-type</payment-type>" +
                "<refund-id>refund-id</refund-id>" +
                "<transaction-fee-amount>1.23</transaction-fee-amount>" +
                "<transaction-fee-currency-iso-code>USD</transaction-fee-currency-iso-code>" +
                "<transaction-initiation-date>2018-10-11</transaction-initiation-date>" +
                "<transaction-updated-date>2018-10-12</transaction-updated-date>" +
                "</paypal-here>");

        PayPalHereDetails details = new PayPalHereDetails(node);

        assertAll("paypal here fields",
                () -> assertEquals("auth-id", details.getAuthorizationId()),
                () -> assertEquals("capture-id", details.getCaptureId()),
                () -> assertEquals("invoice-id", details.getInvoiceId()),
                () -> assertEquals("1234", details.getLast4()),
                () -> assertEquals("payment-id", details.getPaymentId()),
                () -> assertEquals("a-payment-type", details.getPaymentType()),
                () -> assertEquals("refund-id", details.getRefundId()),
                () -> assertEquals("1.23", details.getTransactionFeeAmount()),
                () -> assertEquals("USD", details.getTransactionFeeCurrencyIsoCode()),
                () -> assertEquals("2018-10-11", details.getTransactionInitiationDate()),
                () -> assertEquals("2018-10-12", details.getTransactionUpdatedDate()));
    }
}
