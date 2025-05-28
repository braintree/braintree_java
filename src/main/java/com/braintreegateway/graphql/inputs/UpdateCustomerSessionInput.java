package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.Request;
import com.braintreegateway.graphql.inputs.PayPalPurchaseUnitInput; 
import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.util.Experimental;

/**
 * Represents the input to request an update to a PayPal customer session.
 */
@Experimental("This class is experimental and may change in future releases.")
public class UpdateCustomerSessionInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final CustomerSessionInput customer;
  private final List<PayPalPurchaseUnitInput> purchaseUnits;

  private UpdateCustomerSessionInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.customer = builder.customer;
    this.purchaseUnits = builder.purchaseUnits;
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
    if (purchaseUnits != null && !purchaseUnits.isEmpty()) {
      List<Map<String, Object>> formattedPurchaseUnits = new ArrayList<>();
      for (PayPalPurchaseUnitInput purchaseUnit : purchaseUnits) {
        formattedPurchaseUnits.add(purchaseUnit.toGraphQLVariables());
      }
      variables.put("purchaseUnits", formattedPurchaseUnits);
    }
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
    private List<PayPalPurchaseUnitInput> purchaseUnits;

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

    /**
     * Sets the amount of items purchased
     *
     * @param purchaseUnits Array of purchase unit. Each includes required 
     * information for the payment contract.
     *
     * @return this
     */
    public Builder purchaseUnits(List<PayPalPurchaseUnitInput> purchaseUnits) {
      this.purchaseUnits = purchaseUnits;
      return this;
    }

    public UpdateCustomerSessionInput build() {
      return new UpdateCustomerSessionInput(this);
    }
  }
}
