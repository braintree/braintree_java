package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

public class CreateCustomerSessionInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final CustomerSessionInput customer;
  private final String domain;

  @Override
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("merchantAccountId", merchantAccountId);
    variables.put("sessionId", sessionId);
    if (customer != null) {
      variables.put("customer", customer.toGraphQLVariables());
    }
    variables.put("domain", domain);
    return variables;
  }

  public static class Builder {
    private String merchantAccountId;
    private String sessionId;
    private CustomerSessionInput customer;
    private String domain;

    public Builder merchantAccountId(String merchantAccountId) {
      this.merchantAccountId = merchantAccountId;
      return this;
    }

    public Builder sessionId(String sessionId) {
      this.sessionId = sessionId;
      return this;
    }

    public Builder customer(CustomerSessionInput customer) {
      this.customer = customer;
      return this;
    }

    public Builder domain(String domain) {
      this.domain = domain;
      return this;
    }

    public CreateCustomerSessionInput build() {
      return new CreateCustomerSessionInput(this);
    }
  }

  private CreateCustomerSessionInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.customer = builder.customer;
    this.domain = builder.domain;
  }

  public static Builder builder() {
    return new Builder();
  }
}
