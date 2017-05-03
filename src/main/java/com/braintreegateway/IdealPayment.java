package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.math.BigDecimal;

public class IdealPayment {
    private String id;
    private String idealTransactionId;
    private String imageUrl;
    private String status;
    private String currency;
    private BigDecimal amount;
    private String orderId;
    private String issuer;
    private String approvalUrl;
    private IbanBankAccount ibanBankAccount;

    public IdealPayment(NodeWrapper node) {
        this.id = node.findString("id");
        this.idealTransactionId = node.findString("ideal-transaction-id");
        this.imageUrl = node.findString("image-url");
        this.currency = node.findString("currency");
        this.status = node.findString("status");
        this.amount = node.findBigDecimal("amount");
        this.orderId = node.findString("order-id");
        this.issuer = node.findString("issuer");
        this.approvalUrl = node.findString("approval-url");
        this.ibanBankAccount = new IbanBankAccount(node.findFirst("iban-bank-account"));
    }

    public String getId() {
        return id;
    }

    public String getIdealTransactionId() {
        return idealTransactionId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getApprovalUrl() {
        return approvalUrl;
    }

    public IbanBankAccount getIbanBankAccount() {
        return ibanBankAccount;
    }
}
