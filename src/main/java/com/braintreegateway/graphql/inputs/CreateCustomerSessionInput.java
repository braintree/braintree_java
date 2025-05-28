package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.braintreegateway.Request;
import com.braintreegateway.graphql.inputs.PayPalPurchaseUnitInput; 
import com.braintreegateway.util.Experimental;

/**
 * Represents the input to request the creation of a PayPal customer session.
 */
@Experimental("This class is experimental and may change in future releases.")
public class CreateCustomerSessionInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final String domain;
  private final CustomerSessionInput customer;
  private final List<PayPalPurchaseUnitInput> purchaseUnits;
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

    if (purchaseUnits != null && !purchaseUnits.isEmpty()) {
      List<Map<String, Object>> formattedPurchaseUnits = new ArrayList<>();
      for (PayPalPurchaseUnitInput purchaseUnit : purchaseUnits) {
        formattedPurchaseUnits.add(purchaseUnit.toGraphQLVariables());
      }
      variables.put("purchaseUnits", formattedPurchaseUnits);
    }

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
    this.purchaseUnits = builder.purchaseUnits;
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
    private List<PayPalPurchaseUnitInput> purchaseUnits;

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
     * @param sessionId The customer session identifier. Will create a new session if not set.
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
     * @param customer Customer identifying information.
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
     * @param domain The domain name of the users browser.
     *
     * @return this
     */
    public Builder domain(String domain) {
      this.domain = domain;
      return this;
    }

    /**
     * Sets the purchase units
     *
     * @param purchaseUnits Amount of the items purchased.
     *
     * @return this
     */
    public Builder purchaseUnits(List<PayPalPurchaseUnitInput> purchaseUnits) {
      this.purchaseUnits = purchaseUnits;
      return this;
    }

    public CreateCustomerSessionInput build() {
      return new CreateCustomerSessionInput(this);
    }
  }
}
