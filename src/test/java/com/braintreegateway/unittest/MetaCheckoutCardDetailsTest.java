package com.braintreegateway.unittest;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.MetaCheckoutCard;
import com.braintreegateway.MetaCheckoutCardDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class MetaCheckoutCardDetailsTest {
    @Test
    public void testMetaCheckoutCardDetails() {

        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<transaction>");
        builder.append("<bin>a-bin</bin>");
        builder.append("<cardholder-name>Cardholder</cardholder-name>");
        builder.append("<card-type>Visa</card-type>");
        builder.append("<expiration-month>11</expiration-month>");
        builder.append("<expiration-year>2024</expiration-year>");
        builder.append("<token>token1</token>");
        builder.append("<created-at>2023-05-05T21:28:37Z</created-at>");
        builder.append("<updated-at>2023-05-05T21:28:37Z</updated-at>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<payroll>No</payroll>");
        builder.append("<debit>No</debit>");
        builder.append("<commercial>No</commercial>");
        builder.append("<healthcare>No</healthcare>");
        builder.append("<container-id>a-container-id</container-id>" );
        builder.append("<last-4>1234</last-4>");
        builder.append("</transaction>");

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(builder.toString());
        MetaCheckoutCardDetails card = new MetaCheckoutCardDetails(node);

        assertEquals("a-bin", card.getBin());
        assertEquals("Cardholder", card.getCardholderName());
        assertEquals("Visa", card.getCardType());
        assertEquals("NO", card.getPrepaid().name());
        assertEquals("NO", card.getDebit().name());
        assertEquals("NO", card.getPayroll().name());
        assertEquals("NO", card.getCommercial().name());
        assertEquals("NO", card.getHealthcare().name());
        assertEquals("a-container-id", card.getContainerId());
        assertEquals("1234", card.getLast4());
        assertEquals("11/2024", card.getExpirationDate());
        assertEquals("a-bin******1234", card.getMaskedNumber());
    }
}
