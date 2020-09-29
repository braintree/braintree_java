package com.braintreegateway;

import java.math.BigDecimal;
import java.util.Calendar;

import com.braintreegateway.util.NodeWrapper;

public final class DisputeTransaction {

    private final BigDecimal amount;
    private final Calendar createdAt;
    private final String id;
    private final Integer installmentCount;
    private final String orderId;
    private final String paymentInstrumentSubtype;
    private final String purchaseOrderNumber;

    public DisputeTransaction(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        createdAt = node.findDateTime("created-at");
        id = node.findString("id");
        installmentCount = node.findInteger("installment-count");
        orderId = node.findString("order-id");
        paymentInstrumentSubtype = node.findString("payment-instrument-subtype");
        purchaseOrderNumber = node.findString("purchase-order-number");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public Integer getInstallmentCount() {
        return installmentCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentInstrumentSubtype() {
        return paymentInstrumentSubtype;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }
}
