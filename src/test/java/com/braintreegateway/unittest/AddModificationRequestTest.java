package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AddModificationRequest;
import com.braintreegateway.ModificationsRequest;
import com.braintreegateway.SubscriptionRequest;

public class AddModificationRequestTest {

    @Test
    public void buildsXmlForAllFields() {
        ModificationsRequest parent = new ModificationsRequest(new SubscriptionRequest(), "addOns");
        AddModificationRequest request = new AddModificationRequest(parent);

        request.amount(new BigDecimal("5.00"))
               .inheritedFromId("a-base-id")
               .neverExpires(true)
               .numberOfBillingCycles(6)
               .quantity(2);

        String xml = request.toXML();
        assertTrue(xml.contains("<inheritedFromId>a-base-id</inheritedFromId>"), xml);
        assertTrue(xml.contains("<amount>5.00</amount>"), xml);
        assertTrue(xml.contains("<neverExpires>true</neverExpires>"), xml);
        assertTrue(xml.contains("<numberOfBillingCycles>6</numberOfBillingCycles>"), xml);
        assertTrue(xml.contains("<quantity>2</quantity>"), xml);
    }
}
