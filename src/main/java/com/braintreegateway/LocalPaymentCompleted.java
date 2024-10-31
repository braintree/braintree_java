package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.util.NodeWrapper;

public class LocalPaymentCompleted {
    private String bic;
    private List<BlikAlias> blikAliases;
    private String ibanLastChars;
    private String payerId;
    private String payerName;
    private String paymentId;
    private String paymentMethodNonce;
    private Transaction transaction;

    public LocalPaymentCompleted(NodeWrapper node) {
        this.bic = node.findString("bic");
        this.ibanLastChars = node.findString("iban-last-chars");
        this.payerId = node.findString("payer-id");
        this.payerName = node.findString("payer-name");
        this.paymentId = node.findString("payment-id");
        this.paymentMethodNonce = node.findString("payment-method-nonce");

        NodeWrapper transactionNode = node.findFirst("transaction");
        if (transactionNode != null) {
            this.transaction = new Transaction(transactionNode);
        }

        blikAliases = new ArrayList<BlikAlias>();
        for (NodeWrapper blikAliasNode : node.findAll("blik-aliases/blik-alias")) {
            blikAliases.add(new BlikAlias(blikAliasNode));
        }
    }

    public String getBic() {
        return bic;
    }

    public List<BlikAlias> getBlikAlias() {
        return blikAliases;
    }

    public String getIbanLastChars() {
        return ibanLastChars;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentMethodNonce() {
        return paymentMethodNonce;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
