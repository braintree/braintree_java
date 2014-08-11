package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.exceptions.NotFoundException;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class CreditCardGatewayTest {
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

    @Test(expected=NotFoundException.class)
    public void findHandlesNullPointer() {
        CreditCardGateway creditCardGateway = this.gateway.creditCard();
        creditCardGateway.find(null);
    }

    @Test(expected=NotFoundException.class)
    public void forwardHandlesNullPointerForReceivingToken() {
        PaymentMethodForwardRequest forwardRequest = new PaymentMethodForwardRequest();
        Result<PaymentMethodNonce> forwardResult = gateway.creditCard()
            .forward(forwardRequest);
    }

    @Test(expected=NotFoundException.class)
    public void forwardHandlesNullPointerForReceivingMerchantID() {
        PaymentMethodForwardRequest forwardRequest = new PaymentMethodForwardRequest()
            .token("1234");
        Result<PaymentMethodNonce> forwardResult = gateway.creditCard()
            .forward(forwardRequest);
    }

    @Test(expected=NotFoundException.class)
    public void fromNonceHandlesNullPointer() {
        CreditCardGateway creditCardGateway = this.gateway.creditCard();
        creditCardGateway.fromNonce(null);
    }
}
