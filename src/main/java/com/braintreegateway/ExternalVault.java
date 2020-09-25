package com.braintreegateway;

public class ExternalVault {
    private ExternalVault() {

    }

    public enum Status {
        WILL_VAULT("will_vault"),
        VAULTED("vaulted");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
