package com.braintreegateway.integrationtest;

import java.util.Arrays;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.enums.Recommendations;
import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerRecommendationsInput;
import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.graphql.inputs.PhoneInput;
import com.braintreegateway.graphql.inputs.PayPalPurchaseUnitInput; 
import com.braintreegateway.graphql.inputs.PayPalPayeeInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;
import com.braintreegateway.MonetaryAmountInput;
import com.braintreegateway.graphql.types.CustomerRecommendationsPayload;
import com.braintreegateway.graphql.types.PaymentRecommendation;
import com.braintreegateway.graphql.types.PaymentOptions;
import com.braintreegateway.exceptions.AuthorizationException;


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
  public void createCustomerSessionWithPurchaseUnits() {
    CreateCustomerSessionInput input = CreateCustomerSessionInput.builder()
        .purchaseUnits(buildPurchaseUnits())
        .build();

    Result<String> result = pwppGateway().customerSession().createCustomerSession(input);

    assertTrue(result.isSuccess());
    assertNotNull(result.getTarget());
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
        .purchaseUnits(buildPurchaseUnits())
        .build();

    pwppGateway()
      .customerSession()
      .createCustomerSession(createInput);

    CustomerSessionInput customer = buildCustomerSessionInput();
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
    CustomerSessionInput customer = buildCustomerSessionInput();
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
    CustomerSessionInput customer = buildCustomerSessionInput();
    CustomerRecommendationsInput customerRecommendationsInput =
      CustomerRecommendationsInput
        .builder()
        .sessionId("94f0b2db-5323-4d86-add3-paypal000000")
        .customer(customer)
        .purchaseUnits(buildPurchaseUnits())
        .domain("domain.com")
        .build();

    Result<CustomerRecommendationsPayload> result = pwppGateway().customerSession().getCustomerRecommendations(customerRecommendationsInput);
    CustomerRecommendationsPayload payload = result.getTarget();
    assertTrue(result.isSuccess());

    assertEquals(true, payload.isInPayPalNetwork());
    assertEquals("94f0b2db-5323-4d86-add3-paypal000000", payload.getSessionId());

    PaymentRecommendation recommendation = 
      payload
      .getRecommendations()
      .getPaymentRecommendations()
      .get(0);

    assertEquals(RecommendedPaymentOption.PAYPAL, recommendation.getPaymentOption());
    assertEquals(1, recommendation.getRecommendedPriority());
  }

  @Test
  public void doesNotGetRecommendationsWhenNotAuthorized() {
    CustomerSessionInput customer = buildCustomerSessionInput();
    CustomerRecommendationsInput customerRecommendationsInput = 
      CustomerRecommendationsInput
        .builder()
        .sessionId("6B29FC40-CA47-1067-B31D-00DD010662DA")
        .customer(customer)
        .purchaseUnits(buildPurchaseUnits())
        .domain("domain.com")
        .merchantAccountId("gbp_pwpp_multi_account_merchant_account")
        .build();

    assertThrows(AuthorizationException.class, () -> {
            pwppGateway().customerSession().getCustomerRecommendations(customerRecommendationsInput);
        });
  }

  private BraintreeGateway pwppGateway() {
    return new BraintreeGateway(
        Environment.DEVELOPMENT,
        "pwpp_multi_account_merchant",
        "pwpp_multi_account_merchant_public_key",
        "pwpp_multi_account_merchant_private_key");
  }

  private Result<String> buildCustomerSession(String sessionId) {
    CustomerSessionInput customer = buildCustomerSessionInput();

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

  private CustomerSessionInput buildCustomerSessionInput() {
    PhoneInput phoneInput = 
      PhoneInput
        .builder()
        .countryPhoneCode("1")
        .phoneNumber("4088888888")
        .build();

    return CustomerSessionInput.builder()
        .hashedEmail("48ddb93f0b30c475423fe177832912c5bcdce3cc72872f8051627967ef278e08")
        .hashedPhoneNumber("a2df2987b2a3384210d3aa1c9fb8b627ebdae1f5a9097766c19ca30ec4360176")
        .deviceFingerprintId("00DD010662DE")
        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/x.x.x.x Safari/537.36")
        .build();
  }

  private List<PayPalPurchaseUnitInput> buildPurchaseUnits() {
          MonetaryAmountInput amount= new MonetaryAmountInput();
          amount.setValue(new BigDecimal("100.00")); 
          amount.setCurrencyCode("USD");

        PayPalPurchaseUnitInput purchaseUnit = PayPalPurchaseUnitInput.builder(amount)
            .build();

        List<PayPalPurchaseUnitInput> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(purchaseUnit);

        return purchaseUnits;
    }
}
