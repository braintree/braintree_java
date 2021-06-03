package com.braintreegateway.integrationtest;

import java.util.Map;

import com.braintreegateway.Configuration;
import com.braintreegateway.util.GraphQLClient;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphQLClientIT extends IntegrationTestNew {

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
