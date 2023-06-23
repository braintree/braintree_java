package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class ApplePayDetails {
    private String bin;
    private String cardholderName;
    private String cardType;
    private String commercial;
    private String countryOfIssuance;
    private String debit;
    private String durbinRegulated;
    private String expirationMonth;
    private String expirationYear;
    private String globalId;
    private String healthcare;
    private String imageUrl;
    private String issuingBank;
    private String last4;
    private String merchantTokenIdentifier;
    private String paymentInstrumentName;
    private String payroll;
    private String prepaid;
    private String productId;
    private String sourceCardLast4;
    private String sourceDescription;
    private String token;

    public ApplePayDetails(NodeWrapper node) {
        cardType = node.findString("card-type");
        paymentInstrumentName = node.findString("payment-instrument-name");
        sourceDescription = node.findString("source-description");
        cardholderName = node.findString("cardholder-name");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
        last4 = node.findString("last-4");
        token = node.findString("token");
        imageUrl = node.findString("image-url");
        prepaid = node.findString("prepaid");
        healthcare = node.findString("healthcare");
        debit = node.findString("debit");
        durbinRegulated = node.findString("durbin-regulated");
        commercial = node.findString("commercial");
        payroll = node.findString("payroll");
        issuingBank = node.findString("issuing-bank");
        countryOfIssuance = node.findString("country-of-issuance");
        productId = node.findString("product-id");
        bin = node.findString("bin");
        globalId = node.findString("global-id");
        merchantTokenIdentifier = node.findString("merchant-token-identifier");
        sourceCardLast4 = node.findString("source-card-last4");
    }

    public String getToken() {
        return token;
    }

    public String getCardType() {
        return cardType;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public String getLast4() {
        return last4;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public String getHealthcare() {
        return healthcare;
    }

    public String getDebit() {
        return debit;
    }

    public String getDurbinRegulated() {
        return durbinRegulated;
    }

    public String getCommercial() {
        return commercial;
    }

    public String getPayroll() {
        return payroll;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public String getCountryOfIssuance() {
        return countryOfIssuance;
    }

    public String getProductId() {
        return productId;
    }

    public String getBin() {
        return bin;
    }

    public String getGlobalId() {
        return globalId;
    }

    public String getMerchantTokenIdentifier() {
        return merchantTokenIdentifier;
    }

    public String getSourceCardLast4() {
        return sourceCardLast4;
    }
}
