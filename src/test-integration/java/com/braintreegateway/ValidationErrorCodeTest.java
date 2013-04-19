package com.braintreegateway;

import org.junit.Assert;

import org.junit.Test;


public class ValidationErrorCodeTest {

    @Test
    public void findByCode() {
        Assert.assertEquals(ValidationErrorCode.ADDRESS_CANNOT_BE_BLANK, ValidationErrorCode.findByCode("81801"));
    }

    @Test
    public void findByCodeFallsBackWhenNotFound() {
        Assert.assertEquals(ValidationErrorCode.UNKOWN_VALIDATION_ERROR, ValidationErrorCode.findByCode("-9999"));
    }
}
