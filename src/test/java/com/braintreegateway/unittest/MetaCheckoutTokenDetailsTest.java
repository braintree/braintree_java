package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.MetaCheckoutTokenDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class MetaCheckoutTokenDetailsTest {
    @Test
    public void testMetaCheckoutTokenDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<transaction>");
        builder.append("<bin>a-bin</bin>");
        builder.append("<business>No</business>");
        builder.append("<card-type>Visa</card-type>");
        builder.append("<cardholder-name>Cardholder</cardholder-name>");
        builder.append("<commercial>No</commercial>");
        builder.append("<consumer>No</consumer>");
        builder.append("<container-id>a-container-id</container-id>" );
        builder.append("<corporate>No</corporate>");
        builder.append("<created-at>2023-05-05T21:28:37Z</created-at>");
        builder.append("<cryptogram>AlhlvxmN2ZKuAAESNFZ4GoABFA==</cryptogram>");
        builder.append("<customer-location>US</customer-location>");
        builder.append("<debit>No</debit>");
        builder.append("<ecommerce-indicator>07</ecommerce-indicator>");
        builder.append("<expiration-month>11</expiration-month>");
        builder.append("<expiration-year>2024</expiration-year>");
        builder.append("<expired>false</expired>");
        builder.append("<healthcare>No</healthcare>");
        builder.append("<last-4>1234</last-4>");
        builder.append("<payroll>No</payroll>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("<purchase>No</purchase>");
        builder.append("<token>token1</token>");
        builder.append("<unique-number-identifier>1234</unique-number-identifier>");
        builder.append("<updated-at>2023-05-05T21:28:37Z</updated-at>");
        builder.append("</transaction>");

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(builder.toString());
        MetaCheckoutTokenDetails card = new MetaCheckoutTokenDetails(node);

        assertEquals("07", card.getEcommerceIndicator());
        assertEquals("11/2024", card.getExpirationDate());
        assertEquals("1234", card.getLast4());
        assertEquals("AlhlvxmN2ZKuAAESNFZ4GoABFA==", card.getCryptogram());
        assertEquals(Business.NO, card.getBusiness());
        assertEquals("Cardholder", card.getCardholderName());
        assertEquals("NO", card.getCommercial().name());
        assertEquals(Consumer.NO, card.getConsumer());
        assertEquals(Corporate.NO, card.getCorporate());
        assertEquals("NO", card.getDebit().name());
        assertEquals("NO", card.getHealthcare().name());
        assertEquals("NO", card.getPayroll().name());
        assertEquals("NO", card.getPrepaid().name());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
        assertEquals(Purchase.NO, card.getPurchase());
        assertEquals("Visa", card.getCardType());
        assertEquals("a-bin", card.getBin());
        assertEquals("a-bin******1234", card.getMaskedNumber());
        assertEquals("a-container-id", card.getContainerId());
    }
}
