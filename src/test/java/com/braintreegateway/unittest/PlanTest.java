package com.braintreegateway.unittest;

import java.math.BigDecimal;

import com.braintreegateway.Plan;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlanTest {

    @Test
    public void parsesAllScalarFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<plan>" +
                "<id>a-plan-id</id>" +
                "<merchant-id>a-merchant-id</merchant-id>" +
                "<billing-day-of-month type=\"integer\">15</billing-day-of-month>" +
                "<billing-frequency type=\"integer\">1</billing-frequency>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<currency-iso-code>USD</currency-iso-code>" +
                "<description>a description</description>" +
                "<name>a plan name</name>" +
                "<number-of-billing-cycles type=\"integer\">12</number-of-billing-cycles>" +
                "<price>9.99</price>" +
                "<trial-period type=\"boolean\">true</trial-period>" +
                "<trial-duration type=\"integer\">7</trial-duration>" +
                "<trial-duration-unit>day</trial-duration-unit>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "</plan>");

        Plan plan = new Plan(node);

        assertAll("plan fields",
                () -> assertEquals("a-plan-id", plan.getId()),
                () -> assertEquals("a-merchant-id", plan.getMerchantId()),
                () -> assertEquals(Integer.valueOf(15), plan.getBillingDayOfMonth()),
                () -> assertEquals(Integer.valueOf(1), plan.getBillingFrequency()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), plan.getCreatedAt()),
                () -> assertEquals("USD", plan.getCurrencyIsoCode()),
                () -> assertEquals("a description", plan.getDescription()),
                () -> assertEquals("a plan name", plan.getName()),
                () -> assertEquals(Integer.valueOf(12), plan.getNumberOfBillingCycles()),
                () -> assertEquals(new BigDecimal("9.99"), plan.getPrice()),
                () -> assertTrue(plan.hasTrialPeriod()),
                () -> assertEquals(Integer.valueOf(7), plan.getTrialDuration()),
                () -> assertEquals(Plan.DurationUnit.DAY, plan.getTrialDurationUnit()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-12T21:28:37Z"), plan.getUpdatedAt()));
    }

    @Test
    public void parsesAddOnsAndDiscounts() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<plan>" +
                "<id>a-plan-id</id>" +
                "<add-ons type=\"array\">" +
                "<add-on><id>add-on-id</id><amount>5.00</amount></add-on>" +
                "</add-ons>" +
                "<discounts type=\"array\">" +
                "<discount><id>discount-id</id><amount>2.00</amount></discount>" +
                "</discounts>" +
                "</plan>");

        Plan plan = new Plan(node);

        assertAll("plan nested collections",
                () -> assertEquals(1, plan.getAddOns().size()),
                () -> assertEquals("add-on-id", plan.getAddOns().get(0).getId()),
                () -> assertEquals(1, plan.getDiscounts().size()),
                () -> assertEquals("discount-id", plan.getDiscounts().get(0).getId()));
    }
}
