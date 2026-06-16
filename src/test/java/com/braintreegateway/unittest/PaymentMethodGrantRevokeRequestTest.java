package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PaymentMethodGrantRevokeRequest;

public class PaymentMethodGrantRevokeRequestTest {

    // sharedPaymentMethodToken() is protected — subclass to access it.
    private static class TestableRequest extends PaymentMethodGrantRevokeRequest {
        public TestableRequest token(String token) {
            sharedPaymentMethodToken(token);
            return this;
        }
    }

    @Test
    public void buildsXmlWithSharedPaymentMethodToken() {
        String xml = new TestableRequest().token("a-shared-token").toXML();

        assertTrue(xml.contains("<payment-method>"), xml);
        assertTrue(xml.contains("<shared-payment-method-token>a-shared-token</shared-payment-method-token>"), xml);
    }
}
