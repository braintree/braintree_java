package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class EuropeBankAccount implements PaymentMethod {
    private String token;
    private boolean isDefault;
    private String maskedIban;
    private String bic;
    private String mandateReferenceNumber;
    private String accountHolderName;
    private String imageUrl;
    private String customerId;

    public enum MandateType {
        BUSINESS("business"),
        CONSUMER("consumer");
        private final String name;

        MandateType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public EuropeBankAccount(NodeWrapper node) {
        this.token = node.findString("token");
        this.isDefault = node.findBoolean("default");
        this.maskedIban = node.findString("masked-iban");
        this.bic = node.findString("bic");
        this.mandateReferenceNumber = node.findString("mandate-reference-number");
        this.accountHolderName = node.findString("account-holder-name");
        this.imageUrl = node.findString("image-url");
        this.customerId = node.findString("customer-id");
    }

    public String getToken() {
        return token;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getMaskedIban() {
        return maskedIban;
    }

    public String getBic() {
        return bic;
    }

    public String getMandateReferenceNumber() {
        return mandateReferenceNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCustomerId() {
        return customerId;
    }
}
