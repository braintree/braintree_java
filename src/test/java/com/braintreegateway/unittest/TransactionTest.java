package com.braintreegateway.unittest;

import com.braintreegateway.Transaction;
import com.braintreegateway.util.SimpleNodeWrapper;
import com.braintreegateway.SepaDirectDebitAccountDetails;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

	@Test
	public void fieldsWithUnrecognizedValuesAreCategorizedAsSuch() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>unrecognized_transaction_id</id>\n" +
				"  <status>unrecognizable status</status>\n" +
				"  <type>sale</type>\n" +
				"  <customer></customer>\n" +
				"  <billing></billing>\n" +
				"  <shipping></shipping>\n" +
				"  <custom-fields/>\n" +
				"  <gateway-rejection-reason>unrecognizable gateway rejection reason</gateway-rejection-reason>\n" +
				"  <credit-card></credit-card>\n" +
				"  <status-history type=\"array\"></status-history>\n" +
				"  <subscription></subscription>\n" +
				"  <descriptor></descriptor>\n" +
				"  <escrow-status>unrecognizable escrow status</escrow-status>\n" +
				"  <disbursement-details></disbursement-details>\n" +
				"  <payment-instrument-type>credit_card</payment-instrument-type>\n" +
				"</transaction>\n";

		SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(transactionNode);

		assertEquals(Transaction.GatewayRejectionReason.UNRECOGNIZED, transaction.getGatewayRejectionReason());
		assertEquals(Transaction.EscrowStatus.UNRECOGNIZED, transaction.getEscrowStatus());
		assertEquals(Transaction.Status.UNRECOGNIZED, transaction.getStatus());
	}

	@Test
	public void recognizesExcessiveRetryGatewayRejectReason() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>unrecognized_transaction_id</id>\n" +
				"  <status></status>\n" +
				"  <type>sale</type>\n" +
				"  <customer></customer>\n" +
				"  <billing></billing>\n" +
				"  <shipping></shipping>\n" +
				"  <custom-fields/>\n" +
				"  <gateway-rejection-reason>excessive_retry</gateway-rejection-reason>\n" +
				"  <credit-card></credit-card>\n" +
				"  <status-history type=\"array\"></status-history>\n" +
				"  <subscription></subscription>\n" +
				"  <descriptor></descriptor>\n" +
				"  <escrow-status></escrow-status>\n" +
				"  <disbursement-details></disbursement-details>\n" +
				"  <payment-instrument-type>credit_card</payment-instrument-type>\n" +
				"</transaction>\n";

		SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(transactionNode);

		assertEquals(Transaction.GatewayRejectionReason.EXCESSIVE_RETRY, transaction.getGatewayRejectionReason());
	}

	@Test
	public void recognizesTokenIssuanceGatewayRejectReason() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>unrecognized_transaction_id</id>\n" +
				"  <status></status>\n" +
				"  <type>sale</type>\n" +
				"  <customer></customer>\n" +
				"  <billing></billing>\n" +
				"  <shipping></shipping>\n" +
				"  <custom-fields/>\n" +
				"  <gateway-rejection-reason>token_issuance</gateway-rejection-reason>\n" +
				"  <credit-card></credit-card>\n" +
				"  <status-history type=\"array\"></status-history>\n" +
				"  <subscription></subscription>\n" +
				"  <descriptor></descriptor>\n" +
				"  <escrow-status></escrow-status>\n" +
				"  <disbursement-details></disbursement-details>\n" +
				"  <payment-instrument-type>credit_card</payment-instrument-type>\n" +
				"</transaction>\n";

		SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(transactionNode);

		assertEquals(Transaction.GatewayRejectionReason.TOKEN_ISSUANCE, transaction.getGatewayRejectionReason());
	}

	@Test
	public void parsesNetworkResponseCodeAndText() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>recognized_transaction_id</id>\n" +
				"  <status></status>\n" +
				"  <type>sale</type>\n" +
				"  <customer></customer>\n" +
				"  <billing></billing>\n" +
				"  <shipping></shipping>\n" +
				"  <custom-fields/>\n" +
				"  <credit-card></credit-card>\n" +
				"  <status-history type=\"array\"></status-history>\n" +
				"  <subscription></subscription>\n" +
				"  <descriptor></descriptor>\n" +
				"  <escrow-status></escrow-status>\n" +
				"  <disbursement-details></disbursement-details>\n" +
				"  <payment-instrument-type>credit_card</payment-instrument-type>\n" +
				"  <network-response-code>11</network-response-code>\n" +
				"  <network-response-text>Approved</network-response-text>\n" +
				"</transaction>\n";

		SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(transactionNode);

		assertEquals("11", transaction.getNetworkResponseCode());
		assertEquals("Approved", transaction.getNetworkResponseText());
	}

	@Test
	public void parseAchReturnCode() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>recognized_transaction_id</id>\n" +
				"  <status></status>\n" +
				"  <type>sale</type>\n" +
				"  <customer></customer>\n" +
				"  <billing></billing>\n" +
				"  <shipping></shipping>\n" +
				"  <custom-fields/>\n" +
				"  <credit-card></credit-card>\n" +
				"  <status-history type=\"array\"></status-history>\n" +
				"  <subscription></subscription>\n" +
				"  <descriptor></descriptor>\n" +
				"  <escrow-status></escrow-status>\n" +
				"  <disbursement-details></disbursement-details>\n" +
				"  <payment-instrument-type>credit_card</payment-instrument-type>\n" +
				"  <ach-return-code>R01</ach-return-code>\n" +
				"</transaction>\n";

		SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(transactionNode);

		assertEquals("R01", transaction.getAchReturnCode());
	}

	@Test
	public void parseSepaDirectDebitReturnCode() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>recognized_transaction_id</id>\n" +
				"  <status></status>\n" +
				"  <type>sale</type>\n" +
				"  <customer></customer>\n" +
				"  <billing></billing>\n" +
				"  <shipping></shipping>\n" +
				"  <custom-fields/>\n" +
				"  <credit-card></credit-card>\n" +
				"  <status-history type=\"array\"></status-history>\n" +
				"  <subscription></subscription>\n" +
				"  <descriptor></descriptor>\n" +
				"  <payment-instrument-type>sepa_debit_account</payment-instrument-type>\n" +
				"  <sepa-direct-debit-return-code>AM04</sepa-direct-debit-return-code>\n" +
				"</transaction>\n";

		SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(transactionNode);

		assertEquals("AM04", transaction.getSepaDirectDebitReturnCode());
	}

	@Test
	public void parseSepaDirectDebitAccountDetails() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>recognized_transaction_id</id>\n" +
				"  <type>sale</type>\n" +
				"  <payment-instrument-type>sepa_debit_account</payment-instrument-type>\n" +
                "  <sepa-debit-account-detail>\n" +
                "    <bank-reference-token>123456789</bank-reference-token>\n" +
                "    <capture-id>ch6xxx123</capture-id>\n" +
                "    <merchant-or-partner-customer-id>123456789</merchant-or-partner-customer-id>\n" +
                "    <debug-id>ABC123</debug-id>\n" +
                "    <last-4>1234</last-4>\n" +
                "    <mandate-type>ONE_OFF</mandate-type>\n" +
                "    <paypal-v2-order-id>123456</paypal-v2-order-id>\n" +
                "    <refund-from-transaction-fee-amount>1.34</refund-from-transaction-fee-amount>\n" +
                "    <refund-from-transaction-fee-currency-iso-code>EUR</refund-from-transaction-fee-currency-iso-code>\n" +
                "    <refund-id>caaaxxx123</refund-id>\n" +
                "    <settlement-type>instant</settlement-type>\n" +
                "    <token>ch6byss</token>\n" +
                "    <transaction-fee-amount>12.34</transaction-fee-amount>\n" +
                "    <transaction-fee-currency-iso-code>EUR</transaction-fee-currency-iso-code>\n" +
                "  </sepa-debit-account-detail>\n" +
				"</transaction>";

		SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(transactionNode);

        SepaDirectDebitAccountDetails sepaDirectDebitAccountDetails = transaction.getSepaDirectDebitAccountDetails();

        assertEquals("123456789", sepaDirectDebitAccountDetails.getBankReferenceToken());
        assertEquals("ch6xxx123", sepaDirectDebitAccountDetails.getCaptureId());
        assertEquals("123456789", sepaDirectDebitAccountDetails.getMerchantOrPartnerCustomerId());
        assertEquals("ABC123", sepaDirectDebitAccountDetails.getDebugId());
        assertEquals("1234", sepaDirectDebitAccountDetails.getLast4());
        assertEquals("ONE_OFF", sepaDirectDebitAccountDetails.getMandateType().name());
        assertEquals("123456", sepaDirectDebitAccountDetails.getPayPalV2OrderId());
        assertEquals("1.34", sepaDirectDebitAccountDetails.getRefundFromTransactionFeeAmount());
        assertEquals("EUR", sepaDirectDebitAccountDetails.getRefundFromTransactionFeeCurrencyIsoCode());
        assertEquals("caaaxxx123", sepaDirectDebitAccountDetails.getRefundId());
        assertEquals("INSTANT", sepaDirectDebitAccountDetails.getSettlementType().name());
        assertEquals("ch6byss", sepaDirectDebitAccountDetails.getToken());
        assertEquals("12.34", sepaDirectDebitAccountDetails.getTransactionFeeAmount());
        assertEquals("EUR", sepaDirectDebitAccountDetails.getTransactionFeeCurrencyIsoCode());
	}
}
