package com.braintreegateway.util;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Sha256Hasher implements Hasher {

    public String hmacHash(String privateKey, String content) {
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

    private byte[] sha256Bytes(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes("UTF-8"));
            return md.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
