package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.braintreegateway.BankAccountInstantVerificationJwtRequest;

public class BankAccountInstantVerificationJwtRequestTest {

    @Test
    public void toGraphQLVariablesIncludesAllSetFields() {
        BankAccountInstantVerificationJwtRequest request =
                new BankAccountInstantVerificationJwtRequest()
                        .businessName("Acme Inc")
                        .returnUrl("https://example.com/return")
                        .cancelUrl("https://example.com/cancel");

        assertAll("getters",
                () -> assertEquals("Acme Inc", request.getBusinessName()),
                () -> assertEquals("https://example.com/return", request.getReturnUrl()),
                () -> assertEquals("https://example.com/cancel", request.getCancelUrl()));

        Map<String, Object> variables = request.toGraphQLVariables();
        assertTrue(variables.containsKey("input"));

        @SuppressWarnings("unchecked")
        Map<String, Object> input = (Map<String, Object>) variables.get("input");
        assertAll("graphQL input",
                () -> assertEquals("Acme Inc", input.get("businessName")),
                () -> assertEquals("https://example.com/return", input.get("returnUrl")),
                () -> assertEquals("https://example.com/cancel", input.get("cancelUrl")));
    }

    @Test
    public void toGraphQLVariablesOmitsNullFields() {
        Map<String, Object> variables = new BankAccountInstantVerificationJwtRequest()
                .businessName("Acme Inc")
                .toGraphQLVariables();

        @SuppressWarnings("unchecked")
        Map<String, Object> input = (Map<String, Object>) variables.get("input");
        assertTrue(input.containsKey("businessName"));
        assertFalse(input.containsKey("returnUrl"));
        assertFalse(input.containsKey("cancelUrl"));
    }
}
