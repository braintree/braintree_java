package com.braintreegateway.integrationtest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals("https://sandbox.braintreegateway.com:443/merchants/sandbox_merchant_id", config
                .baseMerchantURL());
    }

    @Test
    public void productionBaseMerchantUrl() {
        BraintreeGateway config = new BraintreeGateway(Environment.PRODUCTION, "production_merchant_id", "publicKey", "privateKey");
        assertEquals("https://www.braintreegateway.com:443/merchants/production_merchant_id", config.baseMerchantURL());
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
}
