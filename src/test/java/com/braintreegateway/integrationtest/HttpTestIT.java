package com.braintreegateway.integrationtest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.CreditCard;
import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.DocumentUpload;
import com.braintreegateway.DocumentUploadRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("deprecation")
public class HttpTestIT extends IntegrationTest {

    @Test
    public void smokeTestGet() {
        Configuration configuration = gateway.getConfiguration();
        NodeWrapper node = new Http(configuration).get(configuration.getMerchantPath() + "/customers/big_spender");
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
    public void smokeTestPostMultipart() {
        URL fileToUpload = getClass().getClassLoader().getResource("fixtures/bt_logo.png");
        DocumentUploadRequest request = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT,
                                                                        new File(fileToUpload.getFile()));

        Configuration configuration = gateway.getConfiguration();
        NodeWrapper node = new Http(configuration).postMultipart(configuration.getMerchantPath() + "/document_uploads", request.getRequest(), request.getFile());
        assertEquals("image/png", node.findString("content-type"));
    }

    @Test
    public void smokeTestPut() {
        CustomerRequest request = new CustomerRequest().firstName("NewName");
        Configuration configuration = gateway.getConfiguration();
        NodeWrapper node = new Http(configuration).put(configuration.getMerchantPath() + "/customers/big_spender", request);
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

        NodeWrapper node = new Http(configuration).get(configuration.getMerchantPath() + "/customers/big_spender");
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
        assertTrue(capturedLog.contains("<cvv>***</cvv>"));
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
            NodeWrapper node = new Http(configuration).get(configuration.getMerchantPath() + "/customers/big_spender");
        } catch (UnexpectedException e) {
        } finally {
            capturedLog = getTestCapturedLog();
            assertTrue(capturedLog.contains("SEVERE: SSL Verification failed. Error message: Missing input stream"));
        }
    }

    @Test(expected = AuthenticationException.class)
    public void authenticationException() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "bad_public_key", "bad_private_key");
        new Http(gateway.getConfiguration()).get("/");
    }

    @Test
    public void sslCertificateSuccessfulInQA() {
        try {
            BraintreeGateway gateway = new BraintreeGateway(Environment.QA, "integration_merchant_id", "bad_public_key", "bad_private_key");
            Http http = new Http(gateway.getConfiguration());
            http.get("/");
        } catch (AuthenticationException ex) {
            // success
        } catch (DownForMaintenanceException ex) {
            // QA is down
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
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
        }
    }

    private void startSSLServer() throws Exception {
        new ProcessBuilder("java", "com.braintreegateway.util.HttpsTest").directory(new File("target/test-classes")).start(); Thread.sleep(2000);
    }
}
