package com.braintreegateway.integrationtest;

import com.braintreegateway.Subscription.Status;
import com.braintreegateway.SubscriptionSearchRequest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SubscriptionSearchRequestIT {
    @Test
    public void daysPastDueXmlIsOperator() {
        String expected = "<search><days_past_due><is>42</is></days_past_due></search>";
        assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().is("42").toXML());
    }

    @Test
    public void daysPastDueXmlBetweenOperator() {
        String expected = "<search><days_past_due><min>5</min><max>7</max></days_past_due></search>";
        assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().between(5, 7).toXML());
    }

    @Test
    public void daysPastDueXmlGreaterThanOrEqualOperator() {
        String expected = "<search><days_past_due><min>42</min></days_past_due></search>";
        assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().greaterThanOrEqualTo(42).toXML());
    }

    @Test
    public void daysPastDueXmlLessThanOrEqualOperator() {
        String expected = "<search><days_past_due><max>42</max></days_past_due></search>";
        assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().lessThanOrEqualTo(42).toXML());
    }

    @Test
    public void billingCyclesRemainingIsOperator() {
        String expected = "<search><billing_cycles_remaining><is>42</is></billing_cycles_remaining></search>";
        assertEquals(expected, new SubscriptionSearchRequest().billingCyclesRemaining().is(42).toXML());
    }

    @Test
    public void billingCyclesRemainingBetweenOperator() {
        String expected = "<search><billing_cycles_remaining><min>1</min><max>2</max></billing_cycles_remaining></search>";
        assertEquals(expected, new SubscriptionSearchRequest().billingCyclesRemaining().between(1, 2).toXML());
    }

    @Test
    public void billingCyclesRemainingLessThanOrEqualOperator() {
        String expected = "<search><billing_cycles_remaining><max>42</max></billing_cycles_remaining></search>";
        assertEquals(expected, new SubscriptionSearchRequest().billingCyclesRemaining().lessThanOrEqualTo(42).toXML());
    }

    @Test
    public void billingCyclesRemainingGreaterThanOrEqualOperator() {
        String expected = "<search><billing_cycles_remaining><min>42</min></billing_cycles_remaining></search>";
        assertEquals(expected, new SubscriptionSearchRequest().billingCyclesRemaining().greaterThanOrEqualTo(42)
                .toXML());
    }

    @Test
    public void idXmlIsOperator() {
        String expected = "<search><id><is>42</is></id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().id().is("42").toXML());
    }

    @Test
    public void idXmlIsNotOperator() {
        String expected = "<search><id><is_not>42</is_not></id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().id().isNot("42").toXML());
    }

    @Test
    public void idXmlStartsWithOperator() {
        String expected = "<search><id><starts_with>42</starts_with></id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().id().startsWith("42").toXML());
    }

    @Test
    public void idXmlEndsWithOperator() {
        String expected = "<search><id><ends_with>42</ends_with></id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().id().endsWith("42").toXML());
    }

    @Test
    public void idXmlContainsOperator() {
        String expected = "<search><id><contains>42</contains></id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().id().contains("42").toXML());
    }

    @Test
    public void merchantAccountIdXmlIsOperator() {
        String expected = "<search><merchant_account_id type=\"array\"><item>42</item></merchant_account_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().merchantAccountId().is("42").toXML());
    }

    @Test
    public void merchantAccountIdXmlInVarargsOperator() {
        String expected = "<search><merchant_account_id type=\"array\"><item>42</item><item>43</item></merchant_account_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().merchantAccountId().in("42", "43").toXML());
    }

    @Test
    public void merchantAccountIdXmlInListOperator() {
        String[] items = new String[]{"42", "43"};
        String expected = "<search><merchant_account_id type=\"array\"><item>42</item><item>43</item></merchant_account_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().merchantAccountId().in(Arrays.asList(items)).toXML());
    }

    @Test
    public void planIdXmlIsOperator() {
        String expected = "<search><plan_id><is>42</is></plan_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().planId().is("42").toXML());
    }

    @Test
    public void planIdXmlIsNotOperator() {
        String expected = "<search><plan_id><is_not>42</is_not></plan_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().planId().isNot("42").toXML());
    }

    @Test
    public void planIdXmlStartsWithOperator() {
        String expected = "<search><plan_id><starts_with>42</starts_with></plan_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().planId().startsWith("42").toXML());
    }

    @Test
    public void planIdXmlEndsWithOperator() {
        String expected = "<search><plan_id><ends_with>42</ends_with></plan_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().planId().endsWith("42").toXML());
    }

    @Test
    public void planIdXmlContainsOperator() {
        String expected = "<search><plan_id><contains>42</contains></plan_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().planId().contains("42").toXML());
    }

    @Test
    public void plantIdXmlInVarargsOperator() {
        String expected = "<search><plan_id type=\"array\"><item>42</item><item>43</item></plan_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().planId().in("42", "43").toXML());
    }

    @Test
    public void planIdXmlInListOperator() {
        String[] items = new String[]{"42", "43"};
        String expected = "<search><plan_id type=\"array\"><item>42</item><item>43</item></plan_id></search>";
        assertEquals(expected, new SubscriptionSearchRequest().planId().in(Arrays.asList(items)).toXML());
    }

    @Test
    public void priceXmlBetweenOperator() {
        String expected = "<search><price><min>5</min><max>15</max></price></search>";
        assertEquals(expected, new SubscriptionSearchRequest().price().between(new BigDecimal(5), new BigDecimal(15))
                .toXML());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void priceXmlDeprecatedGreaterThanOrEqualOperator() {
        String expected = "<search><price><min>5</min></price></search>";
        assertEquals(expected, new SubscriptionSearchRequest().price().greaterThanOrEqual(new BigDecimal(5))
                .toXML());
    }

    @Test
    public void priceXmlGreaterThanOrEqualToOperator() {
        String expected = "<search><price><min>5</min></price></search>";
        assertEquals(expected, new SubscriptionSearchRequest().price().greaterThanOrEqualTo(new BigDecimal(5))
                .toXML());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void priceXmlDeprecatedLessThanOrEqualOperator() {
        String expected = "<search><price><max>5</max></price></search>";
        assertEquals(expected, new SubscriptionSearchRequest().price().lessThanOrEqual(new BigDecimal(5)).toXML());
    }

    @Test
    public void priceXmlLessThanOrEqualToOperator() {
        String expected = "<search><price><max>5</max></price></search>";
        assertEquals(expected, new SubscriptionSearchRequest().price().lessThanOrEqualTo(new BigDecimal(5)).toXML());
    }

    @Test
    public void priceXmlIsOperator() {
        String expected = "<search><price><is>5</is></price></search>";
        assertEquals(expected, new SubscriptionSearchRequest().price().is(new BigDecimal(5)).toXML());
    }

    @Test
    public void toXMLEscapesXmlOnTextNodes() {
        String expected = "<search><days_past_due><is>&lt;test&gt;</is></days_past_due></search>";
        assertEquals(expected, new SubscriptionSearchRequest().daysPastDue().is("<test>").toXML());
    }

    @Test
    public void toXMLEscapesXmlOnMultipleValueNodes() {
        String expected = "<search><ids type=\"array\"><item>&lt;a</item><item>b&amp;</item></ids></search>";
        assertEquals(expected, new SubscriptionSearchRequest().ids().in("<a", "b&").toXML());
    }

    @Test
    public void statusReturnsCorrectStringRepresentation() {
        String expected = "<search><status type=\"array\"><item>Active</item><item>Canceled</item><item>Past Due</item></status></search>";
        List<Status> statuses = new ArrayList<Status>();
        statuses.add(Status.ACTIVE);
        statuses.add(Status.CANCELED);
        statuses.add(Status.PAST_DUE);
        assertEquals(expected, new SubscriptionSearchRequest().status().in(statuses).toXML());
    }
}
