package com.braintreegateway.integrationtest;

import java.util.Map;

import com.braintreegateway.Configuration;
import com.braintreegateway.util.GraphQLClient;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings("deprecation")
public class GraphQLClientIT extends IntegrationTest {

    public String pingQuery =
        "query {" +
        "    ping" +
        "}";

    @Test
    public void smokeTestQueryWithoutVariables() {
        Configuration configuration = gateway.getConfiguration();

        Map<String, Object> result = new GraphQLClient(configuration).query(pingQuery);
        assertNotNull(result);
    }
}
