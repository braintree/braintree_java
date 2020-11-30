package com.braintreegateway.util;

public class Crypto {
    public boolean secureCompare(String left, String right) {
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
