package com.braintreegateway.util;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class ShaTypeHasher implements Hasher{
    public String hmacHash(String privateKey, String content, String shaAlgorithm) {
        String hash = "";
        try {
            SecretKeySpec signingKey = new SecretKeySpec(shaTypeBytes(privateKey, shaAlgorithm), shaAlgorithm);
            String macShaInstance = "";
            if(shaAlgorithm.equals("SHA1")) {
                macShaInstance = "HmacSHA1";
            } else {
                macShaInstance = "HmacSHA256";
            }
            Mac mac = Mac.getInstance(macShaInstance);
            mac.init(signingKey);

            byte[] rawMac = mac.doFinal(content.getBytes("UTF-8"));
            byte[] hexBytes = new Hex().encode(rawMac);
            hash = new String(hexBytes, "ISO-8859-1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hash;
    }

    public byte[] shaTypeBytes(String string, String shaAlgorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(shaAlgorithm);
            md.update(string.getBytes("UTF-8"));
            return md.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
