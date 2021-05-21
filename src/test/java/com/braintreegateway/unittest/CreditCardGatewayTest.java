package com.braintreegateway.unittest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CreditCardGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.exceptions.NotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CreditCardGatewayTest {
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
    public void findThrowsErrorOnNullPointer() {
        CreditCardGateway creditCardGateway = this.gateway.creditCard();
        assertThrows(NotFoundException.class, () -> {
            creditCardGateway.find(null);
        });
    }

    @Test
    public void fromThrowsErrorOnNullPointer() {
        CreditCardGateway creditCardGateway = this.gateway.creditCard();
        assertThrows(NotFoundException.class, () -> {
            creditCardGateway.fromNonce(null);
        });
    }
}
