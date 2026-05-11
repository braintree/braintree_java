package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.ServerException;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MerchantIT extends IntegrationTest {

    @BeforeEach
    public void createGateway() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );
    }

    // NEXT_MAJOR_VERSION remove this test
    @Test
    public void createThrowsServerException() {
        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        assertThrows(ServerException.class, () -> {
            gateway.merchant().create(request);
        });
    }
}
