package com.braintreegateway.unittest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;

import com.braintreegateway.Dispute;
import com.braintreegateway.util.SimpleNodeWrapper;
import com.braintreegateway.testhelpers.CalendarTestUtils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DisputeTest {

    //language=XML
    private static final String ATTRIBUTES = "<dispute>\n" +
        "<id>123456</id>\n" +
        "<amount>100.00</amount>\n" +
        "<amount-disputed>100.00</amount-disputed>\n" +
        "<amount-won>95.00</amount-won>\n " +
        "<case-number>CASE-12345</case-number>\n " +
        "<chargeback-protection-level>effortless</chargeback-protection-level>\n " +
        "<created-at type=\"datetime\">2017-06-16T20:44:41Z</created-at>\n " +
        "<currency-iso-code>USD</currency-iso-code>\n " +
        "<processor-comments>Forwarded comments</processor-comments>\n" +
        "<kind>chargeback</kind>\n " +
        "<merchant-account-id>abc123</merchant-account-id>\n " +
        "<reason>fraud</reason>\n " +
        "<reason-code>83</reason-code>\n " +
        "<reason-description>Reason code 83 description</reason-description>\n " +
        "<received-date type=\"date\">2016-02-15</received-date>\n " +
        "<reference-number>123456</reference-number>\n " +
        "<reply-by-date type=\"date\">2016-02-22</reply-by-date>\n " +
        "<status>open</status>\n " +
        "<updated-at type=\"datetime\">2013-04-10T10:50:39Z</updated-at>\n " +
        "<original-dispute-id>original_dispute_id</original-dispute-id>\n " +
        "<status-history type=\"array\">\n " +
        "  <status-history>\n " +
        "    <status>open</status>\n " +
        "    <timestamp type=\"datetime\">2013-04-10T10:50:39Z</timestamp>\n " +
        "    <effective-date type=\"date\">2013-04-10</effective-date>\n " +
        "  </status-history>\n " +
        "</status-history>\n " +
        "<paypal-messages type=\"array\">\n " +
        "  <paypal-messages>\n " +
        "    <message>message</message>\n " +
        "    <sender>seller</sender>\n " +
        "    <sent-at type=\"datetime\">2013-04-10T10:50:39Z</sent-at>\n " +
        "  </paypal-messages>\n " +
        "</paypal-messages>\n " +
        "<evidence type=\"array\">\n " +
        "  <evidence>\n" +
        "    <created-at type=\"datetime\">2013-04-11T10:50:39Z</created-at>\n " +
        "    <id>evidence1</id>\n " +
        "    <url>url_of_file_evidence</url>\n " +
        "  </evidence>\n" +
        "  <evidence>\n" +
        "    <created-at type=\"datetime\">2013-04-11T10:50:39Z</created-at>\n " +
        "    <id>evidence2</id>\n " +
        "    <comment>text evidence</comment>\n " +
        "    <sent-to-processor-at type=\"date\">2009-04-11</sent-to-processor-at>\n " +
        "  </evidence>\n " +
        "</evidence>\n " +
        "<transaction>\n " +
        "  <id>123456</id>\n " +
        "  <amount>100.00</amount>\n " +
        "  <created-at>2017-06-21T20:44:41Z</created-at>\n " +
        "  <installment-count nil=\"true\"/>\n " +
        "  <order-id nil=\"true\"/>\n " +
        "  <purchase-order-number nil=\"true\"/>\n " +
        "  <payment-instrument-subtype>Visa</payment-instrument-subtype>\n " +
        "</transaction>\n " +
        "<date-opened type=\"date\">2014-03-28</date-opened>\n " +
        "<date-won type=\"date\">2014-04-05</date-won>\n " +
        "</dispute>";

    @Test
    public void includesFields() throws ParseException {
        String xml = "<dispute>" +
            "<received-date type=\"date\">2017-01-02</received-date>" +
            "<reply-by-date type=\"date\">2017-01-04</reply-by-date>" +
            "<date-opened type=\"date\">2017-01-02</date-opened>" +
            "<date-won type=\"date\">2017-01-03</date-won>" +
            "<currency-iso-code>GBP</currency-iso-code>" +
            "<reason>fraud</reason>" +
            "<status>won</status>" +
            "<kind>chargeback</kind>" +
            "<amount>100.00</amount>" +
            "<id>dispute_id</id>" +
            "<transaction>" +
            "  <id>transaction_id</id>" +
            "  <amount>500.00</amount>" +
            "</transaction>" +
            "</dispute>";
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

        Dispute dispute = new Dispute(node);

        assertEquals(CalendarTestUtils.date("2017-01-02"), dispute.getReceivedDate());
        assertEquals(CalendarTestUtils.date("2017-01-04"), dispute.getReplyByDate());
        assertEquals(CalendarTestUtils.date("2017-01-02"), dispute.getOpenedDate());
        assertEquals(CalendarTestUtils.date("2017-01-03"), dispute.getWonDate());
        assertEquals(Dispute.Reason.FRAUD, dispute.getReason());
        assertEquals(Dispute.Status.WON, dispute.getStatus());
        assertEquals(Dispute.Kind.CHARGEBACK, dispute.getKind());
        assertEquals(new BigDecimal("100.00"), dispute.getAmount());
        assertEquals("GBP", dispute.getCurrencyIsoCode());
        assertEquals("dispute_id", dispute.getId());
        assertEquals("transaction_id", dispute.getTransaction().getId());
  }

  @Test
  public void legacyConstructor() {
        //language=XML
        String legacyXml = "<dispute>\n" +
            "<amount>250.00</amount>\n" +
            "<currency-iso-code>USD</currency-iso-code>\n" +
            "<received-date type=\"date\">2014-03-01</received-date>\n" +
            "<reply-by-date type=\"date\">2014-03-21</reply-by-date>\n" +
            "<kind>chargeback</kind>\n" +
            "<status>open</status>\n" +
            "<reason>fraud</reason>\n" +
            "<id>1234</id>\n" +
            "<transaction>\n" +
            "  <id>1234</id>\n" +
            "  <amount>250.00</amount>\n" +
            "</transaction>\n" +
            "<date-opened type=\"date\">2014-03-28</date-opened>\n" +
            "<date-won type=\"date\">2014-09-01</date-won>\n" +
            "</dispute>\n";

        SimpleNodeWrapper oldDisputeNode = SimpleNodeWrapper.parse(legacyXml);

        Dispute dispute = new Dispute(oldDisputeNode);

        assertEquals(dispute.getId(), "1234");
        assertEquals(dispute.getAmount(), new BigDecimal("250.00"));
        assertEquals(dispute.getCurrencyIsoCode(), "USD");
        assertEquals(dispute.getReason(), Dispute.Reason.FRAUD);
        assertEquals(dispute.getStatus(), Dispute.Status.OPEN);
        assertEquals(dispute.getTransaction().getId(), "1234");
        assertEquals(dispute.getTransaction().getAmount(), new BigDecimal("250.00"));
        assertEquals(dispute.getOpenedDate().get(Calendar.YEAR), 2014);
        assertEquals(dispute.getOpenedDate().get(Calendar.MONTH)+1, 3);
        assertEquals(dispute.getOpenedDate().get(Calendar.DAY_OF_MONTH), 28);
        assertEquals(dispute.getWonDate().get(Calendar.YEAR), 2014);
        assertEquals(dispute.getWonDate().get(Calendar.MONTH)+1, 9);
        assertEquals(dispute.getWonDate().get(Calendar.DAY_OF_MONTH), 1);
        assertEquals(dispute.getKind(), Dispute.Kind.CHARGEBACK);
    }

    @Test
    public void legacyConstructorWithNewAttributes() {
        SimpleNodeWrapper oldDisputeNode = SimpleNodeWrapper.parse(ATTRIBUTES);

        Dispute dispute = new Dispute(oldDisputeNode);

        assertEquals(dispute.getId(), "123456");
        assertEquals(dispute.getAmount(), new BigDecimal("100.00"));
        assertEquals(dispute.getCurrencyIsoCode(), "USD");
        assertEquals(dispute.getReason(), Dispute.Reason.FRAUD);
        assertEquals(dispute.getStatus(), Dispute.Status.OPEN);
        assertEquals(dispute.getTransaction().getId(), "123456");
        assertEquals(dispute.getTransaction().getAmount(), new BigDecimal("100.00"));
        assertEquals(dispute.getOpenedDate().get(Calendar.YEAR), 2014);
        assertEquals(dispute.getOpenedDate().get(Calendar.MONTH)+1, 3);
        assertEquals(dispute.getOpenedDate().get(Calendar.DAY_OF_MONTH), 28);
        assertEquals(dispute.getWonDate().get(Calendar.YEAR), 2014);
        assertEquals(dispute.getWonDate().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getWonDate().get(Calendar.DAY_OF_MONTH), 5);
        assertEquals(dispute.getKind(), Dispute.Kind.CHARGEBACK);
    }

    @Test
    public void constructorPopulatesNewFields() {
        SimpleNodeWrapper disputeNode = SimpleNodeWrapper.parse(ATTRIBUTES);

        Dispute dispute = new Dispute(disputeNode);

        assertEquals(dispute.getDisputedAmount(), new BigDecimal("100.00"));
        assertEquals(dispute.getWonAmount(), new BigDecimal("95.00"));
        assertEquals(dispute.getCaseNumber(), "CASE-12345");
        assertEquals(dispute.getCreatedAt().get(Calendar.YEAR), 2017);
        assertEquals(dispute.getCreatedAt().get(Calendar.MONTH)+1, 6);
        assertEquals(dispute.getCreatedAt().get(Calendar.DAY_OF_MONTH), 16);
        assertEquals(dispute.getCreatedAt().get(Calendar.HOUR), 8);
        assertEquals(dispute.getCreatedAt().get(Calendar.MINUTE), 44);
        assertEquals(dispute.getCreatedAt().get(Calendar.SECOND), 41);
        assertEquals(dispute.getProcessorComments(), "Forwarded comments");
        assertEquals(dispute.getMerchantAccountId(), "abc123");
        assertEquals(dispute.getOriginalDisputeId(), "original_dispute_id");
        assertEquals(dispute.getReasonCode(), "83");
        assertEquals(dispute.getReasonDescription(), "Reason code 83 description");
        assertEquals(dispute.getReferenceNumber(), "123456");
        assertEquals(dispute.getUpdatedAt().get(Calendar.YEAR), 2013);
        assertEquals(dispute.getUpdatedAt().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getUpdatedAt().get(Calendar.DAY_OF_MONTH), 10);
        assertEquals(dispute.getUpdatedAt().get(Calendar.HOUR), 10);
        assertEquals(dispute.getUpdatedAt().get(Calendar.MINUTE), 50);
        assertEquals(dispute.getUpdatedAt().get(Calendar.SECOND), 39);
        assertNull(dispute.getEvidence().get(0).getComment());
        assertEquals(dispute.getEvidence().get(0).getCreatedAt().get(Calendar.YEAR), 2013);
        assertEquals(dispute.getEvidence().get(0).getCreatedAt().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getEvidence().get(0).getCreatedAt().get(Calendar.DAY_OF_MONTH), 11);
        assertEquals(dispute.getEvidence().get(0).getCreatedAt().get(Calendar.HOUR), 10);
        assertEquals(dispute.getEvidence().get(0).getCreatedAt().get(Calendar.MINUTE), 50);
        assertEquals(dispute.getEvidence().get(0).getCreatedAt().get(Calendar.SECOND), 39);
        assertEquals(dispute.getEvidence().get(0).getId(), "evidence1");
        assertNull(dispute.getEvidence().get(0).getSentToProcessorAt());
        assertEquals(dispute.getEvidence().get(0).getUrl(), "url_of_file_evidence");
        assertEquals(dispute.getEvidence().get(1).getComment(), "text evidence");
        assertEquals(dispute.getEvidence().get(1).getCreatedAt().get(Calendar.YEAR), 2013);
        assertEquals(dispute.getEvidence().get(1).getCreatedAt().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getEvidence().get(1).getCreatedAt().get(Calendar.DAY_OF_MONTH), 11);
        assertEquals(dispute.getEvidence().get(1).getCreatedAt().get(Calendar.HOUR), 10);
        assertEquals(dispute.getEvidence().get(1).getCreatedAt().get(Calendar.MINUTE), 50);
        assertEquals(dispute.getEvidence().get(1).getCreatedAt().get(Calendar.SECOND), 39);
        assertEquals(dispute.getEvidence().get(1).getId(), "evidence2");
        assertEquals(dispute.getEvidence().get(1).getSentToProcessorAt().get(Calendar.YEAR), 2009);
        assertEquals(dispute.getEvidence().get(1).getSentToProcessorAt().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getEvidence().get(1).getSentToProcessorAt().get(Calendar.DAY_OF_MONTH), 11);
        assertNull(dispute.getEvidence().get(1).getUrl());
        assertEquals(dispute.getPayPalMessages().get(0).getMessage(), "message");
        assertEquals(dispute.getPayPalMessages().get(0).getSender(), "seller");
        assertEquals(dispute.getPayPalMessages().get(0).getSentAt().get(Calendar.YEAR), 2013);
        assertEquals(dispute.getPayPalMessages().get(0).getSentAt().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getPayPalMessages().get(0).getSentAt().get(Calendar.DAY_OF_MONTH), 10);
        assertEquals(dispute.getStatusHistory().get(0).getEffectiveDate().get(Calendar.YEAR), 2013);
        assertEquals(dispute.getStatusHistory().get(0).getEffectiveDate().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getStatusHistory().get(0).getEffectiveDate().get(Calendar.DAY_OF_MONTH), 10);
        assertEquals(dispute.getStatusHistory().get(0).getStatus(), Dispute.Status.OPEN);
        assertEquals(dispute.getStatusHistory().get(0).getTimestamp().get(Calendar.YEAR), 2013);
        assertEquals(dispute.getStatusHistory().get(0).getTimestamp().get(Calendar.MONTH)+1, 4);
        assertEquals(dispute.getStatusHistory().get(0).getTimestamp().get(Calendar.DAY_OF_MONTH), 10);
        assertEquals(dispute.getStatusHistory().get(0).getTimestamp().get(Calendar.HOUR), 10);
        assertEquals(dispute.getStatusHistory().get(0).getTimestamp().get(Calendar.MINUTE), 50);
        assertEquals(dispute.getStatusHistory().get(0).getTimestamp().get(Calendar.SECOND), 39);
        assertEquals(dispute.getChargebackProtectionLevel(), Dispute.ChargebackProtectionLevel.EFFORTLESS);
    }

    @Test
    public void constructorHandlesMissingFields() {
        //language=XML
        String xml = "<dispute>\n" +
            "  <transaction>\n" +
            "    <id>1234</id>\n" +
            "    <amount>250.00</amount>\n" +
            "  </transaction>\n" +
            "</dispute>";
        SimpleNodeWrapper disputeNode = SimpleNodeWrapper.parse(xml);

        Dispute dispute = new Dispute(disputeNode);

        assertNull(dispute.getAmount());
        assertNull(dispute.getOpenedDate());
        assertNull(dispute.getWonDate());
        assertTrue(dispute.getEvidence().isEmpty());
        assertTrue(dispute.getPayPalMessages().isEmpty());
        assertNull(dispute.getReplyByDate());
        assertTrue(dispute.getStatusHistory().isEmpty());
    }

    @Test
    public void constructorPopulatesTransaction() {
        //language=XML
        String xml = "<dispute>\n" +
            "  <transaction>\n " +
            "    <id>123456</id>\n " +
            "    <amount>100.00</amount>\n " +
            "    <created-at>2017-06-21T20:44:41Z</created-at>\n " +
            "    <installment-count>2</installment-count>\n " +
            "    <order-id>ORDER-A1</order-id>\n " +
            "    <purchase-order-number>PO-ABC</purchase-order-number>\n " +
            "    <payment-instrument-subtype>Visa</payment-instrument-subtype>\n " +
            "  </transaction>\n " +
            "</dispute>";
        SimpleNodeWrapper disputeNode = SimpleNodeWrapper.parse(xml);

        Dispute dispute = new Dispute(disputeNode);

        assertEquals(dispute.getTransaction().getId(), "123456");
        assertEquals(dispute.getTransaction().getAmount(), new BigDecimal("100.00"));
        assertEquals(dispute.getTransaction().getCreatedAt().get(Calendar.YEAR), 2017);
        assertEquals(dispute.getTransaction().getCreatedAt().get(Calendar.MONTH)+1, 6);
        assertEquals(dispute.getTransaction().getCreatedAt().get(Calendar.DAY_OF_MONTH), 21);
        assertEquals(dispute.getTransaction().getCreatedAt().get(Calendar.HOUR), 8);
        assertEquals(dispute.getTransaction().getCreatedAt().get(Calendar.MINUTE), 44);
        assertEquals(dispute.getTransaction().getCreatedAt().get(Calendar.SECOND), 41);
        assertEquals(dispute.getTransaction().getInstallmentCount(), Integer.valueOf(2));
        assertEquals(dispute.getTransaction().getOrderId(), "ORDER-A1");
        assertEquals(dispute.getTransaction().getPurchaseOrderNumber(), "PO-ABC");
        assertEquals(dispute.getTransaction().getPaymentInstrumentSubtype(), "Visa");
    }
}
