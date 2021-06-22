package com.braintreegateway.unittest;

import com.braintreegateway.FacilitatorDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FacilitatorDetailsTest {
    @Test
    public void includesFields() {
        String xml = "<facilitator-details>" +
            "<oauth-application-client-id>abc123</oauth-application-client-id>" +
            "<oauth-application-name>Fun Shop</oauth-application-name>" +
            "<source-payment-method-token>abc9xyz</source-payment-method-token>" +
            "</facilitator-details>";
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

        FacilitatorDetails details = new FacilitatorDetails(node);

        assertEquals("abc123", details.getOauthApplicationClientId());
        assertEquals("Fun Shop", details.getOauthApplicationName());
        assertEquals("abc9xyz", details.getSourcePaymentMethodToken());
    }

    @Test
    public void canOmitSourcePaymentMethodToken() {
        String xml = "<facilitator-details>" +
            "<oauth-application-client-id>abc123</oauth-application-client-id>" +
            "<oauth-application-name>Fun Shop</oauth-application-name>" +
            "</facilitator-details>";
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

        FacilitatorDetails details = new FacilitatorDetails(node);

        assertEquals(null, details.getSourcePaymentMethodToken());
    }
}
