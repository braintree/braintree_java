package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

public class CustomerSessionInput extends Request {
  private final String email;
  private final PhoneInput phone;
  private final String deviceFingerprintId;
  private final Boolean paypalAppInstalled;
  private final Boolean venmoAppInstalled;

  @Override
  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */

  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("email", email);
    if (phone != null) {
      variables.put("phone", phone.toGraphQLVariables());
    }
    variables.put("deviceFingerprintId", deviceFingerprintId);
    variables.put("paypalAppInstalled", paypalAppInstalled);
    variables.put("venmoAppInstalled", venmoAppInstalled);
    return variables;
  }

  private CustomerSessionInput(Builder builder) {
    this.email = builder.email;
    this.phone = builder.phone;
    this.deviceFingerprintId = builder.deviceFingerprintId;
    this.paypalAppInstalled = builder.paypalAppInstalled;
    this.venmoAppInstalled = builder.venmoAppInstalled;
  }

  /**
   * Creates a builder instance for fluent construction of CustomerSessionInput
   * objects.
   *
   * @return CustomerSessionInput.Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * This class provides a fluent interface for constructing CustomerSessionInput
   * objects.
   */
  public static class Builder {
    private String email;
    private PhoneInput phone;
    private String deviceFingerprintId;
    private Boolean paypalAppInstalled;
    private Boolean venmoAppInstalled;

    /**
     * Sets the customer email address.
     *
     * @param email The customer email address.
     *
     * @return this
     */
    public Builder email(String email) {
      this.email = email;
      return this;
    }

    /**
     * Sets the customer phone number input object.
     *
     * @param phone The input object representing the customer phone number.
     *
     * @return this
     */
    public Builder phone(PhoneInput phone) {
      this.phone = phone;
      return this;
    }

    /**
     * Sets the customer device fingerprint ID.
     *
     * @param deviceFingerprintId The customer device fingerprint ID.
     *
     * @return this
     */
    public Builder deviceFingerprintId(String deviceFingerprintId) {
      this.deviceFingerprintId = deviceFingerprintId;
      return this;
    }

    /**
     * Sets whether the PayPal app is installed on the customer's device.
     *
     * @param paypalAppInstalled True if the PayPal app is installed, false otherwise.
     *
     * @return this
     */
    public Builder paypalAppInstalled(Boolean paypalAppInstalled) {
      this.paypalAppInstalled = paypalAppInstalled;
      return this;
    }

    /**
     * Sets whether the Venmo app is installed on the customer's device.
     *
     * @param venmoAppInstalled True if the Venmo app is installed, false otherwise.
     *
     * @return this
     */
    public Builder venmoAppInstalled(Boolean venmoAppInstalled) {
      this.venmoAppInstalled = venmoAppInstalled;
      return this;
    }

    public CustomerSessionInput build() {
      return new CustomerSessionInput(this);
    }
  }
}
