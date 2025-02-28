package com.braintreegateway.test;

public class CreditCardNumbers {

    public enum CardTypeIndicators {
        Commercial("4111111111131010"),
        CountryOfIssuance("4111111111121102"),
        Debit("4117101010101010"),
        DurbinRegulated("4111161010101010"),
        Healthcare("4111111510101010"),
        IssuingBank("4111111141010101"),
        Payroll("4111111114101010"),
        Prepaid("4111111111111210"),
        PrepaidReloadable("4229989900000002"),

        No("4111111111310101"),
        Unknown("4111111111112101");

        private final String value;

        public String getValue() {
          return value;
        }

        private CardTypeIndicators(String value) {
          this.value = value;
        }
    }
}
