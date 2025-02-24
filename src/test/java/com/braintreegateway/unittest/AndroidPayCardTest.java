package com.braintreegateway.unittest;

import com.braintreegateway.AndroidPayCard;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AndroidPayCardTest {

    @Test
    public void testPrepaidReloadable() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<payment-method>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("</payment-method>");


        SimpleNodeWrapper androidPayCardNode = SimpleNodeWrapper.parse(builder.toString());
        AndroidPayCard card = new AndroidPayCard(androidPayCardNode);

        assertEquals("No", card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
    }
}
