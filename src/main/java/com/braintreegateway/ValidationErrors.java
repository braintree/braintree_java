package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.StringUtils;
import java.util.*;

/**
 * Represents a collection of (nested) validation errors. Query for validation
 * errors by object and field. For Example:
 * 
 * <pre>
 *  TransactionRequest request = new TransactionRequest().
 *      amount(null).
 *   Result&lt;Transaction&gt; result = gateway.transaction().sale(request);
 *   Assert.assertFalse(result.isSuccess());
 *   ValidationErrors errors = result.getErrors();
 *   Assert.assertEquals(ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, errors.forObject("transaction").onField("amount").get(0).getCode());
 * </pre>
 * 
 * For more detailed information on {@link ValidationErrors}, see <a
 * href="https://developers.braintreepayments.com/reference/general/validation-errors/overview/java"
 * target
 * ="_blank">https://developers.braintreepayments.com/reference/general/validation-errors/overview/java
 * </a>
 */
public class ValidationErrors {

    private List<ValidationError> errors;
    private Map<String, ValidationErrors> nestedErrors;

    public ValidationErrors() {
        errors = new ArrayList<ValidationError>();
        nestedErrors = new HashMap<String, ValidationErrors>();
    }

    public ValidationErrors(NodeWrapper node) {
        this();
        populateErrors(node);
    }

    public void addError(ValidationError error) {
        errors.add(error);
    }

    public void addErrors(String objectName, ValidationErrors errors) {
        nestedErrors.put(objectName, errors);
    }

    /**
     * Returns the number of errors on this object and all nested objects.
     * 
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

    public ValidationErrors forIndex(int index) {
        return forObject("index_" + index);
    }

    /**
     * Returns a {@link ValidationErrors} representing nested errors for the
     * given objectName.
     * 
     * @param objectName
     *            the name of the object with nested validation errors, e.g.
     *            customer or creditCard.
     * @return a {@link ValidationErrors} object.
     */
    public ValidationErrors forObject(String objectName) {
        ValidationErrors errorsOnObject = nestedErrors.get(StringUtils.dasherize(objectName));
        if (errorsOnObject == null) {
            return new ValidationErrors();
        } else {
            return errorsOnObject;
        }
    }

    /**
     * Returns a List of all of the {@link ValidationError} on this object and
     * all nested objects.
     * 
     * @return a List of {@link ValidationError} objects.
     */
    public List<ValidationError> getAllDeepValidationErrors() {
        List<ValidationError> result = new ArrayList<ValidationError>(errors);
        for (ValidationErrors validationErrors : nestedErrors.values()) {
            result.addAll(validationErrors.getAllDeepValidationErrors());
        }
        return result;
    }

    /**
     * Returns a List of all of the {@link ValidationError} objects at the
     * current nesting level.
     * 
     * @return a List of {@link ValidationError} objects.
     */
    public List<ValidationError> getAllValidationErrors() {
        return Collections.unmodifiableList(new ArrayList<ValidationError>(errors));
    }

    /**
     * Returns a List of {@link ValidationError} objects for the given field.
     * 
     * @param fieldName
     *            the name of the field with errors, e.g. amount or
     *            expirationDate.
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

    private void populateErrors(NodeWrapper node) {
        if (node.getElementName().equals("api-error-response")) {
            node = node.findFirst("errors");
        }

        List<NodeWrapper> errorResponses = node.findAll("*");
        for (NodeWrapper errorResponse : errorResponses) {
            if (!errorResponse.getElementName().equals("errors")) {
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
     * Returns the number of errors on this object at the current nesting level.
     * 
     * @see #deepSize()
     * @return the number of errors.
     */
    public int size() {
        return errors.size();
    }
}
