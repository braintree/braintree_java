package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.MetaCheckoutCardDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class MetaCheckoutCardDetailsTest {
    @Test
    public void testMetaCheckoutCardDetails() {

        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<transaction>");
        builder.append("<bin>a-bin</bin>");
        builder.append("<business>No</business>");
        builder.append("<card-type>Visa</card-type>");
        builder.append("<cardholder-name>Cardholder</cardholder-name>");
        builder.append("<commercial>No</commercial>");
        builder.append("<consumer>No</consumer>");
        builder.append("<corporate>No</corporate>");
        builder.append("<container-id>a-container-id</container-id>" );
        builder.append("<created-at>2023-05-05T21:28:37Z</created-at>");
        builder.append("<debit>No</debit>");
        builder.append("<expiration-month>11</expiration-month>");
        builder.append("<expiration-year>2024</expiration-year>");
        builder.append("<healthcare>No</healthcare>");
        builder.append("<last-4>1234</last-4>");
        builder.append("<payroll>No</payroll>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("<purchase>No</purchase>");
        builder.append("<token>token1</token>");
        builder.append("<updated-at>2023-05-05T21:28:37Z</updated-at>");
        builder.append("</transaction>");

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(builder.toString());
        MetaCheckoutCardDetails card = new MetaCheckoutCardDetails(node);

        assertEquals("11/2024", card.getExpirationDate());
        assertEquals("1234", card.getLast4());
        assertEquals("Cardholder", card.getCardholderName());
        assertEquals("NO", card.getCommercial().name());
        assertEquals("NO", card.getDebit().name());
        assertEquals("NO", card.getHealthcare().name());
        assertEquals("NO", card.getPayroll().name());
        assertEquals("NO", card.getPrepaid().name());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
        assertEquals(Business.NO, card.getBusiness());
        assertEquals(Consumer.NO, card.getConsumer());
        assertEquals(Corporate.NO, card.getCorporate());
        assertEquals(Purchase.NO, card.getPurchase());
        assertEquals("Visa", card.getCardType());
        assertEquals("a-bin", card.getBin());
        assertEquals("a-bin******1234", card.getMaskedNumber());
    }
}
