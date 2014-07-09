package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class PaymentMethodGatewayTest {
    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration_merchant_id",
            "integration_public_key",
            "integration_private_key"
        );
    }

    @Test
    public void parseResultReturnsUnknownPaymentMethodInElseCase() {
        String xml = "<monopoly-money><token>foo</token></monopoly-money>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof UnknownPaymentMethod);
    }
}
