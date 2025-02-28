package com.braintreegateway.unittest;

import com.braintreegateway.ApplePayCard;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApplePayCardTest {
    @Test
    public void testApplePayMpanCard() {
        String xml = 
                "<payment-method>" +
                    "<merchant-token-identifier> merchant-token-123 </merchant-token-identifier>" +
                    "<source-card-last4> 1234 </source-card-last4>" +
                "</payment-method>";

        SimpleNodeWrapper applePayCardNode = SimpleNodeWrapper.parse(xml);
        ApplePayCard card = new ApplePayCard(applePayCardNode);
        
        assertEquals("merchant-token-123", card.getMerchantTokenIdentifier());
        assertEquals("1234", card.getSourceCardLast4());
    }

    @Test
    public void testPrepaidReloadable() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<payment-method>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("</payment-method>");


        SimpleNodeWrapper applePayCardNode = SimpleNodeWrapper.parse(builder.toString());
        ApplePayCard card = new ApplePayCard(applePayCardNode);

        assertEquals("No", card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
    }
}
