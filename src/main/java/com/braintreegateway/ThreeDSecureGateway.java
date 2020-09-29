package com.braintreegateway;

import com.braintreegateway.exceptions.BraintreeException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.StringUtils;
import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class ThreeDSecureGateway {
    private final Configuration configuration;

    ThreeDSecureGateway(Configuration configuration) {
        this.configuration = configuration;
    }

    public Result<ThreeDSecureLookupResponse> lookup(ThreeDSecureLookupRequest request) {
        if (request.getAmount() == null) {
            throw new BraintreeException("Amount required");
        } else if (request.getNonce() == null) {
            throw new BraintreeException("Payment method nonce required");
        }

        try {
            URL url = new URL(configuration.getBaseURL() + configuration.getMerchantPath() +
                    "/client_api/v1/payment_methods/" + request.getNonce() + "/three_d_secure/lookup");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(request.toJSON().getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().close();

            boolean isError = connection.getResponseCode() != 201;
            InputStream responseStream = isError ? connection.getErrorStream() : connection.getInputStream();
            if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                responseStream = new GZIPInputStream(responseStream);
            }

            String rawResponse = StringUtils.inputStreamToString(responseStream);
            responseStream.close();

            if (isError) {
                Http.throwExceptionIfErrorStatusCode(connection.getResponseCode(), rawResponse);
            }

            Map<String, Object> jsonResponse = JSON.std.mapFrom(rawResponse);

            if (jsonResponse.get("error") != null) {
                Map<String, Object> error = (Map<String, Object>) jsonResponse.get("error");
                ValidationErrors validationErrors = new ValidationErrors();
                validationErrors.addError(new ValidationError("", null, (String) error.get("message")));
                return new Result<>(validationErrors);
            }

            return new Result<>(new ThreeDSecureLookupResponse(jsonResponse, rawResponse));
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }
}
