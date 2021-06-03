package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.util.Http;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PlanIT extends IntegrationTestNew {
    private Http http;

    @BeforeEach
    public void createHttp() {
        http = new Http(this.gateway.getConfiguration());
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
                .trialPeriod(false);
        http.post(gateway.getConfiguration().getMerchantPath() + "/plans/create_plan_for_tests", request);

        FakeModificationRequest addOnRequest = new FakeModificationRequest()
                .amount(new BigDecimal("100.00"))
                .description("java test add-on description")
                .kind("add_on")
                .name("java test add-on name")
                .neverExpires(false)
                .numberOfBillingCycles(12)
                .planId(planId);
        http.post(gateway.getConfiguration().getMerchantPath() + "/modifications/create_modification_for_tests", addOnRequest);

        FakeModificationRequest discountRequest = new FakeModificationRequest()
                .amount(new BigDecimal("100.00"))
                .description("java test add-on description")
                .kind("discount")
                .name("java test discount name")
                .neverExpires(false)
                .numberOfBillingCycles(12)
                .planId(planId);
        http.post(gateway.getConfiguration().getMerchantPath() + "/modifications/create_modification_for_tests", discountRequest);

        List<Plan> plans = gateway.plan().all();
        Plan actualPlan = null;
        for (Plan plan : plans) {
            if (plan.getId().equals(planId)) {
                actualPlan = plan;
                break;
            }
        }

        assertEquals(Integer.valueOf(1), actualPlan.getBillingDayOfMonth());
        assertEquals(Integer.valueOf(1), actualPlan.getBillingFrequency());
        assertEquals("USD", actualPlan.getCurrencyIsoCode());
        assertEquals("java test description", actualPlan.getDescription());
        assertEquals("java test plan", actualPlan.getName());
        assertEquals(Integer.valueOf(12), actualPlan.getNumberOfBillingCycles());
        assertEquals(new BigDecimal("100.00"), actualPlan.getPrice());
        assertEquals(false, actualPlan.hasTrialPeriod());

        AddOn addOn = actualPlan.getAddOns().get(0);
        assertEquals(new BigDecimal("100.00"), addOn.getAmount());
        assertEquals("add_on", addOn.getKind());

        Discount discount = actualPlan.getDiscounts().get(0);
        assertEquals(new BigDecimal("100.00"), discount.getAmount());
        assertEquals("discount", discount.getKind());
    }

}
