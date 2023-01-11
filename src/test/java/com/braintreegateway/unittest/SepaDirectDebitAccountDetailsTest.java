package com.braintreegateway.unittest;

import com.braintreegateway.SepaDirectDebitAccountDetails;
import com.braintreegateway.SepaDirectDebitAccountDetails.MandateType;
import com.braintreegateway.SepaDirectDebitAccountDetails.SettlementType;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SepaDirectDebitAccountDetailsTest {
    @Test
    public void testSepaDirectDebitAccountDetailsAttributes() {
        String xml = "<sepa-debit-account-details>"
                     + "<paypal-v2-order-id>123456</paypal-v2-order-id>"
                     + "<last-4>1234</last-4>"
                     + "<mandate-type>ONE_OFF</mandate-type>"
                     + "<settlement-type>instant</settlement-type>"
                     + "<bank-reference-token>123456789</bank-reference-token>"
                     + "<debug-id>ABC123</debug-id>"
                     + "<token>ch6byss</token>"
                     + "<capture-id>ch6xxx123</capture-id>"
                     + "<refund-id>caaaxxx123</refund-id>"
                     + "<merchant-or-partner-customer-id>123456789</merchant-or-partner-customer-id>"
                     + "<transaction-fee-amount>12.34</transaction-fee-amount>"
                     + "<transaction-fee-currency-iso-code>EUR</transaction-fee-currency-iso-code>"
                     + "<refund-from-transaction-fee-amount>1.34</refund-from-transaction-fee-amount>"
                     + "<refund-from-transaction-fee-currency-iso-code>EUR</refund-from-transaction-fee-currency-iso-code>"
                     + "</sepa-debit-account-details>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        SepaDirectDebitAccountDetails sepaDirectDebitAccountDetails = new SepaDirectDebitAccountDetails(node);

        assertEquals("123456", sepaDirectDebitAccountDetails.getPayPalV2OrderId());
        assertEquals("123456789", sepaDirectDebitAccountDetails.getMerchantOrPartnerCustomerId());
        assertEquals("ABC123", sepaDirectDebitAccountDetails.getDebugId());
        assertEquals("1234", sepaDirectDebitAccountDetails.getLast4());
        assertEquals("ch6byss", sepaDirectDebitAccountDetails.getToken());
        assertEquals("123456789", sepaDirectDebitAccountDetails.getBankReferenceToken());
        assertEquals("ONE_OFF", sepaDirectDebitAccountDetails.getMandateType().name());
        assertEquals("12.34", sepaDirectDebitAccountDetails.getTransactionFeeAmount());
        assertEquals("EUR", sepaDirectDebitAccountDetails.getTransactionFeeCurrencyIsoCode());
        assertEquals("1.34", sepaDirectDebitAccountDetails.getRefundFromTransactionFeeAmount());
        assertEquals("EUR", sepaDirectDebitAccountDetails.getRefundFromTransactionFeeCurrencyIsoCode());
        assertEquals("caaaxxx123", sepaDirectDebitAccountDetails.getRefundId());
        assertEquals("ch6xxx123", sepaDirectDebitAccountDetails.getCaptureId());
        assertEquals("INSTANT", sepaDirectDebitAccountDetails.getSettlementType().name());
    }

    @Test
    public void testMandateTypeEnum() {
        assertEquals(MandateType.ONE_OFF.name(), "ONE_OFF");
        assertEquals(MandateType.RECURRENT.name(), "RECURRENT");

        MandateType mandateTypeArray[] = new MandateType[] { MandateType.ONE_OFF, MandateType.RECURRENT };

        assertArrayEquals(mandateTypeArray, MandateType.values());
    }

    @Test
    public void testSettlementTypeEnum() {
        assertEquals(SettlementType.INSTANT.name(), "INSTANT");
        assertEquals(SettlementType.DELAYED.name(), "DELAYED");

        SettlementType settlementTypeArray[] = new SettlementType[] { SettlementType.INSTANT, SettlementType.DELAYED };

        assertArrayEquals(settlementTypeArray, SettlementType.values());
    }

}
