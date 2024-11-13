package com.braintreegateway.unittest.customersession;

import com.braintreegateway.Result;
import com.braintreegateway.customersession.*;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.GraphQLClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GetCustomerInsightsGatewayTest {

  @Mock private GraphQLClient graphQLClient;

  @InjectMocks private CustomerSessionGateway customerSessionGateway;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetCustomerInsights_invokes_GraphQLClient() throws IOException {
    Map<String, Object> successResponse =
        TestHelper.readResponseFromJsonResource(
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
    Map<String, Object> successResponse =
        TestHelper.readResponseFromJsonResource(
            "unittest/customer_session/customer_insights_successful_response.json");
    when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

    Result<CustomerInsightsPayload> result =
        customerSessionGateway.getCustomerInsights(getMockCustomerInsightsInput());

    CustomerInsightsPayload actualPayload = result.getTarget();
    assertEquals(
        InsightPaymentOption.PAYPAL,
        actualPayload.getInsights().getPaymentRecommendations().get(0).getPaymentOption());
  }

  private static CustomerInsightsInput getMockCustomerInsightsInput() {
    return new CustomerInsightsInput("session-id", Arrays.asList(Insights.PAYMENT_INSIGHTS));
  }

  @Test
  public void testGetCustomerInsights_OnValidationErrors() throws IOException {
    Map<String, Object> errorResponse =
        TestHelper.readResponseFromJsonResource(
            "unittest/customer_session/customer_insights_with_errors.json");
    when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

    Result<CustomerInsightsPayload> result =
        customerSessionGateway.getCustomerInsights(getMockCustomerInsightsInput());
    assertEquals(
        "validation error", result.getErrors().getAllValidationErrors().get(0).getMessage());
  }

  @Test
  public void testGetCustomerInsights_OnParsingError() throws IOException {
    Map<String, Object> errorResponse =
        TestHelper.readResponseFromJsonResource(
            "unittest/customer_session/customer_insights_unparseable_response.json");
    when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);
    assertThrows(
        UnexpectedException.class,
        () -> {
          customerSessionGateway.getCustomerInsights(getMockCustomerInsightsInput());
        });
  }
}
