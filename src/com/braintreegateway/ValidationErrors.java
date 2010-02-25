package com.braintreegateway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.StringUtils;

/**
 * Represents a collection of (nested) validation errors. Query for validation errors by object and field.  For Example:
 * 
 *  <pre>
 *  TransactionRequest request = new TransactionRequest().
 *      amount(null).
 *
 *   Result<Transaction> result = gateway.transaction().sale(request);
 *   Assert.assertFalse(result.isSuccess());
 *   ValidationErrors errors = result.getErrors();
 *   Assert.assertEquals(ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, errors.forObject("transaction").onField("amount").get(0).getCode());
 *  </pre>
 */
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

    /**
     * Returns the number of errors on this object and all nested objects.
     * @see #size()
     * @return the number of errors.
     */
    public int deepSize() {
        int size = errors.size();
        for (ValidationErrors nestedError : nestedErrors.values()) {
            size += nestedError.deepSize();
        }
        return size;
    }

    /**
     * Returns a {@link ValidationErrors} representing nested errors for the given ojbectName.
     * @param objectName the name of the object with nested validation errors, e.g. customer or creditCard.
     * @return a {@link ValidationErrors} object.
     */
    public ValidationErrors forObject(String objectName) {
        return nestedErrors.get(StringUtils.dasherize(objectName));
    }

    /**
     * Returns a List of {@link ValidationError} objects for the given field.
     * @param fieldName the name of the field with errors, e.g. amount or expirationDate.
     * @return a List of {@link ValidationError} objects
     */
    public List<ValidationError> onField(String fieldName) {
        List<ValidationError> list = new ArrayList<ValidationError>();
        for (ValidationError error : errors) {
            if (error.getAttribute().equals(StringUtils.underscore(fieldName))) {
                list.add(error);
            }
        }
        return list;
    }

    /**
     * Returns the number of errors on this object at the current nesting level.
     * @see #deepSize()
     * @return the number of errors.
     */
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

    /**
     * Returns a List of all of the {@link ValidationError} objects at the current nesting level.
     * @return a List of {@link ValidationError} objects.
     */
    public List<ValidationError> getAllValidationErrors() {
        return Collections.unmodifiableList(new ArrayList<ValidationError>(errors));
    }

    /**
     * Returns a List of all of the {@link ValidationError} on this object and all nested objects. 
     * @return a List of {@link ValidationError} objects.
     */
    public List<ValidationError> getAllDeepValidationErrors() {
        List<ValidationError> result = new ArrayList<ValidationError>(errors);
        for (ValidationErrors validationErrors : nestedErrors.values()) {
            result.addAll(validationErrors.getAllDeepValidationErrors());
        }
        return result;
    }
}
