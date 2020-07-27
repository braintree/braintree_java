package com.braintreegateway.unittest.util;

import com.braintreegateway.util.Sha1Hasher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class Sha1HasherTest {

    @Test
    public void hmacHash() {
        String actual = new Sha1Hasher().hmacHash("secretKey", "hello world");
        assertEquals("d503d7a1a6adba1e6474e9ff2c4167f9dfdf4247", actual);
    }

}
