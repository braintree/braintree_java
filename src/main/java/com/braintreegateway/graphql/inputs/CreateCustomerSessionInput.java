package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

/**
 * Represents the input to request the creation of a PayPal customer session.
 *
 * See our
 * {@link https://graphql.braintreepayments.com/reference/#input_object--createcustomersessioninput
 * graphql reference docs} for information on attributes
 */
public class CreateCustomerSessionInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final CustomerSessionInput customer;
  private final String domain;

  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */
  @Override
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("merchantAccountId", merchantAccountId);
    variables.put("sessionId", sessionId);
    if (customer != null) {
      variables.put("customer", customer.toGraphQLVariables());
    }
    variables.put("domain", domain);
    return variables;
  }

  private CreateCustomerSessionInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.customer = builder.customer;
    this.domain = builder.domain;
  }

  /**
   * Creates a builder instance for fluent construction of
   * CreateCustomerSessionInput objects.
   *
   * @return CreateCustomerSessionInput.Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * This class provides a fluent interface for constructing
   * CreateCustomerSessionInput objects.
   */
  public static class Builder {
    private String merchantAccountId;
    private String sessionId;
    private CustomerSessionInput customer;
    private String domain;

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
     * Sets the customer session ID.
     *
     * @param sessionId The customer session ID.
     *
     * @return this
     */
    public Builder sessionId(String sessionId) {
      this.sessionId = sessionId;
      return this;
    }

    /**
     * Sets the input object representing customer information relevant to the customer session.
     *
     * @param  customer The input object representing the customer information relevant to the customer session.
     *
     * @return this
     */
    public Builder customer(CustomerSessionInput customer) {
      this.customer = customer;
      return this;
    }

    /**
     * Sets the customer domain.
     *
     * @param domain The customer domain.
     *
     * @return this
     */
    public Builder domain(String domain) {
      this.domain = domain;
      return this;
    }

    public CreateCustomerSessionInput build() {
      return new CreateCustomerSessionInput(this);
    }
  }
}
