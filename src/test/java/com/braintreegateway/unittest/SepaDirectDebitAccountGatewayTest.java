package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.SepaDirectDebitAccountGateway;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;

import static org.mockito.Mockito.mock;

public class SepaDirectDebitAccountGatewayTest {
    private Http http;
    private Configuration configuration;
    private SepaDirectDebitAccountGateway gateway;

    @BeforeEach
    public void createGateway() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new SepaDirectDebitAccountGateway(http, configuration);
    }

    @Test
    public void findThrowsNotFoundForTraversalToken() {
        assertThrows(NotFoundException.class, () -> gateway.find("../../foo"));
    }

    @Test
    public void deleteThrowsNotFoundForTraversalToken() {
        assertThrows(NotFoundException.class, () -> gateway.delete("../../foo"));
    }
}
