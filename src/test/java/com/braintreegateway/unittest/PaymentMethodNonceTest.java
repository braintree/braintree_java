package com.braintreegateway.unittest;

import com.braintreegateway.PaymentMethodNonce;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PaymentMethodNonceTest {

    @Test
    public void parsesNodeCorrectlyWithDetailsMissing() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>CreditCard</type>" +
                "  <nonce>fake-valid-nonce</nonce>" +
                "  <description>ending in 22</description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <three-d-secure-info nil=\"true\"/>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("CreditCard", paymentMethodNonce.getType());
        assertEquals("fake-valid-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNull(paymentMethodNonce.getThreeDSecureInfo());
        assertNull(paymentMethodNonce.getDetails());
    }

    @Test
    public void ParsesNodeCorrectlyWithNilValues() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>CreditCard</type>" +
                "  <nonce>fake-valid-nonce</nonce>" +
                "  <description>ending in 22</description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <three-d-secure-info nil=\"true\"/>" +
                "  <details nil=\"true\"/>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("CreditCard", paymentMethodNonce.getType());
        assertEquals("fake-valid-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNull(paymentMethodNonce.getThreeDSecureInfo());
        assertNull(paymentMethodNonce.getDetails());
    }
}
