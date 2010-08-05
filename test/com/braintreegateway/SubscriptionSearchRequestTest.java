package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.Subscription.Status;

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

    @Test
    public void idXmlIsOperator() {
        String expected = "<search><id><is>42</is></id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().id().is("42").toXML());
    }

    @Test
    public void idXmlIsNotOperator() {
        String expected = "<search><id><is_not>42</is_not></id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().id().isNot("42").toXML());
    }

    @Test
    public void idXmlStartsWithOperator() {
        String expected = "<search><id><starts_with>42</starts_with></id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().id().startsWith("42").toXML());
    }

    @Test
    public void idXmlEndsWithOperator() {
        String expected = "<search><id><ends_with>42</ends_with></id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().id().endsWith("42").toXML());
    }

    @Test
    public void idXmlContainsOperator() {
        String expected = "<search><id><contains>42</contains></id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().id().contains("42").toXML());
    }

    @Test
    public void merchantAccountIdXmlIsOperator() {
        String expected = "<search><merchant_account_id type=\"array\"><item>42</item></merchant_account_id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().merchantAccountId().is("42").toXML());
    }

    @Test
    public void merchantAccountIdXmlInVarargsOperator() {
        String expected = "<search><merchant_account_id type=\"array\"><item>42</item><item>43</item></merchant_account_id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().merchantAccountId().in("42", "43").toXML());
    }

    @Test
    public void merchantAccountIdXmlInListOperator() {
        String[] items = new String[] { "42", "43" };
        String expected = "<search><merchant_account_id type=\"array\"><item>42</item><item>43</item></merchant_account_id></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().merchantAccountId().in(Arrays.asList(items)).toXML());
    }

    @Test
    public void priceXmlBetweenOperator() {
        String expected = "<search><price><min>5</min><max>15</max></price></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().price().between(new BigDecimal(5), new BigDecimal(15))
                .toXML());
    }

    @Test
    public void priceXmlGreaterThanOrEqualOperator() {
        String expected = "<search><price><min>5</min></price></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().price().greaterThanOrEqual(new BigDecimal(5))
                .toXML());
    }

    @Test
    public void priceXmlLessThanOrEqualOperator() {
        String expected = "<search><price><max>5</max></price></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().price().lessThanOrEqual(new BigDecimal(5)).toXML());
    }

    @Test
    public void toXMLEscapesXmlOnTextNodes() {
        String expected = "<search><days_past_due><is>&lt;test&gt;</is></days_past_due></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().is("<test>").toXML());
    }

    @Test
    public void toXMLEscapesXmlOnMultipleValueNodes() {
        String expected = "<search><ids type=\"array\"><item>&lt;a</item><item>b&amp;</item></ids></search>";
        Assert.assertEquals(expected, new SubscriptionSearchRequest().ids().in("<a", "b&").toXML());
    }

    @Test
    public void statusReturnsCorrectStringRepresentation() {
        String expected = "<search><status type=\"array\"><item>Active</item><item>Canceled</item><item>Past Due</item></status></search>";
        List<Status> statuses = new ArrayList<Status>();
        statuses.add(Status.ACTIVE);
        statuses.add(Status.CANCELED);
        statuses.add(Status.PAST_DUE);
        Assert.assertEquals(expected, new SubscriptionSearchRequest().status().in(statuses).toXML());
    }
}
