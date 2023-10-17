package com.braintreegateway.unittest;

import com.braintreegateway.LocalPaymentDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocalPaymentDetailsTest {
  @Test
  public void includesFields() {
    String xml = "<local-payment-details>" +
                 "<capture-id>CAT-1234</capture-id>" +
                 "<custom-field>whatever</custom-field>" +
                 "<debug-id>DEB-1234</debug-id>" +
                 "<description>Detailed text</description>" +
                 "<funding-source>ideal</funding-source>" +
                 "<implicitly-vaulted-payment-method-global-id>abcdefgabcdefg</implicitly-vaulted-payment-method-global-id>" +
                 "<implicitly-vaulted-payment-method-token>cx9rav</implicitly-vaulted-payment-method-token>" +
                 "<payer-id>nothing</payer-id>" +
                 "<payment-id>ABC12345</payment-id>" +
                 "<refund-from-transaction-fee-amount>2.00</refund-from-transaction-fee-amount>" +
                 "<refund-from-transaction-fee-currency-iso-code>EUR</refund-from-transaction-fee-currency-iso-code>" +
                 "<refund-id>REF-1234</refund-id>" +
                 "<transaction-fee-amount>10.00</transaction-fee-amount>" +
                 "<transaction-fee-currency-iso-code>EUR</transaction-fee-currency-iso-code>" +
                 "</local-payment-details>";
    SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

    LocalPaymentDetails details = new LocalPaymentDetails(node);

    assertEquals("CAT-1234", details.getCaptureId());
    assertEquals("whatever", details.getCustomField());
    assertEquals("DEB-1234", details.getDebugId());
    assertEquals("Detailed text", details.getDescription());
    assertEquals("ideal", details.getFundingSource());
    assertEquals("abcdefgabcdefg", details.getImplicitlyVaultedPaymentMethodGlobalId());
    assertEquals("cx9rav", details.getImplicitlyVaultedPaymentMethodToken());
    assertEquals("nothing", details.getPayerId());
    assertEquals("ABC12345", details.getPaymentId());
    assertEquals("2.00", details.getRefundFromTransactionFeeAmount());
    assertEquals("EUR", details.getRefundFromTransactionFeeCurrencyIsoCode());
    assertEquals("REF-1234", details.getRefundId());
    assertEquals("10.00", details.getTransactionFeeAmount());
    assertEquals("EUR", details.getTransactionFeeCurrencyIsoCode());
  }
}
