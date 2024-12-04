package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

public class UpdateCustomerSessionInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final CustomerSessionInput customer;

  public static class Builder {
    private String merchantAccountId;
    private String sessionId;
    private CustomerSessionInput customer;

    public Builder(String sessionId) {
      this.sessionId = sessionId;
    }

    public Builder merchantAccountId(String merchantAccountId) {
      this.merchantAccountId = merchantAccountId;
      return this;
    }

    public Builder customer(CustomerSessionInput customer) {
      this.customer = customer;
      return this;
    }

    public UpdateCustomerSessionInput build() {
      return new UpdateCustomerSessionInput(this);
    }
  }

  private UpdateCustomerSessionInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.customer = builder.customer;
  }

  public static Builder builder(String sessionId) {
    return new Builder(sessionId);
  }

  @Override
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("merchantAccountId", merchantAccountId);
    variables.put("sessionId", sessionId);
    if (customer != null) {
      variables.put("customer", customer.toGraphQLVariables());
    }
    return variables;
  }
}
