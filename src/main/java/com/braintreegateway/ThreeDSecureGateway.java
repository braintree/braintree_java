package com.braintreegateway;

import com.braintreegateway.exceptions.BraintreeException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class ThreeDSecureGateway {
    private Configuration configuration;

    ThreeDSecureGateway(Configuration configuration) {
        this.configuration = configuration;
    }

    public ThreeDSecureLookupResponse lookup(ThreeDSecureLookupRequest request) {
        String nonce = request.getNonce();
        String payload = request.toJSON();
        String body;
        boolean isError;
        int errorCode = 0;

        if (request.getAmount() == null) {
            throw new BraintreeException("Amount required");
        } else if (request.getNonce() == null) {
            throw new BraintreeException("Payment method nonce required");
        }

        try {
            URL url = new URL(configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/" + nonce + "/three_d_secure/lookup");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().close();
            isError = connection.getResponseCode() != 201;
            if (isError) {
                errorCode = connection.getResponseCode();
            }
            InputStream responseStream = isError ? connection.getErrorStream() : connection.getInputStream();
            if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                responseStream = new GZIPInputStream(responseStream);
            }
            body = StringUtils.inputStreamToString(responseStream);

            responseStream.close();
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        if (isError) {
            Http.throwExceptionIfErrorStatusCode(errorCode, body);
        }

        return new ThreeDSecureLookupResponse(body);
    }
}
