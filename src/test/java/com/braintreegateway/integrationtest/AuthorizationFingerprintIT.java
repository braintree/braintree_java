package com.braintreegateway.integrationtest;

import java.net.URLEncoder;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.braintreegateway.testhelpers.HttpHelper;
import com.braintreegateway.util.Http;
import com.braintreegateway.Environment;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.AuthorizationFingerprint;

public class AuthorizationFingerprintIT {
    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration_merchant_id",
            "integration_public_key",
            "integration_private_key"
        );
    }

    @Test
    public void fingerprintIsAcceptedByTheGateway() {
        String authorizationFingerprint = this.gateway.generateAuthorizationFingerprint();
        String encodedFingerprint = "";
        try {
            encodedFingerprint = URLEncoder.encode(authorizationFingerprint, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json?";
        url += "authorizationFingerprint=" + encodedFingerprint;
        url += "&sessionIdentifierType=testing&sessionIdentifier=test-identifier";

        int responseCode = -1;
        try {
          responseCode = HttpHelper.get(url);
        } catch (java.net.MalformedURLException e) {
          fail();
        } catch (java.io.IOException e) {
          fail();
        }
        assertEquals(200, responseCode);
    }
}
