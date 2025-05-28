package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.braintreegateway.Request;
import com.braintreegateway.graphql.inputs.PayPalPurchaseUnitInput; 
import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.graphql.enums.Recommendations;
import com.braintreegateway.util.Experimental;

/**
 * Represents the input to request PayPal customer session recommendations.
 * 
 */
@Experimental("This class is experimental and may change in future releases.")
public class CustomerRecommendationsInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final String domain; 
  private final List<PayPalPurchaseUnitInput> purchaseUnits;
  private final CustomerSessionInput customer;

  @Override
  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    if (merchantAccountId != null) {
      variables.put("merchantAccountId", merchantAccountId);
    }
    if (sessionId != null) {
      variables.put("sessionId", sessionId);
    } 
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
    if (domain != null) {
      variables.put("domain", domain);
    } 

    return variables;
  }

  private CustomerRecommendationsInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.domain = builder.domain; 
    this.purchaseUnits = builder.purchaseUnits;
    this.customer = builder.customer;
  }

  /**
   * Creates a builder instance for fluent construction of CustomerRecommendationsInput
   * objects.
   * 
   *
   * @return CustomerRecommendationsInput.Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * This class provides a fluent interface for constructing CustomerRecommendationsInput
   * objects.
   */
  public static class Builder {
    private String merchantAccountId;
    private String sessionId; 
    private String domain; 
    private List<PayPalPurchaseUnitInput> purchaseUnits;
    private CustomerSessionInput customer;

    public Builder() {
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
     * Sets the session Id.
     *
     * @param sessionId The customer session identifier.
     *
     * @return this
     */
    public Builder sessionId(String sessionId) {
      this.sessionId = sessionId;
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

     /**
     * Sets the customer domain 
     * 
     * @param domain sets the domain of the customer
     * 
     *
     * @return this
     */
    public Builder domain(String domain) {
      this.domain = domain;
      return this;
    }

    /**
     * Sets the amount of items purchased
     *
     * @param purchaseUnits Amount of the items purchased.
     *
     * @return this
     */
    public Builder purchaseUnits(List<PayPalPurchaseUnitInput> purchaseUnits) {
      this.purchaseUnits = purchaseUnits;
      return this;
    } 

    public CustomerRecommendationsInput build() {
      return new CustomerRecommendationsInput(this);
    }
  }
}