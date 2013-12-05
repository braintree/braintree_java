package com.braintreegateway.util;

import org.junit.Test;
import java.util.regex.*;

import com.braintreegateway.AuthorizationFingerprintGenerator;
import com.braintreegateway.AuthorizationFingerprintOptions;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
      customerId("a-customer-id").
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
  public void requiresCustomerIdForOptions() {
    Pattern expectedPattern = Pattern.compile("verifyCard");
    try {
      AuthorizationFingerprintOptions options = new AuthorizationFingerprintOptions().verifyCard(true);
      AuthorizationFingerprintGenerator.generate("test", "test", "test", options);
      fail("Expected IllegalArgumentException when credit card options are provided with no customer ID");
    } catch (IllegalArgumentException e) {
      assertTrue(expectedPattern.matcher(e.getMessage()).find());
    }

    expectedPattern = Pattern.compile("makeDefault");
    try {
      AuthorizationFingerprintOptions options = new AuthorizationFingerprintOptions().makeDefault(true);
      AuthorizationFingerprintGenerator.generate("test", "test", "test", options);
      fail("Expected IllegalArgumentException when credit card options are provided with no customer ID");
    } catch (IllegalArgumentException e) {
      assertTrue(expectedPattern.matcher(e.getMessage()).find());
    }

    expectedPattern = Pattern.compile("failOnDuplicatePaymentMethod");
    try {
      AuthorizationFingerprintOptions options = new AuthorizationFingerprintOptions().failOnDuplicatePaymentMethod(true);
      AuthorizationFingerprintGenerator.generate("test", "test", "test", options);
      fail("Expected IllegalArgumentException when credit card options are provided with no customer ID");
    } catch (IllegalArgumentException e) {
      assertTrue(expectedPattern.matcher(e.getMessage()).find());
    }
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
