package com.braintreegateway.unittest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.braintreegateway.Dispute;
import com.braintreegateway.util.SimpleNodeWrapper;
import java.math.BigDecimal;
import java.text.ParseException;
import com.braintreegateway.testhelpers.CalendarTestUtils;

public class DisputeTest {
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
        assertEquals("transaction_id", dispute.getTransactionDetails().getId());
    }
}
