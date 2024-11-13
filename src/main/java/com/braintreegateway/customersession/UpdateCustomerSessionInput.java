package com.braintreegateway.customersession;

import com.braintreegateway.Request;

import java.util.HashMap;
import java.util.Map;

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
