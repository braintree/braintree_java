package com.braintreegateway.unittest.graphql;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.braintreegateway.LocalPaymentType;
import com.braintreegateway.graphql.inputs.CreateLocalPaymentContextInput;
import com.braintreegateway.graphql.inputs.MonetaryAmountInput;
import com.braintreegateway.graphql.inputs.PayerInfoInput;

class CreateLocalPaymentContextInputTest {
    @Test
    void testToGraphQLVariables() {
        MonetaryAmountInput amount = new MonetaryAmountInput();
        amount.setValue(new BigDecimal("10.00"));
        amount.setCurrencyCode("EUR");

        PayerInfoInput payerInfo = PayerInfoInput.builder()
            .givenName("John")
            .surname("Doe")
            .build();

        CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
            .amount(amount)
            .type(LocalPaymentType.MBWAY)
            .payerInfo(payerInfo)
            .returnUrl("https://example.com/return")
            .cancelUrl("https://example.com/cancel")
            .merchantAccountId("test-merchant-account")
            .orderId("order-123")
            .countryCode("PT")
            .expiryDate("2025-12-31")
            .build();

        Map<String, Object> variables = input.toGraphQLVariables();

        assertTrue(variables.containsKey("paymentContext"));
        Map<String, Object> paymentContext = (Map<String, Object>) variables.get("paymentContext");

        assertEquals(amount.toGraphQLVariables(), paymentContext.get("amount"));
        assertEquals(LocalPaymentType.MBWAY, paymentContext.get("type"));
        assertEquals(payerInfo.toGraphQLVariables(), paymentContext.get("payerInfo"));
        assertEquals("https://example.com/return", paymentContext.get("returnUrl"));
        assertEquals("https://example.com/cancel", paymentContext.get("cancelUrl"));
        assertEquals("test-merchant-account", paymentContext.get("merchantAccountId"));
        assertEquals("order-123", paymentContext.get("orderId"));
        assertEquals("PT", paymentContext.get("countryCode"));
        assertEquals("2025-12-31", paymentContext.get("expiryDate"));
    }
}
