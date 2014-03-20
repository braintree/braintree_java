package com.braintreegateway.util;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class Crypto {
    public Boolean secureCompare(String left, String right) {
        if (left == null || right == null || (left.length() != right.length())) {
            return false;
        }

        byte[] leftBytes = left.getBytes();
        byte[] rightBytes = right.getBytes();

        int result = 0;
        for (int i = 0; i < left.length(); i++) {
            result = result | leftBytes[i] ^ rightBytes[i];
        }
        return result == 0;
    }
}
