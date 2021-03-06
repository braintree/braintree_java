package com.braintreegateway.integrationtest;

import com.braintreegateway.ValidationErrorCode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationErrorCodeIT {

    @Test
    public void findByCode() {
        assertEquals(ValidationErrorCode.ADDRESS_CANNOT_BE_BLANK, ValidationErrorCode.findByCode("81801"));
    }

    @Test
    public void findByCodeFallsBackWhenNotFound() {
        assertEquals(ValidationErrorCode.UNKNOWN_VALIDATION_ERROR, ValidationErrorCode.findByCode("-9999"));
    }
}
