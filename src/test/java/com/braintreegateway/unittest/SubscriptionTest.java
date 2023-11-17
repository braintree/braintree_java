package com.braintreegateway.unittest;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Subscription;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubscriptionTest {
    @Test
    public void testSubscriptionsAttributes() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String date = sdf.format(new Date());
        String xml = "<subscription>"
                    + "<add-ons type=\"array\">"
                    + "</add-ons>"
                    + "<balance>10.00</balance>"
                    + "<billing-day-of-month>12</billing-day-of-month>"
                    + "<billing-period-end-date type=\"datetime\">" + date + "</billing-period-end-date>"
                    + "<billing-period-start-date type=\"datetime\">" + date + "</billing-period-start-date>"
                    + "<created-at type=\"datetime\">" + date + "</created-at>"
                    + "<current-billing-cycle>5</current-billing-cycle>"
                    + "<days-past-due>0</days-past-due>"
                    + "<description>test description</description>"
                    + "<descriptor>descriptor</descriptor>"
                    + "<discounts type=\"array\">"
                    + "</discounts>"
                    + "<failure-count>0</failure-count>"
                    + "<first-billing-date type=\"datetime\">" + date + "</first-billing-date>"
                    + "<trial-period type=\"boolean\">true</trial-period>"
                    + "<id>12345</id>"
                    + "<merchant-account-id>54321</merchant-account-id>"
                    + "<never-expires type=\"boolean\">false</never-expires>"
                    + "<next-billing-date type=\"datetime\">" + date + "</next-billing-date>"
                    + "<next-billing-period-amount>30.00</next-billing-period-amount>"
                    + "<number-of-billing-cycles>30</number-of-billing-cycles>"
                    + "<paid-through-date type=\"datetime\">" + date + "</paid-through-date>"
                    + "<payment-method-token>123456789</payment-method-token>"
                    + "<plan-id>98765</plan-id>"
                    + "<price>15.00</price>"
                    + "<status>Active</status>"
                    + "<status-history type=\"array\">"
                    + "</status-history>"
                    + "<transactions type=\"array\">"
                    + "</transactions>"
                    + "<trial-duration>5</trial-duration>"
                    + "<trial-duration-unit>MONTH</trial-duration-unit>"
                    + "<updated-at type=\"datetime\">" + date + "</updated-at>"
                  + "</subscription>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        Subscription subscription = new Subscription(node);

        assertEquals(0, subscription.getAddOns().size());
        assertEquals(new BigDecimal("10.00"), subscription.getBalance());
        assertEquals(12, subscription.getBillingDayOfMonth());
        assertEquals(CalendarTestUtils.date(date), subscription.getBillingPeriodEndDate());
        assertEquals(CalendarTestUtils.date(date), subscription.getBillingPeriodStartDate());
        assertEquals(CalendarTestUtils.dateTime(date), subscription.getCreatedAt());
        assertEquals(5, subscription.getCurrentBillingCycle());
        assertEquals(0, subscription.getDaysPastDue());
        assertEquals("test description", subscription.getDescription());
        assertNotNull(subscription.getDescriptor());
        assertEquals(0, subscription.getDiscounts().size());
        assertEquals(0, subscription.getFailureCount());
        assertEquals(CalendarTestUtils.date(date), subscription.getFirstBillingDate());
        assertTrue(subscription.hasTrialPeriod());
        assertEquals("12345", subscription.getId());
        assertEquals("54321", subscription.getMerchantAccountId());
        assertFalse(subscription.neverExpires());
        assertEquals(CalendarTestUtils.date(date), subscription.getNextBillingDate());
        assertEquals(new BigDecimal("30.00"), subscription.getNextBillingPeriodAmount());
        assertEquals(30, subscription.getNumberOfBillingCycles());
        assertEquals(CalendarTestUtils.date(date), subscription.getPaidThroughDate());
        assertEquals("123456789", subscription.getPaymentMethodToken());
        assertEquals("98765", subscription.getPlanId());
        assertEquals(new BigDecimal("15.00"), subscription.getPrice());
        assertEquals(Subscription.Status.ACTIVE, subscription.getStatus());
        assertEquals(0, subscription.getStatusHistory().size());
        assertEquals(0, subscription.getTransactions().size());
        assertEquals(5, subscription.getTrialDuration());
        assertEquals(Subscription.DurationUnit.MONTH, subscription.getTrialDurationUnit());
        assertEquals(CalendarTestUtils.dateTime(date), subscription.getUpdatedAt());
    }
}