package com.braintreegateway.util;

import com.braintreegateway.Configuration;
import com.braintreegateway.Request;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.ValidationErrors;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.AuthorizationException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.exceptions.ServiceUnavailableException;
import com.braintreegateway.exceptions.TooManyRequestsException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.exceptions.UpgradeRequiredException;
import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.braintreegateway.util.HttpClient.Payload;
import static com.braintreegateway.util.HttpClient.RequestMethod;

public class GraphQLClient extends Http {
    private Configuration configuration;
    public enum ErrorClass {
        AUTHENTICATION,
        AUTHORIZATION,
        INTERNAL,
        UNSUPPORTED_CLIENT,
        NOT_FOUND,
        RESOURCE_LIMIT,
        SERVICE_AVAILABILITY,
        UNKNOWN,
        VALIDATION;
    }
    private static final String ERROR_OBJECT_KEY = "errors";
    private static final String ERROR_MESSAGE_KEY = "message";
    private static final String ERROR_EXTENSIONS_KEY = "extensions";
    private static final String ERROR_CLASS_KEY = "errorClass";

    public GraphQLClient(Configuration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    public Map<String, Object> query(String definition) {
        return query(definition, new HashMap<String, Object>());
    }

    // NEXT_MAJOR_VERSION consider using JSONObject instead of Map here, should make
    // little difference to the consumer of the SDK, but should make developing
    // it a little easier
    public Map<String, Object> query(String definition, Map<String, Object> variables) {
        Map<String, Object> jsonMap = null;

        String requestString = formatGraphQLRequest(definition, variables);

        Map<String, String> headers = constructHeaders("application/json");
        headers.put("Braintree-Version", Configuration.GRAPHQL_API_VERSION);

        String jsonString = httpDo(RequestMethod.POST, configuration.getGraphQLURL(), "/graphql", Payload.json(headers, requestString));

        try {
            jsonMap = JSON.std.mapFrom(jsonString);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        throwExceptionIfGraphQLErrorResponse(jsonMap);

        return jsonMap;
    }

    public Map<String, Object> query(String definition, Request request) {
        Map<String, Object> variables = request.toGraphQLVariables();

        return query(definition, variables);
    }

    public static String formatGraphQLRequest(String definition, Map<String, Object> variables) {
        String json = null;

        Map<String, Object> map = new TreeMap<String, Object>();

        map.put("query", definition);
        map.put("variables", variables);

        try {
            json = JSON.std.asString(map);
        } catch (IOException e) {
            throw new AssertionError("An IOException occurred when writing JSON object.");
        }

        return json;
    }

    private void throwExceptionIfGraphQLErrorResponse(Map<String, Object> response) {
        List<Map<String, Object>> errors = (List<Map<String, Object>>) response.get(ERROR_OBJECT_KEY);
        if (errors == null) {
            return;
        }

        for (Map<String, Object> error : errors) {
            String message = (String) error.get(ERROR_MESSAGE_KEY);
            Map<String, Object> extensions = (Map) error.get(ERROR_EXTENSIONS_KEY);
            String errorClass = null;
            if (extensions == null || (errorClass = (String) extensions.get(ERROR_CLASS_KEY)) == null) {
                continue;
            }
            switch (ErrorClass.valueOf(errorClass)) {
                case VALIDATION:
                    continue;
                case AUTHENTICATION:
                    throw new AuthenticationException();
                case AUTHORIZATION:
                    throw new AuthorizationException(message);
                case NOT_FOUND:
                    throw new NotFoundException(message);
                case UNSUPPORTED_CLIENT:
                    throw new UpgradeRequiredException();
                case RESOURCE_LIMIT:
                    throw new TooManyRequestsException("Request rate or complexity limit exceeded");
                case INTERNAL:
                    throw new ServerException();
                case SERVICE_AVAILABILITY:
                    throw new ServiceUnavailableException();
                case UNKNOWN:
                    throw new UnexpectedException("Unexpected Response: " + message);
            }
        }

        return;
    }

    public static ValidationErrors getErrors(Map<String, Object> response) {
        List<Map<String, Object>> errors = (List<Map<String, Object>>) response.get(ERROR_OBJECT_KEY);
        if (errors == null) {
            return null;
        }

        ValidationErrors validationErrors = new ValidationErrors();
        for (Map<String, Object> error : errors) {
            String message = (String) error.get(ERROR_MESSAGE_KEY);
            validationErrors.addError(new ValidationError("", getValidationErrorCode(error), message));
        }

        return validationErrors;
    }

    private static ValidationErrorCode getValidationErrorCode(Map<String, Object> error) {
        Map<String, Object> extensions = (Map<String, Object>) error.get("extensions");
        if (extensions == null) {
            return null;
        }

        String code = (String) extensions.get("legacyCode");
        if (code == null) {
            return null;
        }

        return ValidationErrorCode.findByCode(code);
    }
}
