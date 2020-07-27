package com.braintreegateway.unittest.util;

import com.braintreegateway.*;
import com.braintreegateway.util.ClientLibraryProperties;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class GatewayPropertiesTest {

    // A sequence of digts with optional interspersed dots, allowing one optional -SNAPSHOT
    private final Pattern VERSION_PATTERN = Pattern.compile("^\\d+([.]\\d+)*(-SNAPSHOT)?$");

    public boolean versionValid(String version) {
        return VERSION_PATTERN.matcher(version).matches();
    }

    @Test
    public void patternAcceptance() {
        assertTrue(versionValid("1"));
        assertTrue(versionValid("52.2"));
        assertTrue(versionValid("6.74.3"));
        assertTrue(versionValid("4.9-SNAPSHOT"));
        assertTrue(versionValid("1.1.17-SNAPSHOT"));
    }

    @Test
    public void patternRejection() {
        assertFalse(versionValid("1.1.17-SNAPSHOT-SNAPSHOT"));
        assertFalse(versionValid("1.1-chicken"));
        assertFalse(versionValid("1."));
        assertFalse(versionValid(".1"));
        assertFalse(versionValid("junk1.1"));
        assertFalse(versionValid("1.1-junk"));
    }

    @Test
    public void canRetrieveVersion() {
        String version = new ClientLibraryProperties().version();
        assertTrue("Expecting version to loaded from ClientLibraryProperties", versionValid(version));
    }

    @Test
    public void gatewayUsesProperties() {
        String configurationVersion = Configuration.VERSION;
        String propertiesVersion = new ClientLibraryProperties().version();
        assertEquals(configurationVersion, propertiesVersion);
    }

}
