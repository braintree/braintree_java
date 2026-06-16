package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.braintreegateway.SubscriptionSearchRequest;

public class MultipleValueOrTextNodeTest {

    @Test
    public void inWithListSerializesAsArrayCriteria() {
        SubscriptionSearchRequest search = new SubscriptionSearchRequest();
        search.planId().in(Arrays.asList("plan_one", "plan_two"));

        String xml = search.toXML();
        assertTrue(xml.contains("<plan_id type=\"array\">"), xml);
        assertTrue(xml.contains("<item>plan_one</item>"), xml);
        assertTrue(xml.contains("<item>plan_two</item>"), xml);
    }

    @Test
    public void inWithVarargsSerializesAsArrayCriteria() {
        SubscriptionSearchRequest search = new SubscriptionSearchRequest();
        search.planId().in("plan_one", "plan_two");

        String xml = search.toXML();
        assertTrue(xml.contains("<plan_id type=\"array\">"), xml);
        assertTrue(xml.contains("<item>plan_one</item>"), xml);
        assertTrue(xml.contains("<item>plan_two</item>"), xml);
    }

    @Test
    public void isFromTextNodeSerializesAsIsCriteria() {
        SubscriptionSearchRequest search = new SubscriptionSearchRequest();
        search.planId().is("plan_one");

        String xml = search.toXML();
        assertTrue(xml.contains("<plan_id><is>plan_one</is></plan_id>"), xml);
    }
}
