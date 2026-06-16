package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.braintreegateway.StatusEvent;
import com.braintreegateway.Transaction;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

public class StatusEventTest {

    @Test
    public void parsesAllFields() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<status-event>" +
                "<amount>50.00</amount>" +
                "<status>SETTLED</status>" +
                "<timestamp type=\"datetime\">2018-10-11T21:28:37Z</timestamp>" +
                "<transaction-source>api</transaction-source>" +
                "<user>an-admin</user>" +
                "</status-event>");

        StatusEvent event = new StatusEvent(node);

        assertAll("status event fields",
                () -> assertEquals(new BigDecimal("50.00"), event.getAmount()),
                () -> assertEquals(Transaction.Status.SETTLED, event.getStatus()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), event.getTimestamp()),
                () -> assertEquals(Transaction.Source.API, event.getSource()),
                () -> assertEquals("an-admin", event.getUser()));
    }

    @Test
    public void mapsUnrecognizedEnums() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<status-event>" +
                "<status>not-real</status>" +
                "<transaction-source>not-real</transaction-source>" +
                "</status-event>");

        StatusEvent event = new StatusEvent(node);

        assertAll("unrecognized enums",
                () -> assertEquals(Transaction.Status.UNRECOGNIZED, event.getStatus()),
                () -> assertEquals(Transaction.Source.UNRECOGNIZED, event.getSource()));
    }
}
