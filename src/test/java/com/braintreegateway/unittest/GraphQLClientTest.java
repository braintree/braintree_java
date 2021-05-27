package com.braintreegateway.unittest;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.util.GraphQLClient;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphQLClientTest {

    @Test
    public void formatGraphQLRequestReturnsJsonString() {
        String query = "query string";
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("key", "value");

        String json = GraphQLClient.formatGraphQLRequest(query, variables);

        assertEquals("{\"query\":\"query string\",\"variables\":{\"key\":\"value\"}}", json);
    }

    @Test
    public void formatGraphQLRequestWithoutVariablesMapReturnsJsonString() {
        String query = "query string";
        Map<String, Object> variables = null;

        String json = GraphQLClient.formatGraphQLRequest(query, variables);

        assertEquals("{\"query\":\"query string\"}", json);
    }

    @Test
    public void formatGraphQLRequestWithoutQueryReturnsJsonString() {
        String query = null;
        Map<String, Object> variables = null;

        String json = GraphQLClient.formatGraphQLRequest(query, variables);

        assertEquals("{}", json);
    }

}
