package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintreegateway.Request;
import com.braintreegateway.graphql.enums.Recommendations;

/**
 * Represents the input to request PayPal customer session recommendations.
 */
public class CustomerRecommendationsInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final List<Recommendations> recommendations;
  private final CustomerSessionInput customer;

  @Override
  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("merchantAccountId", merchantAccountId);
    variables.put("sessionId", sessionId);
    variables.put("recommendations", recommendations);
    if (customer != null) {
      variables.put("customer", customer.toGraphQLVariables());
    }
    return variables;
  }

  private CustomerRecommendationsInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.recommendations = builder.recommendations;
    this.customer = builder.customer;
  }

  /**
   * Creates a builder instance for fluent construction of CustomerRecommendationsInput
   * objects.
   * 
   * @param sessionId The customer session id
   * @param recommendations  The recommendation categories to be requested
   *
   * @return CustomerRecommendationsInput.Builder
   */
  public static Builder builder(String sessionId, List<Recommendations> recommendations) {
    return new Builder(sessionId, recommendations);
  }

  /**
   * This class provides a fluent interface for constructing CustomerRecommendationsInput
   * objects.
   */
  public static class Builder {
    private String merchantAccountId;
    private String sessionId;
    private List<Recommendations> recommendations;
    private CustomerSessionInput customer;

    public Builder(String sessionId, List<Recommendations> recommendations) {
      this.sessionId = sessionId;
      this.recommendations = recommendations;
    }

    /**
     * Sets the merchant account ID.
     *
     * @param merchantAccountId The merchant account ID.
     *
     * @return this
     */
    public Builder merchantAccountId(String merchantAccountId) {
      this.merchantAccountId = merchantAccountId;
      return this;
    }

    /**
     * Sets the input object representing customer information relevant to the
     * customer session.
     *
     * @param customer The input object representing the customer information
     *                 relevant to the customer session.
     *
     * @return this
     */
    public Builder customer(CustomerSessionInput customer) {
      this.customer = customer;
      return this;
    }

    public CustomerRecommendationsInput build() {
      return new CustomerRecommendationsInput(this);
    }
  }
}
