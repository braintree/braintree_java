package com.braintreegateway;

import com.braintreegateway.exceptions.BraintreeException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.HttpClient;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.braintreegateway.util.HttpClient.HttpResponse;
import static com.braintreegateway.util.HttpClient.Payload;
import static com.braintreegateway.util.HttpClient.RequestMethod;

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
            Map<String, String> headers = new HashMap<>();
            headers.put("X-ApiVersion", Configuration.apiVersion());

            String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/" + request.getNonce()
                + "/three_d_secure/lookup";

            HttpClient httpClient = configuration.getHttpClient();
            HttpResponse httpResponse = httpClient.request(RequestMethod.POST, url, Payload.json(headers, request.toJSON()));

            int responseCode = httpResponse.getResponseCode();
            String rawResponse = httpResponse.getResponseBody();
            if (responseCode != 201) {
                Http.throwExceptionIfErrorStatusCode(responseCode, rawResponse);
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
