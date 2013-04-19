package com.braintreegateway.util;

import org.junit.Assert;
import org.junit.Test;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;
import com.braintreegateway.util.Crypto;


public class CryptoTest {

    @Test
    public void hmacHash() {
       String actual = new Crypto().hmacHash("secretKey", "hello world");
       Assert.assertEquals("d503d7a1a6adba1e6474e9ff2c4167f9dfdf4247", actual);
    }

    @Test
    public void sha1Bytes() {
       byte[] sha1Bytes = new Crypto().sha1Bytes("hello world");
       byte[] hexBytes = new Hex().encode(sha1Bytes);
       String actual = "";
       try {
           actual = new String(hexBytes, "ISO-8859-1");
       } catch (Exception e) { throw new RuntimeException("bad juju in this test"); }
       Assert.assertEquals("2aae6c35c94fcfb415dbe95f408b9ce91ee846ed", actual);
    }

}
