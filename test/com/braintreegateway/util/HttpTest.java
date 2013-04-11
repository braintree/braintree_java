package com.braintreegateway.util;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.exceptions.UpgradeRequiredException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;

@SuppressWarnings("deprecation")
public class HttpTest {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void smokeTestGet() {
        NodeWrapper node = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION).get("/customers/131866");
        Assert.assertNotNull(node.findString("first-name"));
    }

    @Test
    public void smokeTestPostWithRequest() {
        CustomerRequest request = new CustomerRequest().firstName("Dan").lastName("Manges").company("Braintree");
        NodeWrapper node = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION).post("/customers", request);
        Assert.assertEquals("Dan", node.findString("first-name"));
    }

    @Test
    public void smokeTestPut() {
        CustomerRequest request = new CustomerRequest().firstName("NewName");
        NodeWrapper node = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION).put("/customers/131866", request);
        Assert.assertEquals("NewName", node.findString("first-name"));
    }

    @Test
    public void smokeTestDelete() {
        NodeWrapper node = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION).post("/customers", new CustomerRequest());
        new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION).delete("/customers/" + node.findString("id"));
    }
    
    @Test(expected = AuthenticationException.class)
    public void authenticationException() {
        String authHeader = "Basic " + Base64.encodeBase64String(("bad_public_key:bad_private_key").getBytes()).trim();
        new Http(authHeader, gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION).get("/");
    }

    @Test(expected=AuthenticationException.class)
    public void sslCertificateSuccessfulInSandbox() {
        Http http = new Http("", Environment.SANDBOX.baseURL, Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION);
        http.get("/");
    }
    
    @Test(expected=AuthenticationException.class)
    public void sslCertificateSuccessfulInProduction() {
        Http http = new Http("", Environment.PRODUCTION.baseURL, Environment.PRODUCTION.certificateFilenames, BraintreeGateway.VERSION);
        http.get("/");
    }
    
    @Test(expected=DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingServerToServer() {
        CustomerRequest request = new CustomerRequest();
        new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, "1.0.0").put("/test/maintenance", request);
    }
    
    @Test(expected=DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.getConfiguration().baseMerchantURL + "/test/maintenance");
        gateway.customer().confirmTransparentRedirect(queryString);
    }
    
    @Test(expected=DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingNewTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.getConfiguration().baseMerchantURL + "/test/maintenance");
        gateway.transparentRedirect().confirmCustomer(queryString);
    }
    
    @Test(expected=AuthenticationException.class)
    public void authenticationExceptionRaisedWhenBadCredentialsUsingTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "bad_public", "bad_private");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.customer().transparentRedirectURLForCreate());
        gateway.customer().confirmTransparentRedirect(queryString);
    }

    @Test(expected=AuthenticationException.class)
    public void authenticationExceptionRaisedWhenBadCredentialsUsingNewTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "bad_public", "bad_private");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        gateway.transparentRedirect().confirmCustomer(queryString);
    }
    
    @Test(expected=UpgradeRequiredException.class)
    public void throwUpgradeRequiredIfClientLibraryIsTooOld() {
        new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.SANDBOX.certificateFilenames, "1.0.0").get("/");
    }

    @Test
    public void sslBadCertificate() throws Exception {
        startSSLServer();
        try {
            Http http = new Http(gateway.getAuthorizationHeader(), "https://localhost:9443", Environment.SANDBOX.certificateFilenames, BraintreeGateway.VERSION);
            http.get("/");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Cert"));
        } finally {
            stopSSLServer();
        }
    }

    private void startSSLServer() throws Exception {
        String fileName = StringUtils.getFullPathOfFile("script/httpsd.rb");
        new ProcessBuilder(fileName, "/tmp/httpsd.pid").start().waitFor();
    }

    private void stopSSLServer() throws IOException {
        String pid = StringUtils.inputStreamToString(new FileInputStream("/tmp/httpsd.pid"));
        new ProcessBuilder("kill", "-9", pid).start();
    }
}
