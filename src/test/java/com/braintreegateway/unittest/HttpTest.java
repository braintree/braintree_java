package com.braintreegateway.unittest;

import org.junit.Test;
import static org.junit.Assert.*;

import com.braintreegateway.util.Http;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.AuthorizationException;
import com.braintreegateway.exceptions.GatewayTimeoutException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.RequestTimeoutException;
import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.exceptions.ServiceUnavailableException;
import com.braintreegateway.exceptions.TooManyRequestsException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.exceptions.UpgradeRequiredException;

public class HttpTest {

    @Test(expected = AuthenticationException.class)
    public void throwAuthenticationIfNotAuthenticated() {
        Http.throwExceptionIfErrorStatusCode(401, null);
    }

    @Test(expected = AuthorizationException.class)
    public void throwAuthorizationIfNotAuthorized() {
        Http.throwExceptionIfErrorStatusCode(403, "Not Authorized");
    }

    @Test(expected = NotFoundException.class)
    public void throwNotFoundIfElementNotFound() {
        Http.throwExceptionIfErrorStatusCode(404, null);
    }

    @Test(expected = RequestTimeoutException.class)
    public void throwRequstTimeoutIfRequestTimeout() {
        Http.throwExceptionIfErrorStatusCode(408, null);
    }

    @Test(expected = UpgradeRequiredException.class)
    public void throwUpgradeRequiredIfClientLibraryIsTooOld() {
        Http.throwExceptionIfErrorStatusCode(426, null);
    }

    @Test(expected = TooManyRequestsException.class)
    public void throwTooManyRequestsIfRateLimitExceeded() {
        Http.throwExceptionIfErrorStatusCode(429, null);
    }

    @Test(expected = ServerException.class)
    public void throwServerIfInternalServerError() {
        Http.throwExceptionIfErrorStatusCode(500, null);
    }

    @Test(expected = UnexpectedException.class)
    public void throwUnexpectedIfUnexpected() {
        Http.throwExceptionIfErrorStatusCode(502, null);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void throwServiceUnavailableIfServiceUnavailable() {
        Http.throwExceptionIfErrorStatusCode(503, null);
    }

    @Test(expected = GatewayTimeoutException.class)
    public void throwGatewayTimeoutIfGatewayTimesOut() {
        Http.throwExceptionIfErrorStatusCode(504, null);
    }

    @Test
    public void getAuthorizationHeader() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "development_merchant_id", "integration_public_key", "integration_private_key");
        Http http = new Http(config.getConfiguration());

        assertEquals("Basic aW50ZWdyYXRpb25fcHVibGljX2tleTppbnRlZ3JhdGlvbl9wcml2YXRlX2tleQ==", http.authorizationHeader());
    }
}
