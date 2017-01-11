package com.braintreegateway.unittest;

import org.junit.Test;
import org.mockito.Mockito;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.PaymentMethodDeleteRequest;
import com.braintreegateway.PaymentMethodGateway;
import com.braintreegateway.util.Http;

public class PaymentMethodGatewayTest {

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
