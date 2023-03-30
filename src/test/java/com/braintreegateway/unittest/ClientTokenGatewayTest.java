package com.braintreegateway.unittest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenGateway;
import com.braintreegateway.ClientTokenOptionsRequest;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.exceptions.ClientTokenGenerationException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.SimpleNodeWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTokenGatewayTest {

    private static final String INTEGRATION_MERCHANT_ID = "integration_merchant_id";

    private BraintreeGateway gateway;

    @BeforeEach
    public void createGateway() {
        this.gateway = new BraintreeGateway(
                Environment.DEVELOPMENT,
                INTEGRATION_MERCHANT_ID,
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
    void generateRaisesExceptionIfApiReturnsError() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .customerId("123456789");

        MockGateway mockGateway = new MockGateway();
        mockGateway.returnErrorOnClientTokenRequest(clientTokenRequest);
        final ClientTokenGateway clientTokenGateway = mockGateway.clientToken();

        Exception e = assertThrows(ClientTokenGenerationException.class,
                () -> clientTokenGateway.generate(clientTokenRequest));

        assertEquals("Customer specified by customer_id does not exist", e.getMessage());
    }

    private static class MockGateway extends BraintreeGateway {

        private final Http http;
        private final NodeWrapper ERROR_RESPONSE = SimpleNodeWrapper.parse(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><api-error-response>\n" +
                        "   <errors>\n" +
                        "     <errors type=\"array\"/>\n" +
                        "     <client-token>********</client-token>\n" +
                        "   </errors>\n" +
                        "   <message>Customer specified by customer_id does not exist</message>\n" +
                        " </api-error-response>"
        );

        MockGateway() {
            super(Environment.DEVELOPMENT,
                    INTEGRATION_MERCHANT_ID,
                    "integration_public_key",
                    "integration_private_key");
            this.http = mock(Http.class);
        }

        @Override
        public ClientTokenGateway clientToken() {
            final Configuration configuration = getConfiguration();
            return new ClientTokenGateway(http, configuration);
        }

        OngoingStubbing<NodeWrapper> returnErrorOnClientTokenRequest(final ClientTokenRequest clientTokenRequest) {
            final String url = "/merchants/" + INTEGRATION_MERCHANT_ID + "/client_token";
            return when(http.post(url, clientTokenRequest)).thenReturn(ERROR_RESPONSE);
        }
    }
}
