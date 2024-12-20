package com.braintreegateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.graphql.enums.InsightPaymentOption;
import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerInsightsInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;
import com.braintreegateway.graphql.types.CustomerInsightsPayload;
import com.braintreegateway.graphql.types.PaymentRecommendation;
import com.braintreegateway.graphql.unions.CustomerInsights;
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

  private static final String GET_CUSTOMER_INSIGHTS = "query CustomerInsights($input: CustomerInsightsInput!) {\n"
      + "            customerInsights(input: $input) {\n"
      + "              isInPayPalNetwork\n"
      + "              insights {\n"
      + "                ... on PaymentInsights {\n"
      + "                  paymentRecommendations {\n"
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
   * id. If a session id is not provided in the input, one will be generated by
   * the gateway. Attempt to create a duplicate session will result in an error.
   * Customer specific information such as email, phone number and device
   * information can be optionally included in the input object.
   * 
   * See our
   * {@link https://graphql.braintreepayments.com/reference/#input_object--createcustomersessioninput
   * graphql reference docs} for information on the input attributes.
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
   * @return a {@link Result} object with session id and a success flag if
   *         successful, or errors otherwise.
   * 
   * @throws UnexpectedException If there is an unexpected error during the
   *                             process.
   */
  public Result<String> createCustomerSession(CreateCustomerSessionInput input) {
    return executeMutation(CREATE_CUSTOMER_SESSION, input, "createCustomerSession", "sessionId");
  }

  /**
   * Updates an existing customer session.
   *
   * See our
   * {@link https://graphql.braintreepayments.com/reference/#input_object--updatecustomersessioninput
   * graphql reference docs} for information on the input attributes.
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
   * @return a {@link Result} object with session id and a success flag if
   *         successful, or errors otherwise.
   * 
   * @throws UnexpectedException If there is an unexpected error during the
   *                             process.
   */
  public Result<String> updateCustomerSession(UpdateCustomerSessionInput input) {
    return executeMutation(UPDATE_CUSTOMER_SESSION, input, "updateCustomerSession", "sessionId");
  }

  /**
   * Retrieves customer insights associated with a customer session.
   *
   * See our graphql reference docs for information on the {@link
   * https://graphql.braintreepayments.com/reference/#input_object--customerinsightsinput
   * input} and {@link
   * #object--customerinsightspayload output} attributes.
   * 
   * Example:
   * <pre>
   * List<Insights> requestedInsights = Arrays.asList(Insights.PAYMENT_INSIGHTS);
   * 
   * CustomerInsightsInput input = CustomerInsightsInput
   * .builder(
   * sessionId,
   * requestedInsights
   * )
   * .build()
   * 
   * Result<String> result =
   * gateway.customer_session().getCustomerInsights(input);
   * 
   * if (result.isSuccess()) {
   * CustomerInsightsPayload payload = result.getTarget();
   * List<PaymentRecommendation> paymentRecommendations =
   * payload.getInsights().getPaymentRecommendations();
   * }
   * </pre>
   * 
   * @param input The input parameters for retrieving customer insights.
   * 
   * @return a {@link Result} object with containing a CustomerInsightsPayload and
   * a success flag if successful, or errors otherwise.
   * 
   * @throws UnexpectedException If there is an unexpected error during the
   * process.
   */
  public Result<CustomerInsightsPayload> getCustomerInsights(
      CustomerInsightsInput customerInsightsInput) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("input", customerInsightsInput.toGraphQLVariables());

    try {
      Map<String, Object> response = graphQLClient.query(GET_CUSTOMER_INSIGHTS, variables);
      ValidationErrors errors = GraphQLClient.getErrors(response);
      if (errors != null) {
        return new Result<>(errors);
      }
      Map<String, Object> data = getOrThrow(response, "data");
      return new Result<>(extractCustomerInsightsPayload(data));
    } catch (Throwable e) {
      throw new UnexpectedException(e.getMessage(), e);
    }
  }

  private Result<String> executeMutation(String query, Request input, String operationName, String targetName) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("input", input.toGraphQLVariables());

    try {
      Map<String, Object> response = graphQLClient.query(query, variables);
      ValidationErrors errors = GraphQLClient.getErrors(response);
      if (errors != null) {
        return new Result<>(errors);
      }
      Map<String, Object> data = getOrThrow(response, "data");
      Map<String, Object> result = getOrThrow(data, operationName);
      String sessionId = getOrThrow(result, targetName);
      return new Result<>(sessionId);
    } catch (Throwable e) {
      throw new UnexpectedException(e.getMessage(), e);
    }
  }

  private static <T> T getOrThrow(Map<String, Object> response, String key) {
    if (!response.containsKey(key)) {
      throw new UnexpectedException("Couldn't parse response");
    }
    return (T) response.get(key);
  }

  private static CustomerInsightsPayload extractCustomerInsightsPayload(Map<String, Object> data) {
    Map<String, Object> customerInsightsMap = getOrThrow(data, "customerInsights");
    boolean isInPayPalNetwork = getOrThrow(customerInsightsMap, "isInPayPalNetwork");
    Map<String, Object> insightsMap = getOrThrow(customerInsightsMap, "insights");
    CustomerInsights insights = new CustomerInsights(
        getPaymentRecommendations(insightsMap));

    return new CustomerInsightsPayload(isInPayPalNetwork, insights);
  }

  private static List<PaymentRecommendation> getPaymentRecommendations(
      Map<String, Object> insightObj) {
    List<Map<String, Object>> recommendationObjs = getOrThrow(insightObj, "paymentRecommendations");

    return recommendationObjs.stream()
        .map(
            recommendationObj -> {
              Integer recommendedPriority = getOrThrow(recommendationObj, "recommendedPriority");
              String paymentOptionString = getOrThrow(recommendationObj, "paymentOption");
              InsightPaymentOption paymentOption = InsightPaymentOption
                  .valueOf(paymentOptionString);
              return new PaymentRecommendation(paymentOption, recommendedPriority);
            })
        .collect(Collectors.toList());
  }
}
