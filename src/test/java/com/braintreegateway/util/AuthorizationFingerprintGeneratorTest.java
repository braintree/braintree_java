package com.braintreegateway.util;

import org.junit.Test;

import com.braintreegateway.AuthorizationFingerprintGenerator;
import com.braintreegateway.AuthorizationFingerprintOptions;
import static org.junit.Assert.assertTrue;

public class AuthorizationFingerprintGeneratorTest {

  @Test
  public void containsEssentialData() {
    String fingerprint = AuthorizationFingerprintGenerator.generate(
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
    String fingerprint = AuthorizationFingerprintGenerator.generate(
        "merchant_id",
        "public_key",
        "private_key",
        new AuthorizationFingerprintOptions().customerId("a-customer-id")
    );
    String[] fingerprintParts = fingerprint.split("\\|");
    String data = fingerprintParts[1];

    assertTrue(data.contains("a-customer-id"));
  }

  @Test
  public void containsOptions() {
    AuthorizationFingerprintOptions options = new AuthorizationFingerprintOptions().
      makeDefault(true).
      verifyCard(true).
      failOnDuplicatePaymentMethod(true);

    String fingerprint = AuthorizationFingerprintGenerator.generate(
        "needs encoding",
        "public_key",
        "private_key",
        options
    );
    String[] fingerprintParts = fingerprint.split("\\|");
    String data = fingerprintParts[1];

    assertTrue(data.contains("credit_card%5Boptions%5D%5Bmake_default%5D=true"));
    assertTrue(data.contains("credit_card%5Boptions%5D%5Bverify_card%5D=true"));
    assertTrue(data.contains("credit_card%5Boptions%5D%5Bfail_on_duplicate_payment_method%5D=true"));
  }

  @Test
  public void isUrlEncoded() {
    String fingerprint = AuthorizationFingerprintGenerator.generate(
        "needs encoding",
        "public_key",
        "private_key",
        null
    );
    String[] fingerprintParts = fingerprint.split("\\|");
    String data = fingerprintParts[1];

    assertTrue(data.contains("needs+encoding"));
  }
}
