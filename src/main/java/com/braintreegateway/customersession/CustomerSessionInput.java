package com.braintreegateway.customersession;

import com.braintreegateway.Request;

import java.util.HashMap;
import java.util.Map;

public class CustomerSessionInput extends Request {
  private String email;
  private PhoneInput phone;
  private String deviceFingerprintId;
  private Boolean paypalAppInstalled;
  private Boolean venmoAppInstalled;

  public CustomerSessionInput email(String email) {
    this.email = email;
    return this;
  }

  public CustomerSessionInput phone(PhoneInput phone) {
    this.phone = phone;
    return this;
  }

  public CustomerSessionInput deviceFingerprintId(String deviceFingerprintId) {
    this.deviceFingerprintId = deviceFingerprintId;
    return this;
  }

  public CustomerSessionInput paypalAppInstalled(Boolean value) {
    this.paypalAppInstalled = value;
    return this;
  }

  public CustomerSessionInput venmoAppInstalled(Boolean value) {
    this.venmoAppInstalled = value;
    return this;
  }

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
}
