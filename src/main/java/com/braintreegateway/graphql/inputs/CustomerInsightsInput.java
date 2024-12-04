package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintreegateway.Request;
import com.braintreegateway.graphql.enums.Insights;

public class CustomerInsightsInput extends Request {
  private final String merchantAccountId;
  private final String sessionId;
  private final List<Insights> insights;
  private final CustomerSessionInput customer;

  @Override
  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("merchantAccountId", merchantAccountId);
    variables.put("sessionId", sessionId);
    variables.put("insights", insights);
    if (customer != null) {
      variables.put("customer", customer.toGraphQLVariables());
    }
    return variables;
  }

  public static class Builder {
    private String merchantAccountId;
    private String sessionId;
    private List<Insights> insights;
    private CustomerSessionInput customer;

    public Builder(String sessionId, List<Insights> insights) {
      this.sessionId = sessionId;
      this.insights = insights;
    }

    public Builder merchantAccountId(String merchantAccountId) {
      this.merchantAccountId = merchantAccountId;
      return this;
    }

    public Builder customer(CustomerSessionInput customer) {
      this.customer = customer;
      return this;
    }

    public CustomerInsightsInput build() {
      return new CustomerInsightsInput(this);
    }
  }

  private CustomerInsightsInput(Builder builder) {
    this.merchantAccountId = builder.merchantAccountId;
    this.sessionId = builder.sessionId;
    this.insights = builder.insights;
    this.customer = builder.customer;
  }

  public static Builder builder(String sessionId, List<Insights> insights) {
    return new Builder(sessionId, insights);
  }
}
