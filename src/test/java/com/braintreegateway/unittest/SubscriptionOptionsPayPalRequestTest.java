package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.SubscriptionOptionsPayPalRequest;
import com.braintreegateway.SubscriptionOptionsRequest;
import com.braintreegateway.SubscriptionRequest;

public class SubscriptionOptionsPayPalRequestTest {

    @Test
    public void buildsXmlAndChainsBackToParent() {
        SubscriptionOptionsRequest parent = new SubscriptionOptionsRequest(new SubscriptionRequest());
        SubscriptionOptionsPayPalRequest request = new SubscriptionOptionsPayPalRequest(parent);

        SubscriptionOptionsRequest returned = request
                .description("a-description")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<paypal>"), xml);
        assertTrue(xml.contains("<description>a-description</description>"), xml);
    }
}
