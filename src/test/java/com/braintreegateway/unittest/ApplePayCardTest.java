package com.braintreegateway.unittest;

import com.braintreegateway.ApplePayCard;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.Purchase;
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
    public void testBinFields() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<payment-method>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("<business>No</business>");
        builder.append("<consumer>No</consumer>");
        builder.append("<corporate>No</corporate>");
        builder.append("<purchase>No</purchase>");

        builder.append("</payment-method>");


        SimpleNodeWrapper applePayCardNode = SimpleNodeWrapper.parse(builder.toString());
        ApplePayCard card = new ApplePayCard(applePayCardNode);

        assertEquals("No", card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
        assertEquals(Business.NO, card.getBusiness());
        assertEquals(Consumer.NO, card.getConsumer());
        assertEquals(Corporate.NO, card.getCorporate());
        assertEquals(Purchase.NO, card.getPurchase());
    }

    @Test
    public void testVerificationIsTheLatestVerification() {
        String xml = "<apple-pay-card>"
                     + "<verifications>"
                     + "    <verification>"
                     + "        <created-at type=\"datetime\">2014-11-20T17:27:15Z</created-at>"
                     + "        <id>123</id>"
                     + "    </verification>"
                     + "    <verification>"
                     + "        <created-at type=\"datetime\">2014-11-20T17:27:18Z</created-at>"
                     + "        <id>932</id>"
                     + "    </verification>"
                     + "    <verification>"
                     + "        <created-at type=\"datetime\">2014-11-20T17:27:17Z</created-at>"
                     + "        <id>456</id>"
                     + "    </verification>"
                     + "</verifications>"
                   + "</apple-pay-card>";

        SimpleNodeWrapper applePayCardNode = SimpleNodeWrapper.parse(xml);
        ApplePayCard card = new ApplePayCard(applePayCardNode);

        assertEquals("932", card.getVerification().getId());
    }
}
