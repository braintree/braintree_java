package com.braintreegateway.unittest;

import com.braintreegateway.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testParseEnvironmentThrowsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            Environment.parseEnvironment("Development_2");
        });
    }

    @Test
    public void testParseEnvironmentThrowsErrorOnEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> {
            Environment.parseEnvironment("");
        });
    }
}
