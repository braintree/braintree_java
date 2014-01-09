package com.braintreegateway.integrationtest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.junit.Test;
import com.braintreegateway.exceptions.UnexpectedException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BraintreeGatewayIT {
    @Test
    public void developmentBaseMerchantUrl() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "publicKey",
                "privateKey");
        String port = System.getenv().get("GATEWAY_PORT") == null ? "3000" : System.getenv().get("GATEWAY_PORT");
        assertEquals("http://localhost:" + port + "/merchants/integration_merchant_id", config.baseMerchantURL());
    }

    @Test
    public void sandboxBaseMerchantUrl() {
        BraintreeGateway config = new BraintreeGateway(Environment.SANDBOX, "sandbox_merchant_id", "publicKey", "privateKey");
        assertEquals("https://api.sandbox.braintreegateway.com:443/merchants/sandbox_merchant_id", config
                .baseMerchantURL());
    }

    @Test
    public void productionBaseMerchantUrl() {
        BraintreeGateway config = new BraintreeGateway(Environment.PRODUCTION, "production_merchant_id", "publicKey", "privateKey");
        assertEquals("https://api.braintreegateway.com:443/merchants/production_merchant_id", config.baseMerchantURL());
    }

    @Test
    public void partnerIdAlias() {
        BraintreeGateway config = BraintreeGateway.forPartner(Environment.DEVELOPMENT, "partner_id", "publicKey", "privateKey");
        assertEquals(config.getPublicKey(), "publicKey");
        assertEquals(config.getPrivateKey(), "privateKey");
    }

    @Test
    public void getAuthorizationHeader() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "development_merchant_id",
                "integration_public_key", "integration_private_key");
        assertEquals("Basic aW50ZWdyYXRpb25fcHVibGljX2tleTppbnRlZ3JhdGlvbl9wcml2YXRlX2tleQ==", config
                .getAuthorizationHeader());
    }

    @Test
    public void generateAuthorizationInfo() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "development_merchant_id",
                "integration_public_key", "integration_private_key");

        ObjectMapper json_mapper = new ObjectMapper();
        JsonNode authInfo;
        String fingerprint;
        try {
            String rawAuthInfo = config.generateAuthorizationInfo();
            authInfo = json_mapper.readTree(rawAuthInfo);
            fingerprint = authInfo.get("fingerprint").asText();
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage());
        }

        String[] fingerprintParts = fingerprint.split("\\|");
        String signature = fingerprintParts[0];
        String data = fingerprintParts[1];
        String expectedClientApiUrl = config.baseMerchantURL() + "/client_api";

        assertTrue(signature.length() > 1);
        assertTrue(data.contains("public_key=integration_public_key"));
        assertEquals(authInfo.get("client_api_url").asText(), expectedClientApiUrl);
        assertEquals(authInfo.get("auth_url").asText(), "http://auth.venmo.dev:4567");
    }
}
