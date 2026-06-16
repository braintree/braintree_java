package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.SubscriptionOptionsRequest;
import com.braintreegateway.SubscriptionRequest;

public class SubscriptionOptionsRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        SubscriptionRequest parent = new SubscriptionRequest();
        SubscriptionOptionsRequest request = new SubscriptionOptionsRequest(parent);

        SubscriptionRequest returned = request
                .doNotInheritAddOnsOrDiscounts(true)
                .prorateCharges(true)
                .replaceAllAddOnsAndDiscounts(false)
                .revertSubscriptionOnProrationFailure(true)
                .startImmediately(false)
                .done();

        assertSame(parent, returned);
        // Exercise the nested paypal() builder so it is represented in the options XML.
        request.paypal();

        String xml = request.toXML();
        assertAll("subscription options xml",
                () -> assertTrue(xml.contains("<options>"), xml),
                () -> assertTrue(xml.contains("<doNotInheritAddOnsOrDiscounts>true</doNotInheritAddOnsOrDiscounts>"), xml),
                () -> assertTrue(xml.contains("<prorateCharges>true</prorateCharges>"), xml),
                () -> assertTrue(xml.contains("<replaceAllAddOnsAndDiscounts>false</replaceAllAddOnsAndDiscounts>"), xml),
                () -> assertTrue(xml.contains("<revertSubscriptionOnProrationFailure>true</revertSubscriptionOnProrationFailure>"), xml),
                () -> assertTrue(xml.contains("<startImmediately>false</startImmediately>"), xml),
                () -> assertTrue(xml.contains("<paypal>"), xml));
    }
}
