package com.braintreegateway.unittest;

import com.braintreegateway.BinData;
import com.braintreegateway.CreditCard;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinDataTest {

    @Test
    public void testPrepaidReloadable() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<bin>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("</bin>");


        SimpleNodeWrapper binDataNode = SimpleNodeWrapper.parse(builder.toString());
        BinData card = new BinData(binDataNode);

        assertEquals(CreditCard.Prepaid.NO, card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
    }
}
