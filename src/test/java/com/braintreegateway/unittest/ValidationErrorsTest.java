package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.ValidationErrors;
import com.braintreegateway.util.SimpleNodeWrapper;

public class ValidationErrorsTest {

    @Test
    public void parsesErrorsFromApiErrorResponseNode() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<api-error-response>" +
                "<errors>" +
                "<errors type=\"array\"/>" +
                "<customer>" +
                "<errors type=\"array\">" +
                "<error>" +
                "<code>81608</code>" +
                "<attribute type=\"symbol\">first_name</attribute>" +
                "<message>First name is too long.</message>" +
                "</error>" +
                "</errors>" +
                "</customer>" +
                "</errors>" +
                "</api-error-response>");

        ValidationErrors errors = new ValidationErrors(node);

        assertAll("parsed api-error-response",
                () -> assertEquals(1, errors.deepSize()),
                () -> assertEquals(1, errors.forObject("customer").size()),
                () -> assertEquals("First name is too long.",
                        errors.forObject("customer").onField("first_name").get(0).getMessage()),
                () -> assertEquals(ValidationErrorCode.CUSTOMER_FIRST_NAME_IS_TOO_LONG,
                        errors.forObject("customer").onField("first_name").get(0).getCode()));
    }

    @Test
    public void addErrorAndGetAllValidationErrors() {
        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("amount", ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, "Amount is required."));

        assertAll("add error results",
                () -> assertEquals(1, errors.size()),
                () -> assertEquals(1, errors.deepSize()),
                () -> assertEquals("Amount is required.", errors.getAllValidationErrors().get(0).getMessage()));
    }

    @Test
    public void addNestedErrorsAndDeepSize() {
        ValidationErrors nested = new ValidationErrors();
        nested.addError(new ValidationError("amount", ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, "required"));

        ValidationErrors errors = new ValidationErrors();
        errors.addErrors("transaction", nested);

        assertAll("nested errors depth",
                () -> assertEquals(0, errors.size()),
                () -> assertEquals(1, errors.deepSize()),
                () -> assertEquals(1, errors.getAllDeepValidationErrors().size()));
    }

    @Test
    public void forObjectReturnsEmptyWhenNotFound() {
        ValidationErrors errors = new ValidationErrors();
        assertTrue(errors.forObject("nonexistent").getAllValidationErrors().isEmpty());
    }

    @Test
    public void forIndexDelegatesToForObject() {
        ValidationErrors nested = new ValidationErrors();
        nested.addError(new ValidationError("base", ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, "error"));

        ValidationErrors errors = new ValidationErrors();
        // forIndex("index_0") stores under the dasherized key "index-0"
        errors.addErrors("index-0", nested);

        assertEquals(1, errors.forIndex(0).size());
    }

    @Test
    public void onFieldFiltersCorrectly() {
        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("amount", ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, "Amount required."));
        errors.addError(new ValidationError("status", ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, "Status error."));

        assertAll("onField filtering",
                () -> assertEquals(1, errors.onField("amount").size()),
                () -> assertEquals("Amount required.", errors.onField("amount").get(0).getMessage()),
                () -> assertTrue(errors.onField("nonexistent").isEmpty()));
    }

    @Test
    public void getAllValidationErrorsIsUnmodifiable() {
        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("amount", ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, "required"));

        assertFalse(errors.getAllValidationErrors().isEmpty());
        assertThrows(UnsupportedOperationException.class,
                () -> errors.getAllValidationErrors().clear());
    }
}
