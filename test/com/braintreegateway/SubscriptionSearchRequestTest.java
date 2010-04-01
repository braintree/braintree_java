package com.braintreegateway;

import junit.framework.Assert;

import org.junit.Test;

public class SubscriptionSearchRequestTest {
    @Test
    public void daysPastDueXmlIsOperator() {
        String expected = "<search><days_past_due><is>42</is></days_past_due></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().is("42").toXML());
    }
    
    @Test
    public void daysPastDueXmlIsNotOperator() {
        String expected = "<search><days_past_due><is_not>42</is_not></days_past_due></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().isNot("42").toXML());
    }
    
    @Test
    public void daysPastDueXmlStartsWithOperator() {
        String expected = "<search><days_past_due><starts_with>42</starts_with></days_past_due></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().startsWith("42").toXML());
    }
    
    @Test
    public void daysPastDueXmlEndsWithOperator() {
        String expected = "<search><days_past_due><ends_with>42</ends_with></days_past_due></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().endsWith("42").toXML());
    }
    
    @Test
    public void daysPastDueXmlContainsOperator() {
        String expected = "<search><days_past_due><contains>42</contains></days_past_due></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().contains("42").toXML());
    }
}
