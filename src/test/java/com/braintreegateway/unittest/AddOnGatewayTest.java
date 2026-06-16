package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.AddOn;
import com.braintreegateway.AddOnGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class AddOnGatewayTest {
    private Http http;
    private Configuration configuration;
    private AddOnGateway gateway;
    private String merchantPath;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new AddOnGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();
    }

    @Test
    public void allReturnsAddOns() {
        when(http.get(merchantPath + "/add_ons")).thenReturn(SimpleNodeWrapper.parse(
                "<add-ons type=\"array\">" +
                "<add-on><id>add_on_one</id><amount>10.00</amount></add-on>" +
                "<add-on><id>add_on_two</id><amount>20.00</amount></add-on>" +
                "</add-ons>"));

        List<AddOn> addOns = gateway.all();

        assertAll("add-ons list",
                () -> assertEquals(2, addOns.size()),
                () -> assertEquals("add_on_one", addOns.get(0).getId()),
                () -> assertEquals("add_on_two", addOns.get(1).getId()));
        verify(http).get(merchantPath + "/add_ons");
    }

    @Test
    public void allReturnsEmptyListWhenNoAddOns() {
        when(http.get(merchantPath + "/add_ons")).thenReturn(
                SimpleNodeWrapper.parse("<add-ons type=\"array\"></add-ons>"));

        assertTrue(gateway.all().isEmpty());
    }
}
