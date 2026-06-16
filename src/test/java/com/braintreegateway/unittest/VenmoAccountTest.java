package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.braintreegateway.VenmoAccount;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

public class VenmoAccountTest {

    @Test
    public void parsesAllFieldsAndSubscriptions() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<venmo-account>" +
                "<token>a-token</token>" +
                "<username>venmojoe</username>" +
                "<venmo-user-id>456</venmo-user-id>" +
                "<source-description>Venmo Account: venmojoe</source-description>" +
                "<image-url>https://example.com/venmo.png</image-url>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<customer-id>a-customer-id</customer-id>" +
                "<default type=\"boolean\">true</default>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-id</id><transactions type=\"array\"/>" +
                "<add-ons type=\"array\"/><discounts type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</venmo-account>");

        VenmoAccount account = new VenmoAccount(node);

        assertAll("venmo account fields",
                () -> assertEquals("a-token", account.getToken()),
                () -> assertEquals("venmojoe", account.getUsername()),
                () -> assertEquals("456", account.getVenmoUserId()),
                () -> assertEquals("Venmo Account: venmojoe", account.getSourceDescription()),
                () -> assertEquals("https://example.com/venmo.png", account.getImageUrl()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), account.getCreatedAt()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-12T21:28:37Z"), account.getUpdatedAt()),
                () -> assertEquals("a-customer-id", account.getCustomerId()),
                () -> assertTrue(account.isDefault()),
                () -> assertEquals(1, account.getSubscriptions().size()),
                () -> assertEquals("sub-id", account.getSubscriptions().get(0).getId()));
    }
}
