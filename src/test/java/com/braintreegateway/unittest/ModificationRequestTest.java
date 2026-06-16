package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.ModificationRequest;
import com.braintreegateway.ModificationsRequest;
import com.braintreegateway.SubscriptionRequest;

public class ModificationRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        ModificationsRequest parent = new ModificationsRequest(new SubscriptionRequest(), "addOns");
        ModificationRequest request = new ModificationRequest(parent);

        ModificationsRequest returned = request
                .amount(new BigDecimal("5.00"))
                .neverExpires(true)
                .numberOfBillingCycles(6)
                .quantity(2)
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<amount>5.00</amount>"), xml);
        assertTrue(xml.contains("<neverExpires>true</neverExpires>"), xml);
        assertTrue(xml.contains("<numberOfBillingCycles>6</numberOfBillingCycles>"), xml);
        assertTrue(xml.contains("<quantity>2</quantity>"), xml);
    }
}
