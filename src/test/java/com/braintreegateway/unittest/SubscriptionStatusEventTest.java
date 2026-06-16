package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Subscription;
import com.braintreegateway.SubscriptionStatusEvent;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

public class SubscriptionStatusEventTest {

    @Test
    public void parsesAllFields() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<status-event>" +
                "<balance>10.00</balance>" +
                "<currency-iso-code>USD</currency-iso-code>" +
                "<plan-id>a-plan-id</plan-id>" +
                "<price>9.99</price>" +
                "<subscription-source>api</subscription-source>" +
                "<status>Active</status>" +
                "<timestamp type=\"datetime\">2018-10-11T21:28:37Z</timestamp>" +
                "<user>an-admin</user>" +
                "</status-event>");

        SubscriptionStatusEvent event = new SubscriptionStatusEvent(node);

        assertAll("subscription status event fields",
                () -> assertEquals(new BigDecimal("10.00"), event.getBalance()),
                () -> assertEquals("USD", event.getCurrencyIsoCode()),
                () -> assertEquals("a-plan-id", event.getPlanId()),
                () -> assertEquals(new BigDecimal("9.99"), event.getPrice()),
                () -> assertEquals(Subscription.Source.API, event.getSource()),
                () -> assertEquals(Subscription.Status.ACTIVE, event.getStatus()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), event.getTimestamp()),
                () -> assertEquals("an-admin", event.getUser()));
    }

    @Test
    public void mapsUnrecognizedSourceAndStatus() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<status-event>" +
                "<subscription-source>not-real</subscription-source>" +
                "<status>not-real</status>" +
                "</status-event>");

        SubscriptionStatusEvent event = new SubscriptionStatusEvent(node);

        assertAll("unrecognized enums",
                () -> assertEquals(Subscription.Source.UNRECOGNIZED, event.getSource()),
                () -> assertEquals(Subscription.Status.UNRECOGNIZED, event.getStatus()));
    }
}
