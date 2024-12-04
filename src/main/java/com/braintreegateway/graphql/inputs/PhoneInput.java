package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

public class PhoneInput extends Request {
  private final String countryPhoneCode;
  private final String phoneNumber;
  private final String extensionNumber;

  @Override
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("countryPhoneCode", countryPhoneCode);
    variables.put("phoneNumber", phoneNumber);
    variables.put("extensionNumber", extensionNumber);
    return variables;
  }

  public static class Builder {
    private String countryPhoneCode;
    private String phoneNumber;
    private String extensionNumber;

    public Builder countryPhoneCode(String countryPhoneCode) {
      this.countryPhoneCode = countryPhoneCode;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public Builder extensionNumber(String extensionNumber) {
      this.extensionNumber = extensionNumber;
      return this;
    }

    public PhoneInput build() {
      return new PhoneInput(this);
    }
  }

  private PhoneInput(Builder builder) {
    this.countryPhoneCode = builder.countryPhoneCode;
    this.phoneNumber = builder.phoneNumber;
    this.extensionNumber = builder.extensionNumber;
  }

  public static Builder builder() {
    return new Builder();
  }
}
