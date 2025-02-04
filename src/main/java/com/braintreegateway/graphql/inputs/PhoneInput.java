package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

/**
 * Phone number input for PayPal customer session.
 */
public class PhoneInput extends Request {
  private final String countryPhoneCode;
  private final String phoneNumber;
  private final String extensionNumber;

  @Override
  /**
   * 
   * @return A map representing the input object, to pass as variables to a
   *         GraphQL mutation
   */
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("countryPhoneCode", countryPhoneCode);
    variables.put("phoneNumber", phoneNumber);
    variables.put("extensionNumber", extensionNumber);
    return variables;
  }

  private PhoneInput(Builder builder) {
    this.countryPhoneCode = builder.countryPhoneCode;
    this.phoneNumber = builder.phoneNumber;
    this.extensionNumber = builder.extensionNumber;
  }

  /**
   * Creates a builder instance for fluent construction of PhoneInput
   * objects.
   *
   * @return PhoneInput.Builder
   */
  public static Builder builder() {
    return new Builder();
  }

/**
 * This class provides a fluent interface for constructing PhoneInput objects.
 */
public static class Builder {
    private String countryPhoneCode;
    private String phoneNumber;
    private String extensionNumber;

    /**
     * Sets the country phone code.
     *
     * @param countryPhoneCode The country phone code.
     *
     * @return this
     */
    public Builder countryPhoneCode(String countryPhoneCode) {
      this.countryPhoneCode = countryPhoneCode;
      return this;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber The phone number.
     *
     * @return this
     */
    public Builder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    /**
     * Sets the extension number.
     *
     * @param extensionNumber The extension number.
     *
     * @return this
     */
    public Builder extensionNumber(String extensionNumber) {
      this.extensionNumber = extensionNumber;
      return this;
    }

    public PhoneInput build() {
      return new PhoneInput(this);
    }
  }
}
