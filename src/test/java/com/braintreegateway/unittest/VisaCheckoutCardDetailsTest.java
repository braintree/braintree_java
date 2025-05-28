package com.braintreegateway.unittest;

import com.braintreegateway.VisaCheckoutCardDetails;
import com.braintreegateway.CreditCard;
import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisaCheckoutCardDetailsTest {

    @Test
    public void testBinFields() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<transaction>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("<business>Yes</business>");
        builder.append("<consumer>No</consumer>");
        builder.append("<corporate>No</corporate>");
        builder.append("<purchase>No</purchase>");
        builder.append("</transaction>");

        SimpleNodeWrapper visaCheckoutCardDetailsNode = SimpleNodeWrapper.parse(builder.toString());
        VisaCheckoutCardDetails card = new VisaCheckoutCardDetails(visaCheckoutCardDetailsNode);

        assertEquals(CreditCard.Prepaid.NO, card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
        assertEquals(Business.YES, card.getBusiness());
        assertEquals(Consumer.NO, card.getConsumer());
        assertEquals(Corporate.NO, card.getCorporate());
        assertEquals(Purchase.NO, card.getPurchase());
    }
}
