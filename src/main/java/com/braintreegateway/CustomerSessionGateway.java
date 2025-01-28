  package com.braintreegateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerRecommendationsInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;
import com.braintreegateway.graphql.types.CustomerRecommendationsPayload;
import com.braintreegateway.graphql.types.PaymentOptions;
import com.braintreegateway.graphql.unions.CustomerRecommendations;
import com.braintreegateway.util.GraphQLClient;

/**
 * Creates and manages PayPal customer sessions.
 *
 */
public class CustomerSessionGateway {

  private static final String CREATE_CUSTOMER_SESSION = "mutation CreateCustomerSession($input: CreateCustomerSessionInput!) { "
      + "createCustomerSession(input: $input) {"
      + "    sessionId"
      + "  }"
      + "}";

  private static final String UPDATE_CUSTOMER_SESSION = "mutation UpdateCustomerSession($input: UpdateCustomerSessionInput!) { "
      + "updateCustomerSession(input: $input) {"
      + "    sessionId"
      + "  }"
      + "}";

  private static final String GET_CUSTOMER_RECOMMENDATIONS = "query CustomerRecommendations($input: CustomerRecommendationsInput!) {\n"
      + "            customerRecommendations(input: $input) {\n"
      + "              isInPayPalNetwork\n"
      + "              recommendations {\n"
      + "                ... on PaymentRecommendations {\n"
      + "                  paymentOptions {\n"
      + "                    paymentOption\n"
      + "                    recommendedPriority\n"
      + "                  }\n"
      + "                }\n"
      + "              }\n"
      + "            }\n"
      + "          }";

  private final GraphQLClient graphQLClient;

  public CustomerSessionGateway(GraphQLClient graphQLClient) {
    this.graphQLClient = graphQLClient;
  }

  /**
   * Creates a new customer session.
   *
   * Customer sessions can be created with or without a merchant-provided session
   * ID. If a session ID is not provided in the input, one will be generated by
   * the gateway. Attempt to create a duplicate session will result in an error.
   * Customer specific information such as email, phone number and device
   * information can be optionally included in the input object.
   * 
   *
   * Example:
   * 
   * <pre>
   * CreateCustomerSessionInput input = CreateCustomerSessionInput
   *   .builder()
   *   .sessionId(...)
   *   .customer(...)
   *   .build()
   * 
   *  Result<String> result = gateway.customer_session().createCustomerSession(input);
   * 
   *  if (result.isSuccess()) {
   *    String sessionId = result.getTarget();
   *  }
   * </pre>
   *
   * @param input The input parameters for creating a customer session.
   * 
   * @return a {@link Result} object with session ID if successful, or errors otherwise.
   * 
   * @throws UnexpectedException If there is an unexpected error during the process.
   */
  public Result<String> createCustomerSession(CreateCustomerSessionInput input) {
    return executeMutation(CREATE_CUSTOMER_SESSION, input, "data.createCustomerSession.sessionId");
  }

  /**
   * Updates an existing customer session.
   *
   * Example:
   * 
   * <pre>
   * UpdateCustomerSessionInput input = UpdateCustomerSessionInput
   *   .builder(sessionId)
   *   .customer(...)
   *   .build()
   * 
   *  Result<String> result = gateway.customer_session().updateCustomerSession(input);
   * 
   *  if (result.isSuccess()) {
   *    ...
   *  }
   * </pre>
   * 
   * @param input The input parameters for updating a customer session.
   * 
   * @return a {@link Result} object with session ID if successful, or errors otherwise.
   * 
   * @throws UnexpectedException If there is an unexpected error during the process.
   */
  public Result<String> updateCustomerSession(UpdateCustomerSessionInput input) {
    return executeMutation(UPDATE_CUSTOMER_SESSION, input, "data.updateCustomerSession.sessionId");
  }

