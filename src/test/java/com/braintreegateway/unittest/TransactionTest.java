package com.braintreegateway.unittest;

import com.braintreegateway.Transaction;
import com.braintreegateway.util.SimpleNodeWrapper;

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
}
