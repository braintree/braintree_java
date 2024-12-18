package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintreegateway.Request;
import com.braintreegateway.graphql.enums.Insights;

public class CustomerInsightsInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final List<Insights> insights;
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
    variables.put("insights", insights);
    if (customer != null) {
      variables.put("customer", customer.toGraphQLVariables());
    }
    return variables;
  }

  private CustomerInsightsInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.insights = builder.insights;
    this.customer = builder.customer;
  }

  /**
   * Creates a builder instance for fluent construction of CustomerInsightsInput
   * objects.
   * 
   * @param sessionId The customer session id
   * @param insights The types of insights to be requested
   *
   * @return CustomerInsightsInput.Builder
   */
  public static Builder builder(String sessionId, List<Insights> insights) {
    return new Builder(sessionId, insights);
  }

  /**
   * This class provides a fluent interface for constructing CustomerInsightsInput
   * objects.
   */
  public static class Builder {
    private String merchantAccountId;
    private String sessionId;
    private List<Insights> insights;
    private CustomerSessionInput customer;

    public Builder(String sessionId, List<Insights> insights) {
      this.sessionId = sessionId;
      this.insights = insights;
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
     * Sets the input object representing customer information relevant to the customer session.
     *
     * @param customer The input object representing the customer information relevant to the customer session.
     *
     * @return this
     */
    public Builder customer(CustomerSessionInput customer) {
      this.customer = customer;
      return this;
    }

    public CustomerInsightsInput build() {
      return new CustomerInsightsInput(this);
    }
  }
}
