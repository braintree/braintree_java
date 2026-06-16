package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Subscription;
import com.braintreegateway.SubscriptionRequest;

public class SubscriptionRequestTest {

    @Test
    public void buildsXmlWithAllScalarFields() {
        SubscriptionRequest request = new SubscriptionRequest()
                .id("a-subscription-id")
                .planId("a-plan-id")
                .paymentMethodToken("a-token")
                .paymentMethodNonce("a-nonce")
                .merchantAccountId("a-merchant-account")
                .billingDayOfMonth(15)
                .price(new BigDecimal("9.99"))
                .neverExpires(false)
                .numberOfBillingCycles(12)
                .trialPeriod(true)
                .trialDuration(7)
                .trialDurationUnit(Subscription.DurationUnit.DAY)
                .firstBillingDate(Calendar.getInstance());

        String xml = request.toXML();
        assertTrue(xml.contains("<subscription>"), xml);
        assertTrue(xml.contains("<id>a-subscription-id</id>"), xml);
        assertTrue(xml.contains("<planId>a-plan-id</planId>"), xml);
        assertTrue(xml.contains("<paymentMethodToken>a-token</paymentMethodToken>"), xml);
        assertTrue(xml.contains("<paymentMethodNonce>a-nonce</paymentMethodNonce>"), xml);
        assertTrue(xml.contains("<merchantAccountId>a-merchant-account</merchantAccountId>"), xml);
        assertTrue(xml.contains("<billingDayOfMonth>15</billingDayOfMonth>"), xml);
        assertTrue(xml.contains("<price>9.99</price>"), xml);
        assertTrue(xml.contains("<neverExpires>false</neverExpires>"), xml);
        assertTrue(xml.contains("<numberOfBillingCycles>12</numberOfBillingCycles>"), xml);
        assertTrue(xml.contains("<trialPeriod>true</trialPeriod>"), xml);
        assertTrue(xml.contains("<trialDuration>7</trialDuration>"), xml);
        assertTrue(xml.contains("<trialDurationUnit>day</trialDurationUnit>"), xml);
        assertTrue(xml.contains("<firstBillingDate type=\"datetime\">"), xml);
    }

    @Test
    public void buildsXmlWithNestedBuilders() {
        SubscriptionRequest request = new SubscriptionRequest().planId("a-plan-id");

        request.addOns().add().inheritedFromId("add-on-id").done().done();
        request.discounts().remove("discount-id").done();
        request.options().prorateCharges(true).done();
        request.descriptor().name("company*product").done();

        String xml = request.toXML();
        assertTrue(xml.contains("<addOns>"), xml);
        assertTrue(xml.contains("<discounts>"), xml);
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<descriptor>"), xml);
    }

    @Test
    public void omitsTrialDurationUnitWhenNull() {
        String xml = new SubscriptionRequest().planId("a-plan").toXML();
        assertFalse(xml.contains("trialDurationUnit"), xml);
    }
}
