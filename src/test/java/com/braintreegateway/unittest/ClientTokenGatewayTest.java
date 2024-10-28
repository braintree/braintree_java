package com.braintreegateway.unittest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenOptionsRequest;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Environment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTokenGatewayTest {
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
    public void generateRaisesExceptionIfVerifyCardIsIncludedWithoutCustomerId() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .options(new ClientTokenOptionsRequest().verifyCard(true));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            gateway.clientToken().generate(clientTokenRequest);
        });

        assertTrue(e.getMessage().contains("verifyCard"),
                "exception message should mention verifyCard");
    }

    @Test
    public void generateRaisesExceptionIfMakeDefaultIsIncludedWithoutCustomerId() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .options(new ClientTokenOptionsRequest().makeDefault(true));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            gateway.clientToken().generate(clientTokenRequest);
        });

        assertTrue(e.getMessage().contains("makeDefault"),
                "exception message should mention makeDefault");
    }

    @Test
    public void generateRaisesExceptionIfFailOnDuplicatePaymentMethodIsIncludedWithoutCustomerId() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .options(new ClientTokenOptionsRequest().failOnDuplicatePaymentMethod(true));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            gateway.clientToken().generate(clientTokenRequest);
        });

        assertTrue(e.getMessage().contains("failOnDuplicatePaymentMethod"),
                "exception message should mention failOnDuplicatePaymentMethod");
    }

    @Test
    public void generateRaisesExceptionIfFailOnDuplicatePaymentMethodForCustomerIsIncludedWithoutCustomerId() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .options(new ClientTokenOptionsRequest().failOnDuplicatePaymentMethodForCustomer(true));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            gateway.clientToken().generate(clientTokenRequest);
        });

        assertTrue(e.getMessage().contains("failOnDuplicatePaymentMethodForCustomer"),
                "exception message should mention failOnDuplicatePaymentMethodForCustomer");
    }
}
