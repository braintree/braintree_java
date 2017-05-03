package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class IbanBankAccount {
    private String accountHolderName;
    private String ibanAccountNumberLast4;
    private String ibanCountry;
    private String description;
    private String maskedIban;
    private String bic;

    public IbanBankAccount(NodeWrapper node) {
        this.accountHolderName = node.findString("account-holder-name");
        this.ibanAccountNumberLast4 = node.findString("iban-account-number-last-4");
        this.ibanCountry = node.findString("iban-country");
        this.description = node.findString("description");
        this.maskedIban = node.findString("masked-iban");
        this.bic = node.findString("bic");
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getIbanAccountNumberLast4() {
        return ibanAccountNumberLast4;
    }

    public String getIbanCountry() {
        return ibanCountry;
    }

    public String getDescription() {
        return description;
    }

    public String getMaskedIban() {
        return maskedIban;
    }

    public String getBic() {
        return bic;
    }
}
