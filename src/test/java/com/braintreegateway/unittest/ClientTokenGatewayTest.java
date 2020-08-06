package com.braintreegateway.unittest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenOptionsRequest;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Environment;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class ClientTokenGatewayTest {
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
    public void generateRaisesExceptionIfVerifyCardIsIncludedWithoutCustomerId() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .options(new ClientTokenOptionsRequest().verifyCard(true));
        try {
            gateway.clientToken().generate(clientTokenRequest);
            fail("generate() should have raised an exception!");
        } catch (IllegalArgumentException e) {
            assertTrue("exception message should mention verifyCard",
                    e.getMessage().contains("verifyCard"));
        }
    }

    @Test
    public void generateRaisesExceptionIfMakeDefaultIsIncludedWithoutCustomerId() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .options(new ClientTokenOptionsRequest().makeDefault(true));
        try {
            gateway.clientToken().generate(clientTokenRequest);
            fail("generate() should have raised an exception!");
        } catch (IllegalArgumentException e) {
            assertTrue("exception message should mention makeDefault",
                    e.getMessage().contains("makeDefault"));
        }
    }

    @Test
    public void generateRaisesExceptionIfFailOnDuplicatePaymentMethodIsIncludedWithoutCustomerId() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .options(new ClientTokenOptionsRequest().failOnDuplicatePaymentMethod(true));
        try {
            gateway.clientToken().generate(clientTokenRequest);
            fail("generate() should have raised an exception!");
        } catch (IllegalArgumentException e) {
            assertTrue("exception message should mention failOnDuplicatePaymentMethod",
                    e.getMessage().contains("failOnDuplicatePaymentMethod"));
        }
    }
}
