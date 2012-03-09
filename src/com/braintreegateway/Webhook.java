package com.braintreegateway;

public class Webhook {
	public enum Kind {
        SUBSCRIPTION_PAST_DUE("subscription_past_due");

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
