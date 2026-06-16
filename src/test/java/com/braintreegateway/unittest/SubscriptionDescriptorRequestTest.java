package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.SubscriptionDescriptorRequest;
import com.braintreegateway.SubscriptionRequest;

public class SubscriptionDescriptorRequestTest {

    @Test
    public void buildsXmlAndChainsBackToParent() {
        SubscriptionRequest parent = new SubscriptionRequest();
        SubscriptionDescriptorRequest request = new SubscriptionDescriptorRequest(parent);

        SubscriptionRequest returned = request
                .name("company*product")
                .phone("1234567890")
                .url("example.com")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<descriptor>"), xml);
        assertTrue(xml.contains("<name>company*product</name>"), xml);
        assertTrue(xml.contains("<phone>1234567890</phone>"), xml);
        assertTrue(xml.contains("<url>example.com</url>"), xml);
    }
}
