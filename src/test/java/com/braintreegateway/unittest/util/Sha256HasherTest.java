package com.braintreegateway.unittest.util;

import com.braintreegateway.util.Sha256Hasher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class Sha256HasherTest {

    @Test
    public void hmacHash() {
        String actual = new Sha256Hasher().hmacHash("secretKey", "hello world");
        assertEquals("a31288ecf77d266463fc7e2a63799cb1ce6dcff156610373f722fa298e932340", actual);
    }

}
