package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.util.Http;

import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodGatewayTest {
    private BraintreeGateway gateway;

    @BeforeEach
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

    @Test
    public void findThrowsErrorOnNullPointer() {
        PaymentMethodGateway paymentMethodGateway = this.gateway.paymentMethod();
        assertThrows(NotFoundException.class, () -> {
            paymentMethodGateway.find(null);
        });
    }
    
    @Test
    public void deleteAndRevokeAllGrants() {
        Http http = Mockito.mock(Http.class);
        BraintreeGateway gateway = new BraintreeGateway("development", "merchant_id", "public_key", "private_key");
        PaymentMethodGateway paymentMethodGateway = new PaymentMethodGateway(http, gateway.getConfiguration());
        String token = "some_token";
        PaymentMethodDeleteRequest request = new PaymentMethodDeleteRequest().revokeAllGrants(true);
        paymentMethodGateway.delete(token, request);
        Mockito.verify(http).delete("/merchants/merchant_id/payment_methods/any/some_token?revoke_all_grants=true");
    }
}
