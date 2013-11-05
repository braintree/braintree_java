package com.braintreegateway.util;

import org.junit.Test;

import com.braintreegateway.AuthorizationFingerprint;
import static org.junit.Assert.assertTrue;

public class AuthorizationFingerprintTest {

  @Test
  public void containsEssentialData() {
    String fingerprint = AuthorizationFingerprint.generate(
        "my_merchant_id",
        "my_public_key",
        "private_key",
        null
    );
    String[] fingerprintParts = fingerprint.split("\\|");
    String signature = fingerprintParts[0];
    String data = fingerprintParts[1];


    assertTrue(signature.length() > 1);
    assertTrue(data.contains("my_merchant_id"));
    assertTrue(data.contains("my_public_key"));
    assertTrue(data.contains("created_at"));
  }

  @Test
  public void canIncludeCustomerId() {
    String fingerprint = AuthorizationFingerprint.generate(
        "merchant_id",
        "public_key",
        "private_key",
        "a-customer-id"
    );
    String[] fingerprintParts = fingerprint.split("\\|");
    String data = fingerprintParts[1];

    assertTrue(data.contains("a-customer-id"));
  }

  @Test
  public void isUrlEncoded() {
    String fingerprint = AuthorizationFingerprint.generate(
        "needs encoding",
        "public_key",
        "private_key",
        null
    );
    String[] fingerprintParts = fingerprint.split("\\|");
    String data = fingerprintParts[1];

    assertTrue(data.contains("needs%2Bencoding"));
  }
}
