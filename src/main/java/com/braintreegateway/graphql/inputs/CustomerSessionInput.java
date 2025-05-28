package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;
import com.braintreegateway.util.Experimental;

/**
 * Customer identifying information for a PayPal customer session.
 */
@Experimental("This class is experimental and may change in future releases.")
public class CustomerSessionInput extends Request {
  private final String email;
  private final String hashedEmail; 
  private final String hashedPhoneNumber; 
  private final PhoneInput phone;
  private final String deviceFingerprintId;
  private final Boolean paypalAppInstalled;
  private final Boolean venmoAppInstalled;
  private final String userAgent;

  @Override
  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */  
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    if (email != null) {
      variables.put("email", email);
    } 
    if (hashedEmail != null) {
      variables.put("hashedEmail", hashedEmail);
    } 
    if (hashedPhoneNumber != null) {
      variables.put("hashedPhoneNumber", hashedPhoneNumber);
    } 
    if (phone != null) {
      variables.put("phone", phone.toGraphQLVariables());
    }
    if (deviceFingerprintId != null) {
      variables.put("deviceFingerprintId", deviceFingerprintId);
    }
    variables.put("paypalAppInstalled", paypalAppInstalled);
    variables.put("venmoAppInstalled", venmoAppInstalled);
    if (userAgent != null) {
      variables.put("userAgent", userAgent);
    }
    return variables;
  }

  private CustomerSessionInput(Builder builder) {
    this.email = builder.email;
    this.hashedEmail = builder.hashedEmail; 
    this.hashedPhoneNumber = builder.hashedPhoneNumber; 
    this.phone = builder.phone;
    this.deviceFingerprintId = builder.deviceFingerprintId;
    this.paypalAppInstalled = builder.paypalAppInstalled;
    this.venmoAppInstalled = builder.venmoAppInstalled;
    this.userAgent = builder.userAgent;
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
    private String hashedEmail; 
    private String hashedPhoneNumber;
    private PhoneInput phone;
    private String deviceFingerprintId;
    private Boolean paypalAppInstalled;
    private Boolean venmoAppInstalled;
    private String userAgent;

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
     * Sets the hashed customer email address.
     *
     * @param hashedEmail Customer email address hashed via SHA256.
     *
     * @return this
     */
    public Builder hashedEmail(String hashedEmail) {
      this.hashedEmail = hashedEmail;
      return this;
    }

    /**
     * Sets the hashed customer phone number.
     *
     * @param hashedPhone Customer phone number hashed via SHA256.
     *
     * @return this
     */
    public Builder hashedPhoneNumber(String hashedPhoneNumber) {
      this.hashedPhoneNumber = hashedPhoneNumber;
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

    /**
     * Sets the user agent from the request originating from the customer's device.
     * This will be used to identify the customer's operating system and browser versions.
     *
     * @param userAgent The user agent
     *
     * @return this
     */
    public Builder userAgent(String userAgent) {
      this.userAgent = userAgent;
      return this;
    }
    
    public CustomerSessionInput build() {
      return new CustomerSessionInput(this);
    }
  }
}