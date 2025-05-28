package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.MonetaryAmountInput; 
import com.braintreegateway.graphql.inputs.PayPalPayeeInput; 
import com.braintreegateway.Request;
import com.braintreegateway.util.Experimental;

@Experimental("This class is experimental and may change in future releases.")
public class PayPalPurchaseUnitInput extends Request{
    private final MonetaryAmountInput amount;  
    private final PayPalPayeeInput payee;  

  @Override
  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("amount", amount.toGraphQLVariables()); 
    if(payee!=null) {
      variables.put("payee", payee.toGraphQLVariables());
   }
    return variables;
  }

  private PayPalPurchaseUnitInput(Builder builder) {
    this.amount = builder.amount;
    this.payee = builder.payee;
  }

  /**
   * Creates a builder instance for fluent construction of PayPalPurchaseUnit
   * objects.
   * 
   * @param amount The total order amount. The amount must be a positive number.
   *
   * @return PayPalPurchaseUnitInput.Builder
   */
  public static Builder builder(MonetaryAmountInput amount) {
    return new Builder(amount);
  }

/**
 * This class provides a fluent interface for constructing PayPalPurchaseUnitInput objects.
 */
public static class Builder {
    private MonetaryAmountInput amount; 
    private PayPalPayeeInput payee; 

    public Builder(MonetaryAmountInput amount) {
      this.amount = amount;
    }

    /**
     * Sets the payee.
     *
     * @param payee The details for the merchant who receives the funds 
     * and fulfills the order. The merchant is also known as the payee.
     *
     * @return this
     */
    public Builder payee(PayPalPayeeInput payee) {
      this.payee = payee;
      return this;
    }

    public PayPalPurchaseUnitInput build() {
      return new PayPalPurchaseUnitInput(this);
    }
  }
    
}
