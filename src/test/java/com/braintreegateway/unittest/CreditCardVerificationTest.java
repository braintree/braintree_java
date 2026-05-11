package com.braintreegateway.unittest;

import com.braintreegateway.CreditCardVerification;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreditCardVerificationTest {
  @Test
  public void testVerificationParsesNetworkResponseCodeAndText() {
    String xml = "<verification>"
                 + "    <amount>1.02</amount>"
                 + "    <currency-iso-code>USD</currency-iso-code>"
                 + "    <avs-error-response-code nil=\"true\"></avs-error-response-code>"
                 + "    <avs-postal-code-response-code>I</avs-postal-code-response-code>"
                 + "    <status>processor_declined</status>"
                 + "    <processor-response-code>2000</processor-response-code>"
                 + "    <avs-street-address-response-code>I</avs-street-address-response-code>"
                 + "    <processor-response-text>Do Not Honor</processor-response-text>"
                 + "    <network-response-code>05</network-response-code>"
                 + "    <network-response-text>Do not Honor</network-response-text>"
                 + "    <cvv-response-code>M</cvv-response-code>"
                 + "    <network-transaction-id>ABC1234</network-transaction-id>"
                 + "  </verification>";

    SimpleNodeWrapper verificationNode = SimpleNodeWrapper.parse(xml);
    CreditCardVerification verification = new CreditCardVerification(verificationNode);

    assertEquals("05", verification.getNetworkResponseCode());
    assertEquals("Do not Honor", verification.getNetworkResponseText());
  }

  @Test
  public void testVerificationParsesNetworkTransactionId() {
    String xml = "<verification>"
                 + "    <amount>1.02</amount>"
                 + "    <currency-iso-code>USD</currency-iso-code>"
                 + "    <avs-error-response-code nil=\"true\"></avs-error-response-code>"
                 + "    <avs-postal-code-response-code>I</avs-postal-code-response-code>"
                 + "    <status>processor_declined</status>"
                 + "    <processor-response-code>2000</processor-response-code>"
                 + "    <avs-street-address-response-code>I</avs-street-address-response-code>"
                 + "    <processor-response-text>Do Not Honor</processor-response-text>"
                 + "    <network-response-code>05</network-response-code>"
                 + "    <network-response-text>Do not Honor</network-response-text>"
                 + "    <cvv-response-code>M</cvv-response-code>"
                 + "    <network-transaction-id>ABC1234</network-transaction-id>"
                 + "  </verification>";

    SimpleNodeWrapper verificationNode = SimpleNodeWrapper.parse(xml);
    CreditCardVerification verification = new CreditCardVerification(verificationNode);

    assertEquals("ABC1234", verification.getNetworkTransactionId());
  }

  @Test
  public void testVerificationForVisaAni() {
      String xml = "<verification>"
          + "    <ani-first-name-response-code>M</ani-first-name-response-code>"
          + "    <ani-last-name-response-code>M</ani-last-name-response-code>"
          + "  </verification>";

    SimpleNodeWrapper verificationNode = SimpleNodeWrapper.parse(xml);
    CreditCardVerification verification = new CreditCardVerification(verificationNode);

    assertEquals("M", verification.getAniFirstNameResponseCode());
    assertEquals("M", verification.getAniLastNameResponseCode());
  }

  @Test
  public void testVerificationPaymentAccountReferenceFromCreditCard() {
    String xml = "<verification>"
                 + "    <credit-card>"
                 + "        <payment-account-reference>V0010013019339005665779448477</payment-account-reference>"
                 + "    </credit-card>"
                 + "</verification>";

    SimpleNodeWrapper verificationNode = SimpleNodeWrapper.parse(xml);
    CreditCardVerification verification = new CreditCardVerification(verificationNode);

    assertEquals("V0010013019339005665779448477", verification.getCreditCard().getPaymentAccountReference());
  }

  @Test
  public void testVerificationPaymentAccountReferenceFromApplePay() {
    String xml = "<verification>"
                 + "    <apple-pay>"
                 + "        <payment-account-reference>V0010013019339005665779448477</payment-account-reference>"
                 + "    </apple-pay>"
                 + "</verification>";

    SimpleNodeWrapper verificationNode = SimpleNodeWrapper.parse(xml);
    CreditCardVerification verification = new CreditCardVerification(verificationNode);

    assertEquals("V0010013019339005665779448477", verification.getApplePayDetails().getPaymentAccountReference());
  }
}
