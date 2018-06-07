package com.braintreegateway.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.braintreegateway.Configuration;
import com.braintreegateway.Request;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.ValidationErrors;
import com.braintreegateway.exceptions.UnexpectedException;
import com.fasterxml.jackson.jr.ob.JSON;

public class GraphQLClient extends Http {
    private Configuration configuration;

    public GraphQLClient(Configuration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    public Map<String, Object> query(String definition, Request request) {
        HttpURLConnection connection = null;
        Map<String, Object> jsonMap = null;
        Map<String, Object> variables = request != null ? request.toGraphQLVariables() : null;

        String requestString = formatGraphQLRequest(definition, variables);
        String contentType = "application/json";

        Map<String, String> headers = constructHeaders(contentType, contentType);
        headers.put("Braintree-Version", Configuration.GRAPHQL_API_VERSION);

        try {
            connection = buildConnection(Http.RequestMethod.POST, configuration.getGraphQLURL(), headers);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        String jsonString = httpDo(Http.RequestMethod.POST, "/graphql", requestString, null, connection, headers, null);

        try {
            jsonMap = JSON.std.mapFrom(jsonString);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        //TODO: error handling - check for 404-type errors
        return jsonMap;
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

    public static ValidationErrors getErrors(Map<String, Object> response) {
        List<Map<String, Object>> errors = (List<Map<String, Object>>) response.get("errors");
        if (errors == null) {
            return null;
        }

        ValidationErrors validationErrors = new ValidationErrors();
        for (Map<String, Object> error : errors) {
            String message = (String) error.get("message");
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
