package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.braintreegateway.ModificationsRequest;
import com.braintreegateway.SubscriptionRequest;

public class ModificationsRequestTest {

    @Test
    public void buildsXmlForAddsUpdatesRemovesAndChainsBackToParent() {
        SubscriptionRequest parent = new SubscriptionRequest();
        ModificationsRequest request = new ModificationsRequest(parent, "addOns");

        request.add();
        request.update();
        request.update("existing-id");
        ModificationsRequest afterVarargsRemove = request.remove("remove-1", "remove-2");
        ModificationsRequest afterListRemove = request.remove(Arrays.asList("remove-3"));

        assertSame(request, afterVarargsRemove);
        assertSame(request, afterListRemove);
        assertSame(parent, request.done());

        String xml = request.toXML();
        // <addOns> is the root; the add/remove/update arrays are nested type="array" elements.
        assertTrue(xml.contains("<addOns>"), xml);
        assertTrue(xml.contains("<add type=\"array\">"), xml);
        assertTrue(xml.contains("<update type=\"array\">"), xml);
        assertTrue(xml.contains("<existingId>existing-id</existingId>"), xml);
        assertTrue(xml.contains("<remove type=\"array\">"), xml);
        assertTrue(xml.contains("<item>remove-1</item>"), xml);
        assertTrue(xml.contains("<item>remove-3</item>"), xml);
    }
}
