package com.braintreegateway.unittest;

import com.braintreegateway.PayPalDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PayPalDetailsTest {
  @Test
  public void includesFields() {
    String xml = "<paypal-details>" +
                 "<payer-email>abc@test.com</payer-email>" +
                 "<payment-id>1234567890</payment-id>" +
                 "<authorization-id>12345</authorization-id>" +
                 "<token>token</token>" +
                 "<image-url>www.image-url.com</image-url>" +
                 "<debug-id>12345</debug-id>" +
                 "<payee-id>6789</payee-id>" +
                 "<payee-email>payee@test.com</payee-email>" +
                 "<custom-field>so custom much wow</custom-field>" +
                 "<payer-id>1357</payer-id>" +
                 "<payer-first-name>Grace</payer-first-name>" +
                 "<payer-last-name>Hopper</payer-last-name>" +
                 "<payer-status>status</payer-status>" +
                 "<seller-protection-status>12345</seller-protection-status>" +
                 "<refund-id>8675309</refund-id>" +
                 "<capture-id>2468</capture-id>" +
                 "<transaction-fee-amount>10.00</transaction-fee-amount>" +
                 "<transaction-fee-currency-iso-code>123</transaction-fee-currency-iso-code>" +
                 "<refund-from-transaction-fee-amount>2.00</refund-from-transaction-fee-amount>" +
                 "<refund-from-transaction-fee-currency-iso-code>123</refund-from-transaction-fee-currency-iso-code>" +
                 "<description>item</description>" +
                 "<implicitly-vaulted-payment-method-token>implicittoken</implicitly-vaulted-payment-method-token>" +
                 "<implicitly-vaulted-payment-method-global-id>implicitglobalid</implicitly-vaulted-payment-method-global-id>" +
                 "<billing-agreement-id>billingagreementid</billing-agreement-id>" +
                 "</paypal-details>";
    SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

    PayPalDetails details = new PayPalDetails(node);

    assertEquals("abc@test.com", details.getPayerEmail());
    assertEquals("1234567890", details.getPaymentId());
    assertEquals("12345", details.getAuthorizationId());
    assertEquals("token", details.getToken());
    assertEquals("www.image-url.com", details.getImageUrl());
    assertEquals("12345", details.getDebugId());
    assertEquals("6789", details.getPayeeId());
    assertEquals("payee@test.com", details.getPayeeEmail());
    assertEquals("so custom much wow", details.getCustomField());
    assertEquals("1357", details.getPayerId());
    assertEquals("Grace", details.getPayerFirstName());
    assertEquals("Hopper", details.getPayerLastName());
    assertEquals("status", details.getPayerStatus());
    assertEquals("12345", details.getSellerProtectionStatus());
    assertEquals("8675309", details.getRefundId());
    assertEquals("2468", details.getCaptureId());
    assertEquals("10.00", details.getTransactionFeeAmount());
    assertEquals("123", details.getTransactionFeeCurrencyIsoCode());
    assertEquals("2.00", details.getRefundFromTransactionFeeAmount());
    assertEquals("123", details.getRefundFromTransactionFeeCurrencyIsoCode());
    assertEquals("item", details.getDescription());
    assertEquals("implicittoken", details.getImplicitlyVaultedPaymentMethodToken());
    assertEquals("implicitglobalid", details.getImplicitlyVaultedPaymentMethodGlobalId());
    assertEquals("billingagreementid", details.getBillingAgreementId());
  }
}
