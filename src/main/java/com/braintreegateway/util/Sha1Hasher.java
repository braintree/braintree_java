package com.braintreegateway.util;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Sha1Hasher extends ShaTypeHasher {
    public String hmacHash(String privateKey, String content) {
        return super.hmacHash(privateKey, content, "SHA1");
    }
}
