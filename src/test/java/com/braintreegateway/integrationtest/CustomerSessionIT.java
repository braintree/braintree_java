package com.braintreegateway.integrationtest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.graphql.enums.InsightPaymentOption;
import com.braintreegateway.graphql.enums.Insights;
import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerInsightsInput;
import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.graphql.inputs.PhoneInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;
import com.braintreegateway.graphql.types.CustomerInsightsPayload;
import com.braintreegateway.graphql.types.PaymentRecommendation;

public class CustomerSessionIT extends IntegrationTest {

  @Test
  public void createCustomerSessionWithoutEmailAndPhone() {
    CreateCustomerSessionInput input = new CreateCustomerSessionInput()
        .merchantAccountId("usd_pwpp_multi_account_merchant_account");

    Result<String> result = pwppGateway().customerSession().createCustomerSession(input);
    assertNotNull(result.getTarget());
  }

  @Test
  public void createCustomerSessionWithMerchantProvidedSessionId() {
    String merchantSessionId = "11EF-A1E7-A5F5EE5C-A2E5-AFD2801469FC";
    CreateCustomerSessionInput input = new CreateCustomerSessionInput().sessionId(merchantSessionId);

    Result<String> result = pwppGateway().customerSession().createCustomerSession(input);

    assertEquals(merchantSessionId, result.getTarget());
  }

  @Test
  public void createCustomerSessionWithAPIDerivedSessionId() {
    assertNotNull(buildCustomerSession(null).getTarget());
  }

  @Test
  public void doesNotCreateDuplicateCustomerSession() {
    String existingSessionId = "11EF-34BC-2702904B-9026-C3ECF4BAC765";

    Result<String> result = buildCustomerSession(existingSessionId);

    assertFalse(result.isSuccess());

    assertTrue(
        result
            .getErrors()
            .getAllValidationErrors()
            .get(0)
            .getMessage()
            .contains("Session IDs must be " + "unique per merchant"));
  }

  @Test
  public void updateCustomerSession() {
    String sessionId = "11EF-A1E7-A5F5EE5C-A2E5-AFD2801469FC";
    CreateCustomerSessionInput createInput = new CreateCustomerSessionInput()
        .sessionId(sessionId)
        .merchantAccountId("usd_pwpp_multi_account_merchant_account");

    pwppGateway().customerSession().createCustomerSession(createInput);

    CustomerSessionInput customer = buildCustomerSessionInput("PR5_test@example.com", "4085005005");
    UpdateCustomerSessionInput input = new UpdateCustomerSessionInput(sessionId).customer(customer);

    Result<String> result = pwppGateway().customerSession().updateCustomerSession(input);

    assertTrue(result.isSuccess());
    assertEquals(sessionId, result.getTarget());
  }

  @Test
  public void doesNotUpdateNonExistentSession() {
    String sessionId = "11EF-34BC-2702904B-9026-C3ECF4BAC765";
    CustomerSessionInput customer = buildCustomerSessionInput("PR9_test@example.com", "4085005009");
    UpdateCustomerSessionInput input = new UpdateCustomerSessionInput(sessionId).customer(customer);

    Result<String> result = pwppGateway().customerSession().updateCustomerSession(input);
    assertFalse(result.isSuccess());
    assertTrue(
        result
            .getErrors()
            .getAllValidationErrors()
            .get(0)
            .getMessage()
            .contains("does not exist"));
  }

  @Test
  public void getCustomerInsights() {
    CustomerSessionInput customer = buildCustomerSessionInput("PR5_test@example.com", "4085005005");
    CustomerInsightsInput customerInsightsInput = new CustomerInsightsInput(
        "11EF-A1E7-A5F5EE5C-A2E5-AFD2801469FC", Arrays.asList(Insights.PAYMENT_INSIGHTS))
        .customer(customer);

    Result<CustomerInsightsPayload> result = pwppGateway().customerSession().getCustomerInsights(customerInsightsInput);

    assertTrue(result.isSuccess());
    CustomerInsightsPayload payload = result.getTarget();
    assertEquals(true, payload.isInPayPalNetwork());

    PaymentRecommendation recommendation = payload.getInsights().getPaymentRecommendations().get(0);
    assertEquals(InsightPaymentOption.PAYPAL, recommendation.getPaymentOption());
    assertEquals(1, recommendation.getRecommendedPriority());
  }

  @Test
  public void doesNotGetInsightsForNonExistentSessionId() {
    CustomerSessionInput customer = buildCustomerSessionInput("PR9_test@example.com", "4085005009");
    CustomerInsightsInput customerInsightsInput = new CustomerInsightsInput(
        "11EF-34BC-2702904B-9026-C3ECF4BAC765", Arrays.asList(Insights.PAYMENT_INSIGHTS))
        .customer(customer);

    Result<CustomerInsightsPayload> result = pwppGateway().customerSession().getCustomerInsights(customerInsightsInput);

    assertFalse(result.isSuccess());
    assertTrue(
        result
            .getErrors()
            .getAllValidationErrors()
            .get(0)
            .getMessage()
            .contains("does not exist"));
  }

  private BraintreeGateway pwppGateway() {
    return new BraintreeGateway(
        Environment.DEVELOPMENT,
        "pwpp_multi_account_merchant",
        "pwpp_multi_account_merchant_public_key",
        "pwpp_multi_account_merchant_private_key");
  }

  private Result<String> buildCustomerSession(String sessionId) {
    CustomerSessionInput customer = buildCustomerSessionInput("PR1_test@example.com", "4085005002");
    CreateCustomerSessionInput input = new CreateCustomerSessionInput().customer(customer);
    if (sessionId != null) {
      input = input.sessionId(sessionId);
    }
    return pwppGateway().customerSession().createCustomerSession(input);
  }

  private CustomerSessionInput buildCustomerSessionInput(String email, String phoneNumber) {
    PhoneInput phoneInput = new PhoneInput().countryPhoneCode("1").phoneNumber(phoneNumber);

    return new CustomerSessionInput()
        .email(email)
        .deviceFingerprintId("test")
        .phone(phoneInput)
        .paypalAppInstalled(true)
        .venmoAppInstalled(true);
  }
}
