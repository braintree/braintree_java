package com.braintreegateway.unittest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.braintreegateway.CustomerSessionGateway;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.graphql.enums.InsightPaymentOption;
import com.braintreegateway.graphql.enums.Insights;
import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerInsightsInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;
import com.braintreegateway.graphql.types.CustomerInsightsPayload;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.GraphQLClient;

public class CustomerSessionGatewayTest {

    @Nested
    public class CreateCustomerSession {

        @Mock
        private GraphQLClient graphQLClient;

        @InjectMocks
        private CustomerSessionGateway customerSessionGateway;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testCreateCustomerSession_invokes_GraphQLClient() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/create_session_successful_response.json");
            ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);
            CreateCustomerSessionInput createCustomerSessionInput = new CreateCustomerSessionInput();

            customerSessionGateway.createCustomerSession(createCustomerSessionInput);

            verify(graphQLClient, times(1)).query(anyString(), captor.capture());
            assertEquals(createCustomerSessionInput.toGraphQLVariables(), captor.getValue().get("input"));
        }

        @Test
        public void testCreateCustomerSession_OnSuccess() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/create_session_successful_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

            Result<String> result = customerSessionGateway.createCustomerSession(new CreateCustomerSessionInput());

            assertEquals("customer-session-id", result.getTarget());
        }

        @Test
        public void testCreateCustomerSession_OnValidationErrors() throws IOException {
            Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/create_session_with_errors.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

            Result<String> result = customerSessionGateway.createCustomerSession(new CreateCustomerSessionInput());

            assertEquals(
                    "validation error", result.getErrors().getAllValidationErrors().get(0).getMessage());
        }

        @Test
        public void testCreateCustomerSession_OnParsingError() throws IOException {
            Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/create_session_unparseable_response.json");

            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

            assertThrows(
                    UnexpectedException.class,
                    () -> {
                        customerSessionGateway.createCustomerSession(new CreateCustomerSessionInput());
                    });
        }
    }

    @Nested
    public class UpdateCustomerSessionGateway {

        @Mock
        private GraphQLClient graphQLClient;

        @InjectMocks
        private CustomerSessionGateway customerSessionGateway;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testUpdateCustomerSession_invokes_GraphQLClient() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/update_session_successful_response.json");
            ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);
            UpdateCustomerSessionInput updateCustomerSessionInput = new UpdateCustomerSessionInput("session-id");

            customerSessionGateway.updateCustomerSession(updateCustomerSessionInput);

            verify(graphQLClient, times(1)).query(anyString(), captor.capture());
            assertEquals(updateCustomerSessionInput.toGraphQLVariables(), captor.getValue().get("input"));
        }

        @Test
        public void testUpdateCustomerSession_OnSuccess() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/update_session_successful_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

            Result<String> result = customerSessionGateway
                    .updateCustomerSession(new UpdateCustomerSessionInput("session-id"));

            assertEquals("customer-session-id", result.getTarget());
        }

        @Test
        public void testUpdateCustomerSession_OnValidationErrors() throws IOException {
            Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/update_session_with_errors.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

            Result<String> result = customerSessionGateway
                    .updateCustomerSession(new UpdateCustomerSessionInput("session-id"));

            assertEquals(
                    "validation error", result.getErrors().getAllValidationErrors().get(0).getMessage());
        }

        @Test
        public void testUpdateCustomerSession_OnParsingError() throws IOException {
            Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/update_session_unparseable_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);
            assertThrows(
                    UnexpectedException.class,
                    () -> {
                        customerSessionGateway.updateCustomerSession(
                                new UpdateCustomerSessionInput("session-id"));
                    });
        }
    }

    @Nested
    public class GetCustomerInsights {

        @Mock
        private GraphQLClient graphQLClient;

        @InjectMocks
        private CustomerSessionGateway customerSessionGateway;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testGetCustomerInsights_invokes_GraphQLClient() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/customer_insights_successful_response.json");
            ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);
            CustomerInsightsInput customerInsightsInput = getMockCustomerInsightsInput();

            customerSessionGateway.getCustomerInsights(customerInsightsInput);

            verify(graphQLClient, times(1)).query(anyString(), captor.capture());
            assertEquals(customerInsightsInput.toGraphQLVariables(), captor.getValue().get("input"));
        }

        @Test
        public void testGetCustomerInsights_OnSuccess() throws IOException {
            Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/customer_insights_successful_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

            Result<CustomerInsightsPayload> result = customerSessionGateway
                    .getCustomerInsights(getMockCustomerInsightsInput());

            CustomerInsightsPayload actualPayload = result.getTarget();
            assertEquals(
                    InsightPaymentOption.PAYPAL,
                    actualPayload.getInsights().getPaymentRecommendations().get(0).getPaymentOption());
        }


        @Test
        public void testGetCustomerInsights_OnValidationErrors() throws IOException {
            Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/customer_insights_with_errors.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

            Result<CustomerInsightsPayload> result = customerSessionGateway
                    .getCustomerInsights(getMockCustomerInsightsInput());
            assertEquals(
                    "validation error", result.getErrors().getAllValidationErrors().get(0).getMessage());
        }

        @Test
        public void testGetCustomerInsights_OnParsingError() throws IOException {
            Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                    "unittest/customer_session/customer_insights_unparseable_response.json");
            when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);
            assertThrows(
                    UnexpectedException.class,
                    () -> {
                        customerSessionGateway.getCustomerInsights(getMockCustomerInsightsInput());
                    });
        }
    }

    private static CustomerInsightsInput getMockCustomerInsightsInput() {
        return new CustomerInsightsInput("session-id", Arrays.asList(Insights.PAYMENT_INSIGHTS));
    }
}
