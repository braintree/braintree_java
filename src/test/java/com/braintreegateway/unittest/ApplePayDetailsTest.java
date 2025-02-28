package com.braintreegateway.unittest;

import com.braintreegateway.ApplePayDetails;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApplePayDetailsTest {

    @Test
    public void testPrepaidReloadable() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<transaction>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("</transaction>");

        SimpleNodeWrapper applePayDetailsNode = SimpleNodeWrapper.parse(builder.toString());
        ApplePayDetails card = new ApplePayDetails(applePayDetailsNode);

        assertEquals("No", card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
    }
}
