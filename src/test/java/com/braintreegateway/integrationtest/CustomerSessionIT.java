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
import com.braintreegateway.graphql.enums.Recommendations;
import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerRecommendationsInput;
import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.graphql.inputs.PhoneInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;
import com.braintreegateway.graphql.types.CustomerRecommendationsPayload;
import com.braintreegateway.graphql.types.PaymentOptions;

public class CustomerSessionIT extends IntegrationTest {

  @Test
  public void createCustomerSessionWithoutEmailAndPhone() {
    CreateCustomerSessionInput input =
      CreateCustomerSessionInput.builder()
        .merchantAccountId("usd_pwpp_multi_account_merchant_account")
        .build();

    Result<String> result =
      pwppGateway()
        .customerSession()
        .createCustomerSession(input);

    assertNotNull(result.getTarget());
  }

  @Test
  public void createCustomerSessionWithMerchantProvidedSessionId() {
    String merchantSessionId = "11EF-A1E7-A5F5EE5C-A2E5-AFD2801469FC";
    CreateCustomerSessionInput input = 
      CreateCustomerSessionInput
        .builder()
        .sessionId(merchantSessionId)
        .build();

    Result<String> result = pwppGateway()
                              .customerSession()
                              .createCustomerSession(input);

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
    CreateCustomerSessionInput createInput = 
      CreateCustomerSessionInput
        .builder()
        .sessionId(sessionId)
        .merchantAccountId("usd_pwpp_multi_account_merchant_account")
        .build();

    pwppGateway()
      .customerSession()
      .createCustomerSession(createInput);

    CustomerSessionInput customer = buildCustomerSessionInput("PR5_test@example.com", "4085005005");
    UpdateCustomerSessionInput input = 
      UpdateCustomerSessionInput
        .builder(sessionId)
        .customer(customer)
        .build();

    Result<String> result =
      pwppGateway()
        .customerSession()
        .updateCustomerSession(input);

    assertTrue(result.isSuccess());
    assertEquals(sessionId, result.getTarget());
  }

  @Test
  public void doesNotUpdateNonExistentSession() {
    String sessionId = "11EF-34BC-2702904B-9026-C3ECF4BAC765";
    CustomerSessionInput customer = buildCustomerSessionInput("PR9_test@example.com", "4085005009");
    UpdateCustomerSessionInput input =
      UpdateCustomerSessionInput
        .builder(sessionId)
        .customer(customer)
        .build();

    Result<String> result =
      pwppGateway()
        .customerSession()
        .updateCustomerSession(input);

    assertFalse(result.isSuccess());
    assertTrue(
        result
            .getErrors()
            .getAllValidationErrors()
            .get(0)
            .getMessage()
            .contains("does not exist")
    );
  }

  @Test
  public void getCustomerRecommendations() {
    CustomerSessionInput customer = buildCustomerSessionInput("PR5_test@example.com", "4085005005");
    CustomerRecommendationsInput customerRecommendationsInput =
      CustomerRecommendationsInput
        .builder(
          "11EF-A1E7-A5F5EE5C-A2E5-AFD2801469FC", 
          Arrays.asList(Recommendations.PAYMENT_RECOMMENDATIONS)
        )
        .customer(customer)
        .build();

    Result<CustomerRecommendationsPayload> result = pwppGateway().customerSession().getCustomerRecommendations(customerRecommendationsInput);

    assertTrue(result.isSuccess());
    CustomerRecommendationsPayload payload = result.getTarget();

    assertEquals(true, payload.isInPayPalNetwork());

    PaymentOptions recommendation = 
      payload
      .getRecommendations()
      .getPaymentOptions()
      .get(0);

    assertEquals(RecommendedPaymentOption.PAYPAL, recommendation.getPaymentOption());
    assertEquals(1, recommendation.getRecommendedPriority());
  }

  @Test
  public void doesNotGetRecommendationsForNonExistentSessionId() {
    CustomerSessionInput customer = buildCustomerSessionInput("PR9_test@example.com", "4085005009");
    CustomerRecommendationsInput customerRecommendationsInput = 
      CustomerRecommendationsInput
        .builder(
          "11EF-34BC-2702904B-9026-C3ECF4BAC765", 
          Arrays.asList(Recommendations.PAYMENT_RECOMMENDATIONS)
        )
        .customer(customer)
        .build();

    Result<CustomerRecommendationsPayload> result = 
      pwppGateway()
        .customerSession()
        .getCustomerRecommendations(customerRecommendationsInput);

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

    CreateCustomerSessionInput.Builder inputBuilder =
      CreateCustomerSessionInput
        .builder()
        .customer(customer);

    CreateCustomerSessionInput input = 
          (sessionId != null)
            ? inputBuilder.sessionId(sessionId).build()
            : inputBuilder.build();

    return pwppGateway().customerSession().createCustomerSession(input);
  }

  private CustomerSessionInput buildCustomerSessionInput(String email, String phoneNumber) {
    PhoneInput phoneInput = 
      PhoneInput
        .builder()
        .countryPhoneCode("1")
        .phoneNumber(phoneNumber)
        .build();

    return CustomerSessionInput.builder()
        .email(email)
        .deviceFingerprintId("test")
        .phone(phoneInput)
        .paypalAppInstalled(true)
        .venmoAppInstalled(true)
        .userAgent("Mozilla")
        .build();
  }
}
