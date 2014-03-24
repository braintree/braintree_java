package com.braintreegateway;

import java.math.BigDecimal;

/**
 * Values for testing in the {@link Environment#SANDBOX SANDBOX} environment.
 */
public class SandboxValues {
    public enum CreditCardNumber {
        VISA("4111111111111111"),
        MASTER_CARD("5555555555554444"),
        FRAUD("4000111111111511");
        public String number;

        private CreditCardNumber(String number) {
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
