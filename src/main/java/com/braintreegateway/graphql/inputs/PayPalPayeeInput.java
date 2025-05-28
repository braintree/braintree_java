package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;
import com.braintreegateway.util.Experimental;

/**
 * Input for the PayPal merchant
 */
@Experimental("This class is experimental and may change in future releases.")
public class PayPalPayeeInput extends Request {
    private final String clientId; 
    private final String emailAddress;

  @Override
  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("clientId", clientId);
    variables.put("emailAddress", emailAddress);
    return variables;
  }

  private PayPalPayeeInput(Builder builder) {
    this.clientId = builder.clientId;
    this.emailAddress = builder.emailAddress;
  }

  /**
   * Creates a builder instance for fluent construction of PayPalPayee
   * objects.
   *
   * @return PayPalPayeeInput.Builder
   */
  public static Builder builder() {
    return new Builder();
  }

/**
 * This class provides a fluent interface for constructing PayPalPayeeInput objects.
 */
public static class Builder {
    private String clientId;
    private String emailAddress;

    /**
     * Sets the clientId.
     *
     * @param clientId The public ID for the payee- or merchant-created app. 
     * Introduced to support use cases, such as BrainTree  integration with PayPal, 
     * where payee 'emailAddress' or 'merchantId' is not available..
     *
     * @return this
     */
    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    /**
     * Sets the email address.
     *
     * @param emailAddress The email address of the merchant.
     *
     * @return this
     */
    public Builder emailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
      return this;
    }

    public PayPalPayeeInput build() {
      return new PayPalPayeeInput(this);
    }
  }
}
