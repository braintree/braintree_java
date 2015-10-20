package com.braintreegateway;

import java.math.BigDecimal;

/**
 * Values for testing in the {@link Environment#SANDBOX SANDBOX} environment.
 */
public class SandboxValues {
    public enum CreditCardNumber {
        VISA("4111111111111111"),
        MASTER_CARD("5555555555554444"),
        FRAUD("4000111111111511"),
        AMEX("378282246310005");
        public String number;

        private CreditCardNumber(String number) {
            this.number = number;
        }

        public enum AmexPayWithPoints {
            SUCCESS("371260714673002"),
            INELIGIBLE_CARD("378267515471109"),
            INSUFFICIENT_POINTS("371544868764018");
            public String number;

            private AmexPayWithPoints(String number) {
                this.number = number;
            }
        }
    }

    public enum PaymentMethodNonce {
        APPLE_PAY_VISA("fake-apple-pay-visa-nonce"),
        APPLE_PAY_AMEX("fake-apple-pay-amex-nonce"),
        APPLE_PAY_MASTERCARD("fake-apple-pay-mastercard-nonce");
        public String nonce;

        private PaymentMethodNonce(String nonce) {
            this.nonce = nonce;
        }
    }

    public enum FailsVerification {
        MASTER_CARD("5105105105105100");
        public String number;

        private FailsVerification(String number) {
            this.number = number;
        }
    }

    public enum TransactionAmount {
        AUTHORIZE("1000.00"), DECLINE("2000.00"), FAILED("3000.00");

        public BigDecimal amount;

        private TransactionAmount(String amount) {
            this.amount = new BigDecimal(amount);
        }
    }
}
