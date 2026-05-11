package com.braintreegateway;

import com.braintreegateway.exceptions.ServerException;
import java.math.BigDecimal;
import java.util.Map;

public class LocalPaymentContext {

    private String id;
    private String legacyId;
    private String type;
    private String paymentId;
    private String orderId;
    private String approvalUrl;
    private String merchantAccountId;
    private String createdAt;
    private String updatedAt;
    private String transactedAt;
    private String approvedAt;
    private String expiredAt;
    private MonetaryAmount amount;

    public LocalPaymentContext(Map<String, Object> response) {
        this.id = popValue(response, "id");
        this.legacyId = getValueOptional(response, "legacyId");
        this.type = popValue(response, "type");
        this.paymentId = getValueOptional(response, "paymentId");
        this.orderId = getValueOptional(response, "orderId");
        this.approvalUrl = getValueOptional(response, "approvalUrl");
        this.merchantAccountId = getValueOptional(response, "merchantAccountId");
        this.createdAt = getValueOptional(response, "createdAt");
        this.updatedAt = getValueOptional(response, "updatedAt");
        this.transactedAt = getValueOptional(response, "transactedAt");
        this.approvedAt = getValueOptional(response, "approvedAt");
        this.expiredAt = getValueOptional(response, "expiredAt");
        this.amount = extractAmount(response);
    }

    public String getId() {
        return id;
    }

    public String getLegacyId() {
        return legacyId;
    }

    public String getType() {
        return type;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getApprovalUrl() {
        return approvalUrl;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getTransactedAt() {
        return transactedAt;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    private MonetaryAmount extractAmount(Map<String, Object> response) {
        Map<String, Object> amountHash = getValueOptional(response, "amount");
        if (amountHash == null) {
            return null;
        }

        String currencyCode = null;
        if (amountHash.containsKey("currencyCode")) {
            currencyCode = (String) amountHash.get("currencyCode");
        } else if (amountHash.containsKey("currencyIsoCode")) {
            currencyCode = (String) amountHash.get("currencyIsoCode");
        }

        String valueStr = (String) amountHash.get("value");
        BigDecimal value = null;
        if (valueStr != null) {
            value = new BigDecimal(valueStr);
        }

        MonetaryAmount monetaryAmount = new MonetaryAmount();
        monetaryAmount.setValue(value);
        monetaryAmount.setCurrencyCode(currencyCode);
        return monetaryAmount;
    }

    private <T> T popValue(Map<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            throw new ServerException("Couldn't parse response");
        }
        return (T) map.get(key);
    }

    private <T> T getValueOptional(Map<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            return null;
        }
        return (T) map.get(key);
    }
}
