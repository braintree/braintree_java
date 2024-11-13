package com.braintreegateway.customersession;

import com.braintreegateway.Request;

import java.util.HashMap;
import java.util.Map;

public class CreateCustomerSessionInput extends Request {
    private String merchantAccountId;
    private String sessionId;
    private CustomerSessionInput customer;
    private String domain;

    public CreateCustomerSessionInput merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    public CreateCustomerSessionInput sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public CreateCustomerSessionInput customer(CustomerSessionInput customer) {
        this.customer = customer;
        return this;
    }

    public CreateCustomerSessionInput domain(String value) {
        this.domain = value;
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
        variables.put("domain", domain);
        return variables;
    }
}
