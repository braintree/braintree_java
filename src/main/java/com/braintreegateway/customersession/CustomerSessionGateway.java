package com.braintreegateway.customersession;

import java.util.HashMap;
import java.util.Map;
import com.braintreegateway.Result;
import com.braintreegateway.ValidationErrors;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.GraphQLClient;

import java.util.Map;

public class CustomerSessionGateway {

    private static final String CREATE_CUSTOMER_SESSION_MUTATION =
            "mutation CreateCustomerSession($input: CreateCustomerSessionInput!) { "
                    + "createCustomerSession(input: $input) {"
                    + "    sessionId"
                    + "  }"
                    + "}";

    private static final String UPDATE_CUSTOMER_SESSION_MUTATION =
            "mutation UpdateCustomerSession($input: UpdateCustomerSessionInput!) { "
                    + "updateCustomerSession(input: $input) {"
                    + "    sessionId"
                    + "  }"
                    + "}";

    private static final String GET_CUSTOMER_INSIGHTS_QUERY =
            "query CustomerInsights($input: CustomerInsightsInput!) {\n" +
            "            customerInsights(input: $input) {\n" +
            "              isInPayPalNetwork\n" +
            "              insights {\n" +
            "                ... on PaymentInsights {\n" +
            "                  paymentRecommendations {\n" +
            "                    paymentOption\n" +
            "                    recommendedPriority\n" +
            "                  }\n" +
            "                }\n" +
            "              }\n" +
            "            }\n" +
            "          }";

    private final GraphQLClient graphQLClient;

    public CustomerSessionGateway(GraphQLClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }

    public Result<String> createCustomerSession(CreateCustomerSessionInput createCustomerSessionInput) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("input", createCustomerSessionInput.toGraphQLVariables());
        Map<String, Object> response;
        try {
            response = graphQLClient.query(CREATE_CUSTOMER_SESSION_MUTATION, variables);
        } catch (Exception e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
        ValidationErrors errors = GraphQLClient.getErrors(response);
        if (errors != null) {
            return new Result<>(errors);
        }
        try {
            Map<String, Object> data = (Map) response.get("data");
            Map<String, Object> result = (Map) data.get("createCustomerSession");
            String customerSessionId = (String) result.get("sessionId");
            return new Result<>(customerSessionId);
        } catch (Exception e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    public Result<String>  updateCustomerSession(UpdateCustomerSessionInput updateCustomerSessionInput) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("input", updateCustomerSessionInput.toGraphQLVariables());
        Map<String, Object> response;
        try {
            response = graphQLClient.query(UPDATE_CUSTOMER_SESSION_MUTATION, variables);
        } catch (Exception e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        ValidationErrors errors = GraphQLClient.getErrors(response);
        if (errors != null) {
            return new Result<>(errors);
        }
        try {
            Map<String, Object> data = (Map) response.get("data");
            Map<String, Object> result = (Map) data.get("updateCustomerSession");
            String customerSessionId = (String) result.get("sessionId");
            return new Result<>(customerSessionId);
        } catch (Exception e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    public Result<CustomerInsightsPayload> getCustomerInsights(CustomerInsightsInput customerInsightsInput) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("input", customerInsightsInput.toGraphQLVariables());
        Map<String, Object> response;
        try {
            response = graphQLClient.query(GET_CUSTOMER_INSIGHTS_QUERY, variables);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnexpectedException(e.getMessage(), e);
        }
        ValidationErrors errors = GraphQLClient.getErrors(response);
        if (errors != null) {
            return new Result<>(errors);
        }
        try {
            Map<String, Object> data = (Map) response.get("data");
            return new Result<>(new CustomerInsightsPayload(data));
        } catch (Exception e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }
}
