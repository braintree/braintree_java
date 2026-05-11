package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.braintreegateway.LocalPaymentContext;
import com.braintreegateway.LocalPaymentContextGateway;
import com.braintreegateway.LocalPaymentType;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.graphql.inputs.CreateLocalPaymentContextInput;
import com.braintreegateway.graphql.inputs.MonetaryAmountInput;
import com.braintreegateway.graphql.inputs.PayerInfoInput;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.GraphQLClient;

public class LocalPaymentContextGatewayTest {

    @Nested
    public class CreateLocalPaymentContext {

        @Mock
        private GraphQLClient graphQLClient;

        @InjectMocks
        private LocalPaymentContextGateway localPaymentGateway;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testCreate_invokesGraphQLClient() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                "unittest/local_payment/create_successful_response.json");
            ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

            MonetaryAmountInput amount = new MonetaryAmountInput();
            amount.setValue(new BigDecimal("10.00"));
            amount.setCurrencyCode("EUR");

            CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
                .amount(amount)
                .type(LocalPaymentType.MBWAY)
                .build();

            localPaymentGateway.create(input);

            verify(graphQLClient, times(1)).query(anyString(), captor.capture());
            Map<String, Object> variables = captor.getValue();
            assertTrue(variables.containsKey("input"));
            assertEquals(input.toGraphQLVariables(), variables.get("input"));
        }

        @Test
        public void testCreate_onSuccess() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                "unittest/local_payment/create_successful_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

            MonetaryAmountInput amount = new MonetaryAmountInput();
            amount.setValue(new BigDecimal("10.00"));
            amount.setCurrencyCode("EUR");

            CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
                .amount(amount)
                .type(LocalPaymentType.MBWAY)
                .build();

            Result<LocalPaymentContext> result = localPaymentGateway.create(input);

            assertTrue(result.isSuccess());
            assertEquals("payment-context-id", result.getTarget().getId());
            assertEquals("MBWAY", result.getTarget().getType());
            assertEquals("https://example.com/approve", result.getTarget().getApprovalUrl());
            assertEquals(new BigDecimal("10.00"), result.getTarget().getAmount().getValue());
            assertEquals("EUR", result.getTarget().getAmount().getCurrencyCode());
        }

        @Test
        public void testCreate_onValidationErrors() throws IOException {
            Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                "unittest/local_payment/validation_error_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

            MonetaryAmountInput amount = new MonetaryAmountInput();
            amount.setValue(new BigDecimal("10.00"));
            amount.setCurrencyCode("EUR");

            CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput.builder()
                .amount(amount)
                .type(LocalPaymentType.MBWAY)
                .merchantAccountId("invalid-merchant-account")
                .build();

            Result<LocalPaymentContext> result = localPaymentGateway.create(input);

            assertFalse(result.isSuccess());
            assertTrue(result.getErrors().getAllValidationErrors().size() > 0);
        }
    }

    @Nested
    public class FindLocalPaymentContext {

        @Mock
        private GraphQLClient graphQLClient;

        @InjectMocks
        private LocalPaymentContextGateway localPaymentGateway;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testFind_invokesGraphQLClient() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                "unittest/local_payment/find_successful_response.json");
            ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

            localPaymentGateway.find("payment-context-id");

            verify(graphQLClient, times(1)).query(anyString(), captor.capture());
            Map<String, Object> variables = captor.getValue();
            assertEquals("payment-context-id", variables.get("id"));
        }

        @Test
        public void testFind_onSuccess() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                "unittest/local_payment/find_successful_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

            Result<LocalPaymentContext> result = localPaymentGateway.find("payment-context-id");

            assertTrue(result.isSuccess());
            assertEquals("payment-context-id", result.getTarget().getId());
            assertEquals("legacy-123", result.getTarget().getLegacyId());
            assertEquals("MBWAY", result.getTarget().getType());
            assertEquals("order-456", result.getTarget().getOrderId());
        }
    }
}
