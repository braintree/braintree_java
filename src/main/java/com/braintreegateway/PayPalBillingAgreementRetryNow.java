package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.math.BigDecimal;
import java.util.Calendar;

public class PayPalBillingAgreementRetryNow {
    private String billingAgreementId;
    private String invoiceNumber;
    private BigDecimal transactionAmount;
    private String transactionCountry;
    private String transactionCurrency;
    private Calendar transactionTime;

    public PayPalBillingAgreementRetryNow(NodeWrapper node) {
        this.billingAgreementId = node.findString("billing-agreement-id");
        this.invoiceNumber = node.findString("invoice-number");
        this.transactionAmount = node.findBigDecimal("transaction-amount");
        this.transactionCountry = node.findString("transaction-country");
        this.transactionCurrency = node.findString("transaction-currency");
        this.transactionTime = node.findDateTime("transaction-time");
    }

    public String getBillingAgreementId() {
        return billingAgreementId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionCountry() {
        return transactionCountry;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public Calendar getTransactionTime() {
        return transactionTime;
    }
}
