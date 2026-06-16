package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AuthenticationInsightOptionsRequest;
import com.braintreegateway.PaymentMethodNonceRequest;

public class AuthenticationInsightOptionsRequestTest {

    @Test
    public void buildsXmlAndQueryStringAndChainsBackToParent() {
        PaymentMethodNonceRequest parent = new PaymentMethodNonceRequest();
        AuthenticationInsightOptionsRequest request = new AuthenticationInsightOptionsRequest(parent);

        PaymentMethodNonceRequest returned = request
                .amount(new BigDecimal("10.00"))
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<authenticationInsightOptions>"), xml);
        assertTrue(xml.contains("<amount>10.00</amount>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("authentication_insight_options%5Bamount%5D=10.00"), queryString);
    }
}
