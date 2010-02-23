package com.braintreegateway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.StringUtils;

public class ValidationErrors {

    private List<ValidationError> errors;
    private Map<String, ValidationErrors> nestedErrors;

    public ValidationErrors(NodeWrapper node) {
        this();
        populateErrors(node);
    }

    public ValidationErrors() {
        errors = new ArrayList<ValidationError>();
        nestedErrors = new HashMap<String, ValidationErrors>();
    }

    public void addError(ValidationError error) {
        errors.add(error);
    }

    public void addErrors(String objectName, ValidationErrors errors) {
        nestedErrors.put(objectName, errors);
    }

    public int deepSize() {
        int size = errors.size();
        for (ValidationErrors nestedError : nestedErrors.values()) {
            size += nestedError.deepSize();
        }
        return size;
    }

    public ValidationErrors forObject(String objectName) {
        return nestedErrors.get(StringUtils.dasherize(objectName));
    }

    public List<ValidationError> onField(String fieldName) {
        List<ValidationError> list = new ArrayList<ValidationError>();
        for (ValidationError error : errors) {
            if (error.getAttribute().equals(StringUtils.underscore(fieldName))) {
                list.add(error);
            }
        }
        return list;
    }

    public int size() {
        return errors.size();
    }

    private void populateErrors(NodeWrapper node) {
        if (node.getElementName() == "api-error-response") {
            node = node.findFirst("errors");
        }

        List<NodeWrapper> errorResponses = node.findAll("*");
        for (NodeWrapper errorResponse : errorResponses) {
            if (errorResponse.getElementName() != "errors") {
                nestedErrors.put(errorResponse.getElementName(), new ValidationErrors(errorResponse));
            } else {
                populateTopLevelErrors(errorResponse.findAll("error"));
            }
        }
    }

    private void populateTopLevelErrors(List<NodeWrapper> childErrors) {
        for (NodeWrapper childError : childErrors) {
            ValidationErrorCode code = ValidationErrorCode.findByCode(childError.findString("code"));
            String message = childError.findString("message");
            errors.add(new ValidationError(childError.findString("attribute"), code, message));
        }
    }

    public List<ValidationError> getAllValidationErrors() {
        return Collections.unmodifiableList(new ArrayList<ValidationError>(errors));
    }

    public List<ValidationError> getAllDeepValidationErrors() {
        List<ValidationError> result = new ArrayList<ValidationError>(errors);
        for (ValidationErrors validationErrors : nestedErrors.values()) {
            result.addAll(validationErrors.getAllDeepValidationErrors());
        }
        return result;
    }
}
