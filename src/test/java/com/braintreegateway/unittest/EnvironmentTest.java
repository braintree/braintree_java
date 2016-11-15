package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class EnvironmentTest {

    @Test
    public void testParseEnvironmentReturnsDevelopment() {
        assertEquals(Environment.DEVELOPMENT, Environment.parseEnvironment("development"));
    }

    @Test
    public void testParseEnvironmentReturnsDevelopmentForIntegration() {
        assertEquals(Environment.DEVELOPMENT, Environment.parseEnvironment("integration"));
    }

    @Test
    public void testParseEnvironmentReturnsSandbox() {
        assertEquals(Environment.SANDBOX, Environment.parseEnvironment("sandbox"));
    }

    @Test
    public void testParseEnvironmentReturnsProduction() {
        assertEquals(Environment.PRODUCTION, Environment.parseEnvironment("production"));
    }

    @Test
    public void testParseEnvironmentReturnsQA() {
        assertEquals(Environment.QA, Environment.parseEnvironment("qa"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseEnvironmentThrowsError() {
        Environment.parseEnvironment("Development_2");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseEnvironmentThrowsErrorOnEmptyString() {
        Environment.parseEnvironment("");
    }
}
