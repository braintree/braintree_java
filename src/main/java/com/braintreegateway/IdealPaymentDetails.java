package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class IdealPaymentDetails {
    private String idealPaymentId;
    private String idealTransactionId;
    private String imageUrl;
    private String maskedIban;
    private String bic;

    public IdealPaymentDetails(NodeWrapper node) {
        this.idealPaymentId = node.findString("ideal-payment-id");
        this.idealTransactionId = node.findString("ideal-transaction-id");
        this.imageUrl = node.findString("image-url");
        this.maskedIban = node.findString("masked-iban");
        this.bic = node.findString("bic");
    }

    public String getIdealPaymentId() {
        return idealPaymentId;
    }

    public String getIdealTransactionId() {
        return idealTransactionId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMaskedIban() {
        return maskedIban;
    }

    public String getBic() {
        return bic;
    }
}
