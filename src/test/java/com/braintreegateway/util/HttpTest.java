package com.braintreegateway.util;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.exceptions.UpgradeRequiredException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

@SuppressWarnings("deprecation")
public class HttpTest {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void smokeTestGet() {
        NodeWrapper node = new Http(gateway.getConfiguration()).get("/customers/131866");
        assertNotNull(node.findString("first-name"));
    }

    @Test
    public void smokeTestPostWithRequest() {
        CustomerRequest request = new CustomerRequest().firstName("Dan").lastName("Manges").company("Braintree");
        NodeWrapper node = new Http(gateway.getConfiguration()).post("/customers", request);
        assertEquals("Dan", node.findString("first-name"));
    }

    @Test
    public void smokeTestPut() {
        CustomerRequest request = new CustomerRequest().firstName("NewName");
        NodeWrapper node = new Http(gateway.getConfiguration()).put("/customers/131866", request);
        assertEquals("NewName", node.findString("first-name"));
    }

    @Test
    public void smokeTestDelete() {
        NodeWrapper node = new Http(gateway.getConfiguration()).post("/customers", new CustomerRequest());
        new Http(gateway.getConfiguration()).delete("/customers/" + node.findString("id"));
    }

    @Test(expected = AuthenticationException.class)
    public void authenticationException() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "bad_public_key", "bad_private_key");
        new Http(gateway.getConfiguration()).get("/");
    }

    @Test(expected = AuthenticationException.class)
    public void sslCertificateSuccessfulInSandbox() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, "integration_merchant_id", "integration_public_key", "integration_private_key");
        Http http = new Http(gateway.getConfiguration());
        http.get("/");
    }

    @Test(expected = AuthenticationException.class)
    public void sslCertificateSuccessfulInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "integration_merchant_id", "integration_public_key", "integration_private_key");
        Http http = new Http(gateway.getConfiguration());
        http.get("/");
    }

    @Test(expected = DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingServerToServer() {
        CustomerRequest request = new CustomerRequest();
        new Http(gateway.getConfiguration()).put("/test/maintenance", request);
    }

    @Test(expected = DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.getConfiguration().baseMerchantURL + "/test/maintenance");
        gateway.customer().confirmTransparentRedirect(queryString);
    }

    @Test(expected = DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingNewTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.getConfiguration().baseMerchantURL + "/test/maintenance");
        gateway.transparentRedirect().confirmCustomer(queryString);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticationExceptionRaisedWhenBadCredentialsUsingTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "bad_public", "bad_private");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.customer().transparentRedirectURLForCreate());
        gateway.customer().confirmTransparentRedirect(queryString);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticationExceptionRaisedWhenBadCredentialsUsingNewTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "bad_public", "bad_private");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        gateway.transparentRedirect().confirmCustomer(queryString);
    }

    @Test(expected = UpgradeRequiredException.class)
    public void throwUpgradeRequiredIfClientLibraryIsTooOld() {
        Http.throwExceptionIfErrorStatusCode(426, "Too old");
    }

    @Test
    public void sslBadCertificate() throws Exception {
        Environment environment = new Environment("https://localhost:19443", "", new String[] {"ssl/api_braintreegateway_com.ca.crt"}, "testing");
        BraintreeGateway gateway = new BraintreeGateway(environment, "integration_merchant_id", "bad_public", "bad_private");
        startSSLServer();
        try {
            Http http = new Http(gateway.getConfiguration());
            http.get("/");
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Cert"));
        } finally {
            stopSSLServer();
        }
    }

    @Test
    public void getAuthorizationHeader() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "development_merchant_id", "integration_public_key", "integration_private_key");
        Http http = new Http(config.getConfiguration());

        assertEquals("Basic aW50ZWdyYXRpb25fcHVibGljX2tleTppbnRlZ3JhdGlvbl9wcml2YXRlX2tleQ==", http.authorizationHeader());
    }

    private void startSSLServer() throws Exception {
        String fileName = StringUtils.getFullPathOfFile("script/httpsd.rb");
        new File(fileName).setExecutable(true);
        new ProcessBuilder(fileName, "/tmp/httpsd.pid").start().waitFor();
    }

    private void stopSSLServer() throws IOException {
        String pid = StringUtils.inputStreamToString(new FileInputStream("/tmp/httpsd.pid"));
        new ProcessBuilder("kill", "-9", pid).start();
    }
}
