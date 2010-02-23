package com.braintreegateway;

import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.braintreegateway.util.StringUtils;

public class BraintreeGatewayTest {
    @Test
    public void developmentBaseMerchantUrl() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "publicKey",
                "privateKey");
        String port = System.getenv().get("GATEWAY_PORT") == null ? "3000" : System.getenv().get("GATEWAY_PORT");
        Assert.assertEquals("http://localhost:" + port + "/merchants/integration_merchant_id", config.baseMerchantURL());
    }

    @Test
    public void sandboxBaseMerchantUrl() {
        BraintreeGateway config = new BraintreeGateway(Environment.SANDBOX, "sandbox_merchant_id", "publicKey", "privateKey");
        Assert.assertEquals("https://sandbox.braintreegateway.com:443/merchants/sandbox_merchant_id", config
                .baseMerchantURL());
    }

    @Test
    public void productionBaseMerchantUrl() {
        BraintreeGateway config = new BraintreeGateway(Environment.PRODUCTION, "production_merchant_id", "publicKey", "privateKey");
        Assert.assertEquals("https://braintreegateway.com:443/merchants/production_merchant_id", config.baseMerchantURL());
    }

    @Test
    public void getAuthorizationHeader() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "development_merchant_id",
                "integration_public_key", "integration_private_key");
        Assert.assertEquals("Basic aW50ZWdyYXRpb25fcHVibGljX2tleTppbnRlZ3JhdGlvbl9wcml2YXRlX2tleQ==", config
                .getAuthorizationHeader());
    }
    
    @Test
    public void getVersion() throws Exception {
        String expected = StringUtils.inputStreamToString(new FileInputStream("VERSION"));
        Assert.assertEquals(expected.trim(), new BraintreeGateway(Environment.DEVELOPMENT, null, null, null).getVersion());
    }
}
