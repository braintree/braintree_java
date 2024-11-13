package com.braintreegateway.customersession;

import com.braintreegateway.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerInsightsInput extends Request {
    private String merchantAccountId;
    private String sessionId;
    private List<Insights> insights;
    private CustomerSessionInput customer;

    public CustomerInsightsInput(String sessionId, List<Insights> insights) {
        this.sessionId = sessionId;
        this.insights = insights;
    }
    
    public CustomerInsightsInput merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    public CustomerInsightsInput customer(CustomerSessionInput customer) {
        this.customer = customer;
        return this;
    }

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
}
