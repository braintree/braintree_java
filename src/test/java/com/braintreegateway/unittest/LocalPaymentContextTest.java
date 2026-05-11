package com.braintreegateway.unittest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.braintreegateway.LocalPaymentContext;

public class LocalPaymentContextTest {
    @Test
    public void testConstructorWithAllFields() {
        Map<String, Object> amount = new HashMap<>();
        amount.put("value", "10.00");
        amount.put("currencyCode", "EUR");

        Map<String, Object> response = new HashMap<>();
        response.put("id", "payment-context-id");
        response.put("legacyId", "legacy-123");
        response.put("type", "MBWAY");
        response.put("paymentId", "payment-123");
        response.put("orderId", "order-456");
        response.put("approvalUrl", "https://example.com/approve");
        response.put("merchantAccountId", "merchant-account-id");
        response.put("createdAt", "2025-12-31T00:00:00Z");
        response.put("updatedAt", "2025-12-31T01:00:00Z");
        response.put("transactedAt", "2025-12-31T02:00:00Z");
        response.put("approvedAt", "2025-12-31T03:00:00Z");
        response.put("expiredAt", "2025-12-31T04:00:00Z");
        response.put("amount", amount);

        LocalPaymentContext context = new LocalPaymentContext(response);

        assertEquals("payment-context-id", context.getId());
        assertEquals("legacy-123", context.getLegacyId());
        assertEquals("MBWAY", context.getType());
        assertEquals("payment-123", context.getPaymentId());
        assertEquals("order-456", context.getOrderId());
        assertEquals("https://example.com/approve", context.getApprovalUrl());
        assertEquals("merchant-account-id", context.getMerchantAccountId());
        assertEquals("2025-12-31T00:00:00Z", context.getCreatedAt());
        assertEquals("2025-12-31T01:00:00Z", context.getUpdatedAt());
        assertEquals("2025-12-31T02:00:00Z", context.getTransactedAt());
        assertEquals("2025-12-31T03:00:00Z", context.getApprovedAt());
        assertEquals("2025-12-31T04:00:00Z", context.getExpiredAt());
        assertNotNull(context.getAmount());
        assertEquals(new BigDecimal("10.00"), context.getAmount().getValue());
        assertEquals("EUR", context.getAmount().getCurrencyCode());
    }

    @Test
    public void testConstructorWithCurrencyIsoCode() {
        Map<String, Object> amount = new HashMap<>();
        amount.put("value", "15.50");
        amount.put("currencyIsoCode", "USD");

        Map<String, Object> response = new HashMap<>();
        response.put("id", "payment-context-id-2");
        response.put("type", "CRYPTO");
        response.put("amount", amount);

        LocalPaymentContext context = new LocalPaymentContext(response);

        assertEquals("payment-context-id-2", context.getId());
        assertEquals("CRYPTO", context.getType());
        assertNotNull(context.getAmount());
        assertEquals(new BigDecimal("15.50"), context.getAmount().getValue());
        assertEquals("USD", context.getAmount().getCurrencyCode());
    }
}
