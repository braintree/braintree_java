package com.braintreegateway.integrationtest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.LocalPaymentContext;
import com.braintreegateway.LocalPaymentType;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.graphql.inputs.BillingAddressInput;
import com.braintreegateway.graphql.inputs.CreateLocalPaymentContextInput;
import com.braintreegateway.graphql.inputs.MonetaryAmountInput;
import com.braintreegateway.graphql.inputs.PayerInfoInput;

public class LocalPaymentIT extends IntegrationTest {

    @Test
    public void canCreateMbwayPaymentContext() {
        MonetaryAmountInput amount = new MonetaryAmountInput();
        amount.setValue(new BigDecimal("10.00"));
        amount.setCurrencyCode("EUR");

        PayerInfoInput payerInfo = PayerInfoInput.builder()
            .givenName("John")
            .surname("Doe")
            .phoneNumber("912345678")
            .phoneCountryCode("351")
            .build();

        CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
            .amount(amount)
            .type(LocalPaymentType.MBWAY)
            .payerInfo(payerInfo)
            .returnUrl("https://example.com/return")
            .cancelUrl("https://example.com/cancel")
            .merchantAccountId("eur_pwpp_multi_account_merchant_account")
            .build();

        Result<LocalPaymentContext> result = pwppGateway().localPaymentContext().create(input);

        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget());
        assertNotNull(result.getTarget().getId());
        assertNotNull(result.getTarget().getLegacyId());
        assertEquals("MBWAY", result.getTarget().getType());
        assertEquals(new BigDecimal("10.00"), result.getTarget().getAmount().getValue());
        assertEquals("EUR", result.getTarget().getAmount().getCurrencyCode());
    }

    @Test
    public void canCreateCryptoPaymentContext() {
        MonetaryAmountInput amount = new MonetaryAmountInput();
        amount.setValue(new BigDecimal("25.00"));
        amount.setCurrencyCode("USD");

        PayerInfoInput payerInfo = PayerInfoInput.builder()
            .givenName("John")
            .surname("Doe")
            .email("john.doe@example.com")
            .build();

        CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
            .amount(amount)
            .type(LocalPaymentType.CRYPTO)
            .payerInfo(payerInfo)
            .returnUrl("https://example.com/return")
            .cancelUrl("https://example.com/cancel")
            .merchantAccountId("usd_pwpp_multi_account_merchant_account")
            .build();

        Result<LocalPaymentContext> result = pwppGateway().localPaymentContext().create(input);

        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget());
        assertNotNull(result.getTarget().getId());
        assertNotNull(result.getTarget().getLegacyId());
        assertEquals("CRYPTO", result.getTarget().getType());
        assertEquals(new BigDecimal("25.00"), result.getTarget().getAmount().getValue());
        assertEquals("USD", result.getTarget().getAmount().getCurrencyCode());
    }

    @Test
    public void returnsErrorForInvalidInput() {
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
            .merchantAccountId("invalid_merchant_account")
            .build();

        Result<LocalPaymentContext> result = pwppGateway().localPaymentContext().create(input);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrors().getAllValidationErrors().size() > 0);
    }

    @Test
    public void canFindPaymentContextById() {
        MonetaryAmountInput amount = new MonetaryAmountInput();
        amount.setValue(new BigDecimal("10.00"));
        amount.setCurrencyCode("EUR");

        PayerInfoInput payerInfo = PayerInfoInput.builder()
            .givenName("John")
            .surname("Doe")
            .phoneNumber("912345678")
            .phoneCountryCode("351")
            .build();

        CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
            .amount(amount)
            .type(LocalPaymentType.MBWAY)
            .payerInfo(payerInfo)
            .returnUrl("https://example.com/return")
            .cancelUrl("https://example.com/cancel")
            .merchantAccountId("eur_pwpp_multi_account_merchant_account")
            .build();

        Result<LocalPaymentContext> createResult = pwppGateway().localPaymentContext().create(input);
        assertTrue(createResult.isSuccess());

        String paymentContextId = createResult.getTarget().getId();
        Result<LocalPaymentContext> findResult = pwppGateway().localPaymentContext().find(paymentContextId);

        assertTrue(findResult.isSuccess());
        assertNotNull(findResult.getTarget());
        assertEquals(paymentContextId, findResult.getTarget().getId());
        assertNotNull(findResult.getTarget().getLegacyId());
        assertEquals("MBWAY", findResult.getTarget().getType());
    }

    @Test
    public void throwsNotFoundErrorForNonExistentId() {
        assertThrows(NotFoundException.class, () -> {
            pwppGateway().localPaymentContext().find("non-existent-id-123");
        });
    }

    @Test
    public void canCreatePaymentContextWithOnlyRequiredFields() {
        MonetaryAmountInput amount = new MonetaryAmountInput();
        amount.setValue(new BigDecimal("15.00"));
        amount.setCurrencyCode("USD");

        PayerInfoInput payerInfo = PayerInfoInput.builder()
            .givenName("Jane")
            .surname("Smith")
            .build();

        CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
            .amount(amount)
            .type(LocalPaymentType.CRYPTO)
            .payerInfo(payerInfo)
            .returnUrl("https://example.com/return")
            .cancelUrl("https://example.com/cancel")
            .build();

        Result<LocalPaymentContext> result = pwppGateway().localPaymentContext().create(input);

        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget());
        assertNotNull(result.getTarget().getId());
        assertNotNull(result.getTarget().getLegacyId());
        assertEquals("CRYPTO", result.getTarget().getType());
        assertEquals(new BigDecimal("15.00"), result.getTarget().getAmount().getValue());
        assertEquals("USD", result.getTarget().getAmount().getCurrencyCode());
    }

    private BraintreeGateway pwppGateway() {
        return new BraintreeGateway(
            Environment.DEVELOPMENT,
            "pwpp_multi_account_merchant",
            "pwpp_multi_account_merchant_public_key",
            "pwpp_multi_account_merchant_private_key");
    }
}
