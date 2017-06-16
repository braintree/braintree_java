package com.braintreegateway.unittest;

import com.braintreegateway.Transaction;
import com.braintreegateway.util.SimpleNodeWrapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionTest {

	@Test
	public void fieldsWithUnrecognizedValuesAreCategorizedAsSuch() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<transaction>\n" +
				"  <id>unrecognized_transaction_id</id>\n" +
				"  <status>unrecognizable status</status>\n" +
				"  <type>sale</type>\n" +
				"  <currency-iso-code>USD</currency-iso-code>\n" +
				"  <amount>100.00</amount>\n" +
				"  <merchant-account-id>sandbox_sub_merchant_account</merchant-account-id>\n" +
				"  <sub-merchant-account-id>sandbox_sub_merchant_account</sub-merchant-account-id>\n" +
				"  <master-merchant-account-id>sandbox_master_merchant_account</master-merchant-account-id>\n" +
				"  <order-id nil=\"true\"/>\n" +
				"  <created-at type=\"datetime\">2017-06-15T19:15:43Z</created-at>\n" +
				"  <updated-at type=\"datetime\">2017-06-15T19:15:43Z</updated-at>\n" +
				"  <customer>\n" +
				"    <id nil=\"true\"/>\n" +
				"    <first-name nil=\"true\"/>\n" +
				"    <last-name nil=\"true\"/>\n" +
				"    <company nil=\"true\"/>\n" +
				"    <email nil=\"true\"/>\n" +
				"    <website nil=\"true\"/>\n" +
				"    <phone nil=\"true\"/>\n" +
				"    <fax nil=\"true\"/>\n" +
				"  </customer>\n" +
				"  <billing>\n" +
				"    <id nil=\"true\"/>\n" +
				"    <first-name nil=\"true\"/>\n" +
				"    <last-name nil=\"true\"/>\n" +
				"    <company nil=\"true\"/>\n" +
				"    <street-address nil=\"true\"/>\n" +
				"    <extended-address nil=\"true\"/>\n" +
				"    <locality nil=\"true\"/>\n" +
				"    <region nil=\"true\"/>\n" +
				"    <postal-code nil=\"true\"/>\n" +
				"    <country-name nil=\"true\"/>\n" +
				"    <country-code-alpha2 nil=\"true\"/>\n" +
				"    <country-code-alpha3 nil=\"true\"/>\n" +
				"    <country-code-numeric nil=\"true\"/>\n" +
				"  </billing>\n" +
				"  <refund-id nil=\"true\"/>\n" +
				"  <refund-ids type=\"array\"/>\n" +
				"  <refunded-transaction-id nil=\"true\"/>\n" +
				"  <partial-settlement-transaction-ids type=\"array\"/>\n" +
				"  <authorized-transaction-id nil=\"true\"/>\n" +
				"  <settlement-batch-id nil=\"true\"/>\n" +
				"  <shipping>\n" +
				"    <id nil=\"true\"/>\n" +
				"    <first-name nil=\"true\"/>\n" +
				"    <last-name nil=\"true\"/>\n" +
				"    <company nil=\"true\"/>\n" +
				"    <street-address nil=\"true\"/>\n" +
				"    <extended-address nil=\"true\"/>\n" +
				"    <locality nil=\"true\"/>\n" +
				"    <region nil=\"true\"/>\n" +
				"    <postal-code nil=\"true\"/>\n" +
				"    <country-name nil=\"true\"/>\n" +
				"    <country-code-alpha2 nil=\"true\"/>\n" +
				"    <country-code-alpha3 nil=\"true\"/>\n" +
				"    <country-code-numeric nil=\"true\"/>\n" +
				"  </shipping>\n" +
				"  <custom-fields/>\n" +
				"  <avs-error-response-code nil=\"true\"/>\n" +
				"  <avs-postal-code-response-code>M</avs-postal-code-response-code>\n" +
				"  <avs-street-address-response-code>M</avs-street-address-response-code>\n" +
				"  <cvv-response-code>U</cvv-response-code>\n" +
				"  <gateway-rejection-reason>unrecognizable gateway rejection reason</gateway-rejection-reason>\n" +
				"  <processor-authorization-code nil=\"true\"/>\n" +
				"  <processor-response-code></processor-response-code>\n" +
				"  <processor-response-text>Unknown ()</processor-response-text>\n" +
				"  <additional-processor-response nil=\"true\"/>\n" +
				"  <voice-referral-number nil=\"true\"/>\n" +
				"  <purchase-order-number nil=\"true\"/>\n" +
				"  <tax-amount nil=\"true\"/>\n" +
				"  <tax-exempt type=\"boolean\">false</tax-exempt>\n" +
				"  <credit-card>\n" +
				"    <token nil=\"true\"/>\n" +
				"    <bin>401288</bin>\n" +
				"    <last-4>1881</last-4>\n" +
				"    <card-type>Visa</card-type>\n" +
				"    <expiration-month>10</expiration-month>\n" +
				"    <expiration-year>2017</expiration-year>\n" +
				"    <customer-location>US</customer-location>\n" +
				"    <cardholder-name nil=\"true\"/>\n" +
				"    <image-url>https://assets.braintreegateway.com/payment_method_logo/visa.png?environment=development</image-url>\n" +
				"    <prepaid>No</prepaid>\n" +
				"    <healthcare>Unknown</healthcare>\n" +
				"    <debit>Unknown</debit>\n" +
				"    <durbin-regulated>Unknown</durbin-regulated>\n" +
				"    <commercial>Unknown</commercial>\n" +
				"    <payroll>Unknown</payroll>\n" +
				"    <issuing-bank>Unknown</issuing-bank>\n" +
				"    <country-of-issuance></country-of-issuance>\n" +
				"    <product-id>Unknown</product-id>\n" +
				"    <unique-number-identifier nil=\"true\"/>\n" +
				"    <venmo-sdk type=\"boolean\">false</venmo-sdk>\n" +
				"  </credit-card>\n" +
				"  <status-history type=\"array\">\n" +
				"    <status-event>\n" +
				"      <timestamp type=\"datetime\">2017-06-15T19:15:43Z</timestamp>\n" +
				"      <status>unrecognizable status</status>\n" +
				"      <amount>100.00</amount>\n" +
				"      <user nil=\"true\"/>\n" +
				"      <transaction-source>recurring</transaction-source>\n" +
				"    </status-event>\n" +
				"  </status-history>\n" +
				"  <plan-id nil=\"true\"/>\n" +
				"  <subscription-id nil=\"true\"/>\n" +
				"  <subscription>\n" +
				"    <billing-period-end-date nil=\"true\"/>\n" +
				"    <billing-period-start-date nil=\"true\"/>\n" +
				"  </subscription>\n" +
				"  <add-ons type=\"array\"/>\n" +
				"  <discounts type=\"array\"/>\n" +
				"  <descriptor>\n" +
				"    <name nil=\"true\"/>\n" +
				"    <phone nil=\"true\"/>\n" +
				"    <url nil=\"true\"/>\n" +
				"  </descriptor>\n" +
				"  <recurring nil=\"true\"/>\n" +
				"  <channel nil=\"true\"/>\n" +
				"  <service-fee-amount nil=\"true\"/>\n" +
				"  <escrow-status>unrecognizable escrow status</escrow-status>\n" +
				"  <disbursement-details>\n" +
				"    <disbursement-date nil=\"true\"/>\n" +
				"    <settlement-amount nil=\"true\"/>\n" +
				"    <settlement-currency-iso-code nil=\"true\"/>\n" +
				"    <settlement-currency-exchange-rate nil=\"true\"/>\n" +
				"    <funds-held nil=\"true\"/>\n" +
				"    <success nil=\"true\"/>\n" +
				"  </disbursement-details>\n" +
				"  <disputes type=\"array\"/>\n" +
				"  <payment-instrument-type>credit_card</payment-instrument-type>\n" +
				"  <processor-settlement-response-code></processor-settlement-response-code>\n" +
				"  <processor-settlement-response-text></processor-settlement-response-text>\n" +
				"  <three-d-secure-info nil=\"true\"/>\n" +
				"</transaction>\n";

		SimpleNodeWrapper creditCardNode = SimpleNodeWrapper.parse(xml);
		Transaction transaction = new Transaction(creditCardNode);

		assertEquals(Transaction.GatewayRejectionReason.UNRECOGNIZED, transaction.getGatewayRejectionReason());
		assertEquals(Transaction.EscrowStatus.UNRECOGNIZED, transaction.getEscrowStatus());
		assertEquals(Transaction.Status.UNRECOGNIZED, transaction.getStatus());
	}
}
