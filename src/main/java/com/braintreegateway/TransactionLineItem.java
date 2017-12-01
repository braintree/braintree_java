package com.braintreegateway;

public class TransactionLineItem {

    public enum Kind {
        DEBIT("debit"),
        CREDIT("credit");

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private TransactionLineItem() {
    }

}
