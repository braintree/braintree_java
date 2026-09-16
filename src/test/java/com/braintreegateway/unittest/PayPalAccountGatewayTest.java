package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.PayPalAccountGateway;
import com.braintreegateway.PayPalAccountRequest;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;

import static org.mockito.Mockito.mock;

public class PayPalAccountGatewayTest {
    private Http http;
    private Configuration configuration;
    private PayPalAccountGateway gateway;

    @BeforeEach
    public void createGateway() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new PayPalAccountGateway(http, configuration);
    }

    @Test
    public void findThrowsNotFoundForTraversalToken() {
        assertThrows(NotFoundException.class, () -> gateway.find("../../foo"));
    }

    @Test
    public void deleteThrowsNotFoundForTraversalToken() {
        assertThrows(NotFoundException.class, () -> gateway.delete("../../foo"));
    }

    @Test
    public void updateThrowsNotFoundForTraversalToken() {
        assertThrows(NotFoundException.class, () -> gateway.update("../../foo", new PayPalAccountRequest()));
    }
}
