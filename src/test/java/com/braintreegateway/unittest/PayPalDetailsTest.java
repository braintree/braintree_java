package com.braintreegateway.unittest;

import com.braintreegateway.PayPalDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PayPalDetailsTest {
  @Test
  public void includesFields() {
    String xml = "<paypal-details>" +
                 "<authorization-id>12345</authorization-id>" +
                 "<billing-agreement-id>billingagreementid</billing-agreement-id>" +
                 "<capture-id>2468</capture-id>" +
                 "<custom-field>so custom much wow</custom-field>" +
                 "<debug-id>12345</debug-id>" +
                 "<description>item</description>" +
                 "<image-url>www.image-url.com</image-url>" +
                 "<implicitly-vaulted-payment-method-token>implicittoken</implicitly-vaulted-payment-method-token>" +
                 "<implicitly-vaulted-payment-method-global-id>implicitglobalid</implicitly-vaulted-payment-method-global-id>" +
                 "<payer-email>abc@test.com</payer-email>" +
                 "<payment-id>1234567890</payment-id>" +
                 "<payee-id>6789</payee-id>" +
                 "<payee-email>payee@test.com</payee-email>" +
                 "<payer-id>1357</payer-id>" +
                 "<payer-first-name>Grace</payer-first-name>" +
                 "<payer-last-name>Hopper</payer-last-name>" +
                 "<payer-status>status</payer-status>" +
                 "<recipient-email>test@paypal.com</recipient-email>" +
                 "<recipient-phone>" + 
                     "<country-code>1</country-code>" + 
                     "<national-number>4082222222</national-number>" +
                 "</recipient-phone>" +   
                 "<refund-id>8675309</refund-id>" +
                 "<refund-from-transaction-fee-amount>2.00</refund-from-transaction-fee-amount>" +
                 "<refund-from-transaction-fee-currency-iso-code>123</refund-from-transaction-fee-currency-iso-code>" +
                 "<seller-protection-status>12345</seller-protection-status>" +
                 "<tax-id>54321</tax-id>" +
                 "<tax-id-type>BR_CPF</tax-id-type>" +
                 "<token>token</token>" +
                 "<transaction-fee-amount>10.00</transaction-fee-amount>" +
                 "<transaction-fee-currency-iso-code>123</transaction-fee-currency-iso-code>" +
                 "</paypal-details>";
    SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

    PayPalDetails details = new PayPalDetails(node);


    assertEquals("12345", details.getAuthorizationId());
    assertEquals("billingagreementid", details.getBillingAgreementId());
    assertEquals("2468", details.getCaptureId());
    assertEquals("so custom much wow", details.getCustomField());
    assertEquals("12345", details.getDebugId());
    assertEquals("item", details.getDescription());
    assertEquals("www.image-url.com", details.getImageUrl());
    assertEquals("implicittoken", details.getImplicitlyVaultedPaymentMethodToken());
    assertEquals("implicitglobalid", details.getImplicitlyVaultedPaymentMethodGlobalId());
    assertEquals("abc@test.com", details.getPayerEmail());
    assertEquals("1234567890", details.getPaymentId());
    assertEquals("6789", details.getPayeeId());
    assertEquals("payee@test.com", details.getPayeeEmail());
    assertEquals("1357", details.getPayerId());
    assertEquals("Grace", details.getPayerFirstName());
    assertEquals("Hopper", details.getPayerLastName());
    assertEquals("status", details.getPayerStatus());
    assertEquals("test@paypal.com", details.getRecipientEmail());
    assertEquals("1", details.getRecipientPhone().getCountryCode()); 
    assertEquals("4082222222", details.getRecipientPhone().getNationalNumber());
    assertEquals("8675309", details.getRefundId());
    assertEquals("2.00", details.getRefundFromTransactionFeeAmount());
    assertEquals("123", details.getRefundFromTransactionFeeCurrencyIsoCode());
    assertEquals("12345", details.getSellerProtectionStatus());
    assertEquals("54321", details.getTaxId());
    assertEquals("BR_CPF", details.getTaxIdType());
    assertEquals("token", details.getToken());
    assertEquals("10.00", details.getTransactionFeeAmount());
    assertEquals("123", details.getTransactionFeeCurrencyIsoCode());

  }
}
