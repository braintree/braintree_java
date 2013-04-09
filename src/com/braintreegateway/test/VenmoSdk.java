package com.braintreegateway.test;

public class VenmoSdk {
    public enum PaymentMethodCode {
        Visa("4111111111111111");

        public String code;

        private PaymentMethodCode(String number) {
            this.code = VenmoSdk.generateTestPaymentMethodCode(number);
        }
    }

    public static String generateTestPaymentMethodCode(String number) {
        return "stub-" + number;
    }
}
