package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.util.Http;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class PlanIT {
    private BraintreeGateway gateway;
    private Http http;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
        http = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.DEVELOPMENT.certificateFilenames, BraintreeGateway.VERSION);
    }

    @Test
    public void returnsAllPlans() {
        String planId = "a_plan_id" + String.valueOf(new Random().nextInt());
        PlanRequest request = new PlanRequest()
                .billingDayOfMonth(1)
                .billingFrequency(1)
                .currencyIsoCode("USD")
                .description("java test description")
                .id(planId)
                .name("java test plan")
                .numberOfBillingCycles(12)
                .price(new BigDecimal("100.00"))
                .trialDuration(1)
                .trialDurationUnit(Plan.DurationUnit.DAY)
                .trialPeriod(false);
        http.post("/plans/create_plan_for_tests", request);

        FakeModificationRequest addOnRequest = new FakeModificationRequest()
                .amount(new BigDecimal("100.00"))
                .description("java test add-on description")
                .kind("add_on")
                .name("java test add-on name")
                .neverExpires(false)
                .numberOfBillingCycles(12)
                .planId(planId);
        http.post("/modifications/create_modification_for_tests", addOnRequest);

        FakeModificationRequest discountRequest = new FakeModificationRequest()
                .amount(new BigDecimal("100.00"))
                .description("java test add-on description")
                .kind("discount")
                .name("java test discount name")
                .neverExpires(false)
                .numberOfBillingCycles(12)
                .planId(planId);
        http.post("/modifications/create_modification_for_tests", discountRequest);

        List<Plan> plans = gateway.plan().all();
        Plan actualPlan = null;
        for (Plan plan : plans) {
            if (plan.getId().equals(planId)) {
                actualPlan = plan;
                break;
            }
        }

        assertEquals(new Integer(1), actualPlan.getBillingDayOfMonth());
        assertEquals(new Integer(1), actualPlan.getBillingFrequency());
        assertEquals("USD", actualPlan.getCurrencyIsoCode());
        assertEquals("java test description", actualPlan.getDescription());
        assertEquals("java test plan", actualPlan.getName());
        assertEquals(new Integer(12), actualPlan.getNumberOfBillingCycles());
        assertEquals(new BigDecimal("100.00"), actualPlan.getPrice());
        assertEquals((Integer) 1, actualPlan.getTrialDuration());
        assertEquals(Plan.DurationUnit.DAY, actualPlan.getTrialDurationUnit());
        assertEquals(false, actualPlan.hasTrialPeriod());

        AddOn addOn = actualPlan.getAddOns().get(0);
        assertEquals(new BigDecimal("100.00"), addOn.getAmount());
        assertEquals("add_on", addOn.getKind());

        Discount discount = actualPlan.getDiscounts().get(0);
        assertEquals(new BigDecimal("100.00"), discount.getAmount());
        assertEquals("discount", discount.getKind());
    }

}