  /**
   * Retrieves customer recommendations associated with a customer session.
   *
   * Example:
   * <pre>
   * List<Recommendations> requestedRecommendations = Arrays.asList(Recommendations.PAYMENT_RECOMMENDATIONS);
   * 
   * CustomerRecommendationsInput input = CustomerRecommendationsInput
   *   .builder(
   *     sessionId,
   *     requestedRecommendations
   *   )
   *   .build()
   * 
   * Result<String> result = gateway.customer_session().getCustomerRecommendations(input);
   * 
   * if (result.isSuccess()) {
   *   CustomerRecommendationssPayload payload = result.getTarget();
   *   List<PaymentOptions> paymentOptionss =
   *     payload.getRecommendations().getPaymentOptions();
   * }
   * </pre>
   * 
   * @param input The input parameters for retrieving customer recommendations.
   * 
   * @return a {@link Result} object containing customer recommendations 
   *         if successful, or errors otherwise.
   * 
   * @throws UnexpectedException If there is an unexpected error during the process.
   */
  public Result<CustomerRecommendationsPayload> getCustomerRecommendations(
      CustomerRecommendationsInput CustomerRecommendationsInput) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("input", CustomerRecommendationsInput.toGraphQLVariables());

    try {
      Map<String, Object> response = graphQLClient.query(GET_CUSTOMER_RECOMMENDATIONS, variables);
      ValidationErrors errors = GraphQLClient.getErrors(response);
      if (errors != null) {
        return new Result<>(errors);
      }
      return new Result<>(
        extractCustomerRecommendationsPayload(response)
      );
    } catch (Throwable e) {
      throw new UnexpectedException(e.getMessage(), e);
    }
  }

  private Result<String> executeMutation(String query, Request input, String sessionIdKey) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("input", input.toGraphQLVariables());

    try {
      Map<String, Object> response = graphQLClient.query(query, variables);
      ValidationErrors errors = GraphQLClient.getErrors(response);
      if (errors != null) {
        return new Result<>(errors);
      }
      String sessionId = getValue(response, sessionIdKey);
      return new Result<>(sessionId);
    } catch (Throwable e) {
      throw new UnexpectedException(e.getMessage(), e);
    }
  }

  private static CustomerRecommendationsPayload extractCustomerRecommendationsPayload(Map<String, Object> response) {
    boolean isInPayPalNetwork = getValue(response, "data.customerRecommendations.isInPayPalNetwork");
    CustomerRecommendations recommendations =  extractRecommendations(response);
    return new CustomerRecommendationsPayload(isInPayPalNetwork, recommendations);
  }
  
  private static CustomerRecommendations extractRecommendations(Map<String, Object> response) {
    List<Map<String, Object>> paymentOptionsObjs = getValue(response, "data.customerRecommendations.recommendations.paymentOptions");

    List<PaymentOptions> paymentOptionsList = paymentOptionsObjs.stream()
    .map(
        paymentOptionsObj -> {
          Integer recommendedPriority = getValue(paymentOptionsObj, "recommendedPriority");
          String paymentOptionString = getValue(paymentOptionsObj, "paymentOption");
          RecommendedPaymentOption paymentOption = RecommendedPaymentOption
              .valueOf(paymentOptionString);
          return new PaymentOptions(paymentOption, recommendedPriority);
        })
    .collect(Collectors.toList());

    return new CustomerRecommendations(paymentOptionsList);
  }

  private static <T> T getValue(Map<String, Object> response, String key) {
    Map<String, Object> map = response;
    String[] keyParts = key.split("\\.");
    for (int k = 0; k < keyParts.length - 1 ; k++) {
      String subKey = keyParts[k];
      map = popValue(map, subKey);
    }
    String lastKey = keyParts[keyParts.length - 1];
    return popValue(map, lastKey);
  }

  private static <T> T popValue(Map<String, Object> response, String key) {
    if (!response.containsKey(key)) {
      throw new UnexpectedException("Couldn't parse response");
    }
    return (T) response.get(key);
  }
}
