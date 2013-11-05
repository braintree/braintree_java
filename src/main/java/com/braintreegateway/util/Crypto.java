package com.braintreegateway.util;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class Crypto {
    public String hmacHash(String privateKey, String content) {
        String hash = "";
        try {
            SecretKeySpec signingKey = new SecretKeySpec(sha1Bytes(privateKey), "SHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawMac = mac.doFinal(content.getBytes("UTF-8"));
            byte[] hexBytes = new Hex().encode(rawMac);
            hash = new String(hexBytes, "ISO-8859-1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hash;
    }

    public String hmacHashSha256(String privateKey, String content) {
        String hash = "";
        try {
            SecretKeySpec signingKey = new SecretKeySpec(sha256Bytes(privateKey), "SHA-256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawMac = mac.doFinal(content.getBytes("UTF-8"));
            byte[] hexBytes = new Hex().encode(rawMac);
            hash = new String(hexBytes, "ISO-8859-1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hash;
    }

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

    public byte[] sha1Bytes(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(string.getBytes("UTF-8"));
            return md.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] sha256Bytes(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes("UTF-8"));
            return md.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
