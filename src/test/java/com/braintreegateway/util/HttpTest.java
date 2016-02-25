package com.braintreegateway.util;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.CreditCard;
import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.exceptions.UpgradeRequiredException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.Result;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.StringUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.logging.StreamHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

@SuppressWarnings("deprecation")
public class HttpTest {

    private BraintreeGateway gateway;
    private static OutputStream logCapturingStream;
    private static StreamHandler customLogHandler;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    public void attachLogCapturer()
    {
        Configuration configuration = this.gateway.getConfiguration();
        Logger logger = configuration.getLogger();
        logCapturingStream = new ByteArrayOutputStream();
        Handler[] handlers = logger.getParent().getHandlers();
        customLogHandler = new StreamHandler(logCapturingStream, handlers[0].getFormatter());
        customLogHandler.setLevel(Level.FINE);
        logger.addHandler(customLogHandler);
    }

    public String getTestCapturedLog()
    {
      customLogHandler.flush();
      return logCapturingStream.toString();
    }

    @Test
    public void smokeTestGet() {
        Configuration configuration = gateway.getConfiguration();
        NodeWrapper node = new Http(configuration).get(configuration.getMerchantPath() + "/customers/131866");
        assertNotNull(node.findString("first-name"));
    }

    @Test
    public void smokeTestPostWithRequest() {
        CustomerRequest request = new CustomerRequest().firstName("Dan").lastName("Manges").company("Braintree");
        Configuration configuration = gateway.getConfiguration();
        NodeWrapper node = new Http(configuration).post(configuration.getMerchantPath() + "/customers", request);
        assertEquals("Dan", node.findString("first-name"));
    }

    @Test
    public void smokeTestPut() {
        CustomerRequest request = new CustomerRequest().firstName("NewName");
        Configuration configuration = gateway.getConfiguration();
        NodeWrapper node = new Http(configuration).put(configuration.getMerchantPath() + "/customers/131866", request);
        assertEquals("NewName", node.findString("first-name"));
    }

    @Test
    public void smokeTestDelete() {
        Configuration configuration = gateway.getConfiguration();
        NodeWrapper node = new Http(configuration).post(configuration.getMerchantPath() + "/customers", new CustomerRequest());
        new Http(gateway.getConfiguration()).delete(configuration.getMerchantPath() + "/customers/" + node.findString("id"));
    }

    @Test
    public void smokeTestLogsRequests() {
        Configuration configuration = gateway.getConfiguration();
        attachLogCapturer();

        NodeWrapper node = new Http(configuration).get(configuration.getMerchantPath() + "/customers/131866");
        String capturedLog = getTestCapturedLog();

        assertTrue(capturedLog.contains("[Braintree]"));
        assertTrue(capturedLog.contains("GET /merchants/integration_merchant_id/customers"));
    }

    @Test
    public void smokeTestLogsFullRequestInDebugMode() {
        Configuration configuration = gateway.getConfiguration();
        configuration.getLogger().setLevel(Level.FINEST);
        attachLogCapturer();

        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);

        String capturedLog = getTestCapturedLog();
        assertTrue(capturedLog.contains("[Braintree]"));
        assertTrue(capturedLog.contains("POST /merchants/integration_merchant_id/payment_methods"));
        assertTrue(capturedLog.contains("<cardholder-name>John Doe</cardholder-name>"));
        assertTrue(capturedLog.contains("<number>510510******5100</number>"));
    }

    @Test
    public void smokeTestLogsErrors() {
        Environment fake_environment = new Environment(
            "https://api.sandbox.braintreegateway.com:443",
            "http://auth.sandbox.venmo.com",
            new String[] {"fake_ca_cert"},
            "sandbox"
        );
        this.gateway = new BraintreeGateway(
            fake_environment,
            "integration_merchant_id",
            "integration_public_key",
            "integration_private_key"
        );

        String capturedLog = "";
        try {
            Configuration configuration = this.gateway.getConfiguration();
            attachLogCapturer();
            NodeWrapper node = new Http(configuration).get(configuration.getMerchantPath() + "/customers/131866");
        } catch (UnexpectedException e) {
        } finally {
            capturedLog = getTestCapturedLog();
            assertTrue(capturedLog.contains("SEVERE: SSL Verification failed. Error message:"));
        }
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
        Configuration configuration = gateway.getConfiguration();
        new Http(configuration).put(configuration.getMerchantPath() + "/test/maintenance", request);
    }

    @Test(expected = DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        Configuration configuration = gateway.getConfiguration();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, configuration.getBaseURL() + configuration.getMerchantPath() + "/test/maintenance");
        gateway.customer().confirmTransparentRedirect(queryString);
    }

    @Test(expected = DownForMaintenanceException.class)
    public void downForMaintenanceExceptionRaisedWhenAppInMaintenanceModeUsingNewTR() {
        CustomerRequest request = new CustomerRequest();
        CustomerRequest trParams = new CustomerRequest();
        Configuration configuration = gateway.getConfiguration();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, configuration.getBaseURL() + configuration.getMerchantPath() + "/test/maintenance");
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
