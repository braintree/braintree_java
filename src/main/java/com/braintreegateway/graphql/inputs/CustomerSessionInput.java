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

  public static class Builder {
    private String email;
    private PhoneInput phone;
    private String deviceFingerprintId;
    private Boolean paypalAppInstalled;
    private Boolean venmoAppInstalled;

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder phone(PhoneInput phone) {
      this.phone = phone;
      return this;
    }

    public Builder deviceFingerprintId(String deviceFingerprintId) {
      this.deviceFingerprintId = deviceFingerprintId;
      return this;
    }

    public Builder paypalAppInstalled(Boolean paypalAppInstalled) {
      this.paypalAppInstalled = paypalAppInstalled;
      return this;
    }

    public Builder venmoAppInstalled(Boolean venmoAppInstalled) {
      this.venmoAppInstalled = venmoAppInstalled;
      return this;
    }

    public CustomerSessionInput build() {
      return new CustomerSessionInput(this);
    }
  }

  private CustomerSessionInput(Builder builder) {
    this.email = builder.email;
    this.phone = builder.phone;
    this.deviceFingerprintId = builder.deviceFingerprintId;
    this.paypalAppInstalled = builder.paypalAppInstalled;
    this.venmoAppInstalled = builder.venmoAppInstalled;
  }

  public static Builder builder() {
    return new Builder();
  }
}
