package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.VenmoAccountDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class VenmoAccountDetailsTest {

    @Test
    public void parsesAllFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<venmo-account>" +
                "<token>a-token</token>" +
                "<username>venmojoe</username>" +
                "<venmo-user-id>456</venmo-user-id>" +
                "<image-url>https://example.com/venmo.png</image-url>" +
                "<source-description>Venmo Account: venmojoe</source-description>" +
                "</venmo-account>");

        VenmoAccountDetails details = new VenmoAccountDetails(node);

        assertAll("venmo account fields",
                () -> assertEquals("a-token", details.getToken()),
                () -> assertEquals("venmojoe", details.getUsername()),
                () -> assertEquals("456", details.getVenmoUserId()),
                () -> assertEquals("https://example.com/venmo.png", details.getImageUrl()),
                () -> assertEquals("Venmo Account: venmojoe", details.getSourceDescription()));
    }
}
