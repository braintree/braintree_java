package com.braintreegateway.unittest.customersession;

import com.braintreegateway.Result;
import com.braintreegateway.customersession.CustomerSessionGateway;
import com.braintreegateway.customersession.UpdateCustomerSessionInput;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UpdateCustomerSessionGatewayTest {

  @Mock private GraphQLClient graphQLClient;

  @InjectMocks private CustomerSessionGateway customerSessionGateway;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testUpdateCustomerSession_invokes_GraphQLClient() throws IOException {
    Map<String, Object> successResponse =
        TestHelper.readResponseFromJsonResource(
            "unittest/customer_session/update_session_successful_response.json");
    ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
    when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);
    UpdateCustomerSessionInput updateCustomerSessionInput =
        new UpdateCustomerSessionInput("session-id");

    customerSessionGateway.updateCustomerSession(updateCustomerSessionInput);

    verify(graphQLClient, times(1)).query(anyString(), captor.capture());
    assertEquals(updateCustomerSessionInput.toGraphQLVariables(), captor.getValue().get("input"));
  }

  @Test
  public void testUpdateCustomerSession_OnSuccess() throws IOException {
    Map<String, Object> successResponse =
        TestHelper.readResponseFromJsonResource(
            "unittest/customer_session/update_session_successful_response.json");
    when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(successResponse);

    Result<String> result =
        customerSessionGateway.updateCustomerSession(new UpdateCustomerSessionInput("session-id"));

    assertEquals("customer-session-id", result.getTarget());
  }

  @Test
  public void testUpdateCustomerSession_OnValidationErrors() throws IOException {
    Map<String, Object> errorResponse =
        TestHelper.readResponseFromJsonResource(
            "unittest/customer_session/update_session_with_errors.json");
    when(graphQLClient.query(anyString(), any(Map.class))).thenReturn(errorResponse);

    Result<String> result =
        customerSessionGateway.updateCustomerSession(new UpdateCustomerSessionInput("session-id"));

    assertEquals(
        "validation error", result.getErrors().getAllValidationErrors().get(0).getMessage());
  }

  @Test
  public void testUpdateCustomerSession_OnParsingError() throws IOException {
    Map<String, Object> errorResponse =
        TestHelper.readResponseFromJsonResource(
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
