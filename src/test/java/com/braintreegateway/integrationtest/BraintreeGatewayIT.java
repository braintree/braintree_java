package com.braintreegateway.integrationtest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BraintreeGatewayIT {
    @Test
    public void developmentBaseMerchantUrl() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "publicKey",
                "privateKey");
        String port = System.getenv().get("GATEWAY_PORT") == null ? "3000" : System.getenv().get("GATEWAY_PORT");
        assertEquals("http://localhost:" + port + "/merchants/integration_merchant_id", gateway.getConfiguration().baseMerchantURL);
    }

    @Test
    public void sandboxBaseMerchantUrl() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, "sandbox_merchant_id", "publicKey", "privateKey");
        assertEquals("https://api.sandbox.braintreegateway.com:443/merchants/sandbox_merchant_id", gateway.getConfiguration().baseMerchantURL);
    }

    @Test
    public void productionBaseMerchantUrl() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "production_merchant_id", "publicKey", "privateKey");
        assertEquals("https://api.braintreegateway.com:443/merchants/production_merchant_id", gateway.getConfiguration().baseMerchantURL);
    }

    @Test
    public void partnerIdAlias() {
        BraintreeGateway gateway = BraintreeGateway.forPartner(Environment.DEVELOPMENT, "partner_id", "publicKey", "privateKey");
        assertEquals(gateway.getPublicKey(), "publicKey");
        assertEquals(gateway.getPrivateKey(), "privateKey");
    }

}
