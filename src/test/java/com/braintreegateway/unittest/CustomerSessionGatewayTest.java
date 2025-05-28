package com.braintreegateway.unittest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.enums.Recommendations;
import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerRecommendationsInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;
import com.braintreegateway.graphql.types.CustomerRecommendationsPayload;
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
                        CreateCustomerSessionInput createCustomerSessionInput = CreateCustomerSessionInput.builder()
                                        .build();

                        customerSessionGateway.createCustomerSession(createCustomerSessionInput);

                        verify(graphQLClient, times(1)).query(anyString(), captor.capture());
                        assertEquals(createCustomerSessionInput.toGraphQLVariables(), captor.getValue().get("input"));
                }

                @Test
                public void testCreateCustomerSession_OnSuccess() throws IOException {
                        Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/create_session_successful_response.json");
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

                        Result<String> result = customerSessionGateway
                                        .createCustomerSession(CreateCustomerSessionInput.builder().build());

                        assertEquals("customer-session-id", result.getTarget());
                }

                @Test
                public void testCreateCustomerSession_OnValidationErrors() throws IOException {
                        Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/create_session_with_errors.json");
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

                        Result<String> result = customerSessionGateway
                                        .createCustomerSession(CreateCustomerSessionInput.builder().build());

                        assertEquals(
                                        "validation error",
                                        result.getErrors().getAllValidationErrors().get(0).getMessage());
                }

                @Test
                public void testCreateCustomerSession_OnParsingError() throws IOException {
                        Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/create_session_unparseable_response.json");

                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

                        assertThrows(
                                        ServerException.class,
                                        () -> {
                                                customerSessionGateway.createCustomerSession(
                                                                CreateCustomerSessionInput.builder().build());
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
                        UpdateCustomerSessionInput updateCustomerSessionInput = UpdateCustomerSessionInput
                                        .builder("session-id").build();

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
                                        .updateCustomerSession(
                                                        UpdateCustomerSessionInput.builder("session-id").build());

                        assertEquals("customer-session-id", result.getTarget());
                }

                @Test
                public void testUpdateCustomerSession_OnValidationErrors() throws IOException {
                        Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/update_session_with_errors.json");
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

                        Result<String> result = customerSessionGateway
                                        .updateCustomerSession(
                                                        UpdateCustomerSessionInput.builder("session-id").build());

                        assertEquals(
                                        "validation error",
                                        result.getErrors().getAllValidationErrors().get(0).getMessage());
                }

                @Test
                public void testUpdateCustomerSession_OnParsingError() throws IOException {
                        Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/update_session_unparseable_response.json");
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);
                        assertThrows(
                                        ServerException.class,
                                        () -> {
                                                customerSessionGateway.updateCustomerSession(
                                                                UpdateCustomerSessionInput.builder("session-id")
                                                                                .build());
                                        });
                }
        }

        @Nested
        public class GetCustomerRecommendations {

                @Mock
                private GraphQLClient graphQLClient;

                @InjectMocks
                private CustomerSessionGateway customerSessionGateway;

                @BeforeEach
                public void setup() {
                        MockitoAnnotations.initMocks(this);
                }

                @Test
                public void testGetCustomerRecommentations_invokes_GraphQLClient() throws IOException {
                        Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/customer_recommendations_successful_response.json");
                        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);
                        CustomerRecommendationsInput CustomerRecommendationsInput = getMockCustomerRecommendationsInput();

                        customerSessionGateway.getCustomerRecommendations(CustomerRecommendationsInput);

                        verify(graphQLClient, times(1)).query(anyString(), captor.capture());
                        assertEquals(CustomerRecommendationsInput.toGraphQLVariables(), captor.getValue().get("input"));
                }

                @Test
                public void testGetCustomerRecommendations_OnSuccess() throws IOException {
                        Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/customer_recommendations_successful_response.json");
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

                        Result<CustomerRecommendationsPayload> result = customerSessionGateway
                                        .getCustomerRecommendations(getMockCustomerRecommendationsInput());
                        CustomerRecommendationsPayload actualPayload = result.getTarget();
                        assertEquals(
                                        RecommendedPaymentOption.PAYPAL,
                                        actualPayload.getRecommendations().getPaymentOptions().get(0)
                                                        .getPaymentOption());
                }

                @Test
                public void testGetCustomerRecommendations_OnValidationErrors() throws IOException {
                        Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/customer_recommendations_with_errors.json");
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

                        Result<CustomerRecommendationsPayload> result = customerSessionGateway
                                        .getCustomerRecommendations(getMockCustomerRecommendationsInput());
                        assertEquals(
                                        "validation error",
                                        result.getErrors().getAllValidationErrors().get(0).getMessage());
                }

                @Test
                public void testGetCustomerRecommendations_OnParsingError() throws IOException {
                        Map<String, Object> errorResponse = TestHelper.readResponseFromJsonResource(
                                        "unittest/customer_session/customer_recommendations_unparseable_response.json");
                        when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);
                        assertThrows(
                                        ServerException.class,
                                        () -> {
                                                customerSessionGateway
                                                                .getCustomerRecommendations(getMockCustomerRecommendationsInput());
                                        });
                }
        }

        private static CustomerRecommendationsInput getMockCustomerRecommendationsInput() {
                return CustomerRecommendationsInput.builder().sessionId("sessionId").build();
        }
}