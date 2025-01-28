package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

/**
 * Represents the input to request an update to a PayPal customer session.
 */
public class UpdateCustomerSessionInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final CustomerSessionInput customer;

  private UpdateCustomerSessionInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.customer = builder.customer;
  }

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
    if (customer != null) {
      variables.put("customer", customer.toGraphQLVariables());
    }
    return variables;
  }

  /**
   * Creates a builder instance for fluent construction of
   * UpdateCustomerSessionInput
   * objects.
   * 
   * @param sessionId ID of the customer session to be updated.
   * 
   * @return UpdateCustomerSessionInput.Builder
   */
  public static Builder builder(String sessionId) {
    return new Builder(sessionId);
  }

  /**
   * This class provides a fluent interface for constructing
   * UpdateCustomerSessionInput objects.
   */
  public static class Builder {
    private String merchantAccountId;
    private String sessionId;
    private CustomerSessionInput customer;

    public Builder(String sessionId) {
      this.sessionId = sessionId;
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
     *                 relevant to the
     *                 customer session.
     *
     * @return this
     */
    public Builder customer(CustomerSessionInput customer) {
      this.customer = customer;
      return this;
    }

    public UpdateCustomerSessionInput build() {
      return new UpdateCustomerSessionInput(this);
    }
  }
}
