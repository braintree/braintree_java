package com.braintreegateway.unittest.customersession;

import com.braintreegateway.customersession.CustomerInsightsInput;
import com.braintreegateway.customersession.CustomerSessionInput;
import com.braintreegateway.customersession.Insights;
import com.braintreegateway.customersession.PaymentRecommendation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerInsightsInputTest {
    @Test
    void testToGraphQLVariables() {
        CustomerSessionInput customerSessionInput = new CustomerSessionInput();
        CustomerInsightsInput input = new CustomerInsightsInput(
                "session-id",
                Arrays.asList(Insights.PAYMENT_INSIGHTS)
        )
            .merchantAccountId("merchant-account-id")
            .customer(customerSessionInput);

        Map<String, Object> map = input.toGraphQLVariables();
        
        assertEquals("session-id", map.get("sessionId"));
        assertEquals(customerSessionInput.toGraphQLVariables(), map.get("customer"));
        assertEquals(Insights.PAYMENT_INSIGHTS, ((List<Insights> ) map.get("insights")).get(0) );
    }
}