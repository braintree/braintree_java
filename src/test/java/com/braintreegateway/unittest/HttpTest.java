package com.braintreegateway.unittest;

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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTest {

    @Test
    public void throwAuthenticationIfNotAuthenticated() {
        assertThrows(AuthenticationException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(401, null);
        });
    }

    @Test
    public void throwAuthorizationIfNotAuthorized() {
        assertThrows(AuthorizationException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(403, "Not Authorized");
        });
    }

    @Test
    public void throwNotFoundIfElementNotFound() {
        assertThrows(NotFoundException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(404, null);
        });
    }

    @Test
    public void throwRequstTimeoutIfRequestTimeout() {
        assertThrows(RequestTimeoutException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(408, null);
        });
    }

    @Test
    public void throwUpgradeRequiredIfClientLibraryIsTooOld() {
        assertThrows(UpgradeRequiredException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(426, null);
        });
    }

    @Test
    public void throwTooManyRequestsIfRateLimitExceeded() {
        assertThrows(TooManyRequestsException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(429, null);
        });
    }

    @Test
    public void throwServerIfInternalServerError() {
        assertThrows(ServerException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(500, null);
        });
    }

    @Test
    public void throwUnexpectedIfUnexpected() {
        assertThrows(UnexpectedException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(502, null);
        });
    }

    @Test
    public void throwServiceUnavailableIfServiceUnavailable() {
        assertThrows(ServiceUnavailableException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(503, null);
        });
    }

    @Test
    public void throwGatewayTimeoutIfGatewayTimesOut() {
        assertThrows(GatewayTimeoutException.class, () -> {
            Http.throwExceptionIfErrorStatusCode(504, null);
        });
    }

    @Test
    public void getAuthorizationHeader() {
        BraintreeGateway config = new BraintreeGateway(Environment.DEVELOPMENT, "development_merchant_id", "integration_public_key", "integration_private_key");
        Http http = new Http(config.getConfiguration());

        assertEquals("Basic aW50ZWdyYXRpb25fcHVibGljX2tleTppbnRlZ3JhdGlvbl9wcml2YXRlX2tleQ==", http.authorizationHeader());
    }
}
