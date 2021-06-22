package com.braintreegateway.unittest.util;

import com.braintreegateway.util.Crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


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
