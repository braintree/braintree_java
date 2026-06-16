package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CustomActionsPaymentMethodDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class CustomActionsPaymentMethodDetailsTest {

    @Test
    public void parsesAllFieldsIncludingNestedFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<custom-actions-payment-method>" +
                "<token>a-token</token>" +
                "<global-id>a-global-id</global-id>" +
                "<action-name>an-action</action-name>" +
                "<unique-number-identifier>a-uni</unique-number-identifier>" +
                "<fields type=\"array\">" +
                "<field><name>field-one</name><display-value>value-one</display-value></field>" +
                "<field><name>field-two</name><display-value>value-two</display-value></field>" +
                "</fields>" +
                "</custom-actions-payment-method>");

        CustomActionsPaymentMethodDetails details = new CustomActionsPaymentMethodDetails(node);

        assertAll("custom actions payment method fields",
                () -> assertEquals("a-token", details.getToken()),
                () -> assertEquals("a-global-id", details.getGlobalId()),
                () -> assertEquals("an-action", details.getActionName()),
                () -> assertEquals("a-uni", details.getUniqueNumberIdentifier()),
                () -> assertEquals(2, details.getFields().size()),
                () -> assertEquals("field-one", details.getFields().get(0).getName()),
                () -> assertEquals("value-one", details.getFields().get(0).getDisplayValue()),
                () -> assertEquals("field-two", details.getFields().get(1).getName()));
    }

    @Test
    public void handlesMissingFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<custom-actions-payment-method><token>a-token</token></custom-actions-payment-method>");

        CustomActionsPaymentMethodDetails details = new CustomActionsPaymentMethodDetails(node);

        assertEquals("a-token", details.getToken());
        assertTrue(details.getFields().isEmpty());
    }
}
