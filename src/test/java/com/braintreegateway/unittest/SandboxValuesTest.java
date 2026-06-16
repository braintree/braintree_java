package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.SandboxValues;

public class SandboxValuesTest {

    @Test
    public void creditCardNumberEnumValues() {
        assertAll("credit card numbers",
                () -> assertEquals("4111111111111111", SandboxValues.CreditCardNumber.VISA.number),
                () -> assertEquals("5555555555554444", SandboxValues.CreditCardNumber.MASTER_CARD.number),
                () -> assertEquals("4000111111111511", SandboxValues.CreditCardNumber.FRAUD.number),
                () -> assertEquals("378282246310005", SandboxValues.CreditCardNumber.AMEX.number));
    }

    @Test
    public void amexPayWithPointsValues() {
        assertAll("amex pay with points",
                () -> assertEquals("371260714673002", SandboxValues.CreditCardNumber.AmexPayWithPoints.SUCCESS.number),
                () -> assertEquals("378267515471109", SandboxValues.CreditCardNumber.AmexPayWithPoints.INELIGIBLE_CARD.number),
                () -> assertEquals("371544868764018", SandboxValues.CreditCardNumber.AmexPayWithPoints.INSUFFICIENT_POINTS.number));
    }

    @Test
    public void transactionAmountEnumValues() {
        assertAll("transaction amounts",
                () -> assertEquals(new BigDecimal("1000.00"), SandboxValues.TransactionAmount.AUTHORIZE.amount),
                () -> assertEquals(new BigDecimal("2000.00"), SandboxValues.TransactionAmount.DECLINE.amount),
                () -> assertEquals(new BigDecimal("3000.00"), SandboxValues.TransactionAmount.FAILED.amount));
    }

    @Test
    public void paymentMethodNonceValues() {
        assertEquals("fake-apple-pay-visa-nonce", SandboxValues.PaymentMethodNonce.APPLE_PAY_VISA.nonce);
    }

    @Test
    public void expirationDateValue() {
        assertEquals("03/2030", SandboxValues.ExpirationDate.ADYEN.expiration);
    }

    @Test
    public void failsVerificationValue() {
        assertEquals("5105105105105100", SandboxValues.FailsVerification.MASTER_CARD.number);
    }

    @Test
    public void disputeChargebackValue() {
        assertEquals("4023898493988028", SandboxValues.Dispute.CHARGEBACK);
    }
}
