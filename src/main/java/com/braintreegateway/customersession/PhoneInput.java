package com.braintreegateway.customersession;

import com.braintreegateway.Request;

import java.util.HashMap;
import java.util.Map;

public class PhoneInput extends Request {
  private String countryPhoneCode;
  private String phoneNumber;
  private String extensionNumber;

  public PhoneInput countryPhoneCode(String countryPhoneCode) {
    this.countryPhoneCode = countryPhoneCode;
    return this;
  }

  public PhoneInput phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public PhoneInput extensionNumber(String extensionNumber) {
    this.extensionNumber = extensionNumber;
    return this;
  }

  @Override
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("countryPhoneCode", countryPhoneCode);
    variables.put("phoneNumber", phoneNumber);
    variables.put("extensionNumber", extensionNumber);
    return variables;
  }
}
