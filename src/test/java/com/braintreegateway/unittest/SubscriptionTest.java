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

    @Test
    public void parsesAddOnsDiscountsAndStatusHistory() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<subscription>" +
                "<id>sub-id</id><status>Active</status>" +
                "<add-ons type=\"array\"><add-on><id>add-on-id</id><amount>5.00</amount></add-on></add-ons>" +
                "<discounts type=\"array\"><discount><id>discount-id</id><amount>2.00</amount></discount></discounts>" +
                "<status-history type=\"array\"><status-event>" +
                "<status>Active</status><subscription-source>api</subscription-source>" +
                "</status-event></status-history>" +
                "<transactions type=\"array\"/>" +
                "</subscription>");
        Subscription sub = new Subscription(node);
        assertAll("nested collections",
                () -> assertEquals(1, sub.getAddOns().size()),
                () -> assertEquals(1, sub.getDiscounts().size()),
                () -> assertEquals(1, sub.getStatusHistory().size()),
                () -> assertEquals(Subscription.Status.ACTIVE, sub.getStatusHistory().get(0).getStatus()));
    }

    @Test
    public void durationUnitEnumToString() {
        assertAll("duration unit toString",
                () -> assertEquals("DAY", Subscription.DurationUnit.DAY.toString()),
                () -> assertEquals("MONTH", Subscription.DurationUnit.MONTH.toString()));
    }

    @Test
    public void sourceEnumToString() {
        assertEquals("api", Subscription.Source.API.toString());
    }

    @Test
    public void parsesTransactionsAndRemainingGetters() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<subscription><id>sub-id</id><status>Active</status>" +
                "<next-billing-date type=\"date\">2024-02-01</next-billing-date>" +
                "<next-billing-period-amount>9.99</next-billing-period-amount>" +
                "<trial-period type=\"boolean\">true</trial-period>" +
                "<never-expires type=\"boolean\">false</never-expires>" +
                "<descriptor><name>company*product</name><phone>3125551234</phone></descriptor>" +
                "<transactions type=\"array\">" +
                "<transaction><id>txn-id</id><type>sale</type><amount>9.99</amount></transaction>" +
                "</transactions>" +
                "<add-ons type=\"array\"/><discounts type=\"array\"/>" +
                "<status-history type=\"array\"/></subscription>");
        Subscription sub = new Subscription(node);
        assertAll("remaining getters",
                () -> assertNotNull(sub.getNextBillingDate()),
                () -> assertNotNull(sub.getNextBillingPeriodAmount()),
                () -> assertNotNull(sub.getDescriptor()),
                () -> assertEquals(1, sub.getTransactions().size()),
                () -> assertTrue(sub.hasTrialPeriod()),
                () -> assertFalse(sub.neverExpires()));
    }

    @Test
    public void equalsReturnsTrueForSameId() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<subscription><id>sub-id</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>");
        Subscription a = new Subscription(node);
        Subscription b = new Subscription(node);
        assertTrue(a.equals(b));
    }

    @Test
    public void equalsReturnsFalseForDifferentIdAndNonSubscription() {
        Subscription a = new Subscription(SimpleNodeWrapper.parse(
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>"));
        Subscription b = new Subscription(SimpleNodeWrapper.parse(
                "<subscription><id>sub-2</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>"));
        assertFalse(a.equals(b));
        assertFalse(a.equals("not-a-subscription"));
    }
}
