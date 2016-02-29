package com.braintreegateway.integrationtest;

import com.braintreegateway.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;

public class IntegrationTest {

    protected BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration_merchant_id",
            "integration_public_key",
            "integration_private_key"
        );
    }

    @Before
    public void ignoreLogging(){
        if (this.gateway == null) {
            createGateway();
        }

        this.gateway.getConfiguration().setLogger(Logger.getLogger("null"));
        this.gateway.getConfiguration().getLogger().setLevel(Level.INFO);
        this.gateway.getConfiguration().getLogger().setUseParentHandlers(false);
    }
}
