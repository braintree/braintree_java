package com.braintreegateway.unittest.util;

import com.braintreegateway.util.Crypto;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class CryptoTest {

    @Test
    public void secureCompareWithEqualStrings() {
        assertTrue(new Crypto().secureCompare("a_string", "a_string"));
    }

    @Test
    public void secureCompareWithNotEqualStrings() {
        assertFalse(new Crypto().secureCompare("a_string", "a_strong"));
    }

    @Test
    public void secureCompareWithLongerNotEqualStrings() {
        assertFalse(new Crypto().secureCompare("a_string", "a_string_that_is_longer"));
    }
}
