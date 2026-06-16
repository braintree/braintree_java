package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AmexExpressCheckoutDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

@SuppressWarnings("deprecation")
public class AmexExpressCheckoutDetailsTest {

    @Test
    public void parsesAllFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<amex-express-checkout-card>" +
                "<bin>371260</bin>" +
                "<card-member-expiry-date>2025-12</card-member-expiry-date>" +
                "<card-member-number>1234567890</card-member-number>" +
                "<card-type>American Express</card-type>" +
                "<expiration-month>12</expiration-month>" +
                "<expiration-year>2025</expiration-year>" +
                "<image-url>https://example.com/amex.png</image-url>" +
                "<source-description>AmEx 1234</source-description>" +
                "<token>a-token</token>" +
                "</amex-express-checkout-card>");

        AmexExpressCheckoutDetails details = new AmexExpressCheckoutDetails(node);

        assertAll("amex express checkout fields",
                () -> assertEquals("371260", details.getBin()),
                () -> assertEquals("2025-12", details.getCardMemberExpiryDate()),
                () -> assertEquals("1234567890", details.getCardMemberNumber()),
                () -> assertEquals("American Express", details.getCardType()),
                () -> assertEquals("12", details.getExpirationMonth()),
                () -> assertEquals("2025", details.getExpirationYear()),
                () -> assertEquals("https://example.com/amex.png", details.getImageUrl()),
                () -> assertEquals("AmEx 1234", details.getSourceDescription()),
                () -> assertEquals("a-token", details.getToken()));
    }
}
