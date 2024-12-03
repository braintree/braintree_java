package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Request;

public class UpdateCustomerSessionInput extends Request {
  private String merchantAccountId;
  private String sessionId;
  private CustomerSessionInput customer;

  public UpdateCustomerSessionInput(String sessionId) {
    this.sessionId = sessionId;
  }

  public UpdateCustomerSessionInput merchantAccountId(String merchantAccountId) {
    this.merchantAccountId = merchantAccountId;
    return this;
  }

  public UpdateCustomerSessionInput customer(CustomerSessionInput customer) {
    this.customer = customer;
    return this;
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
