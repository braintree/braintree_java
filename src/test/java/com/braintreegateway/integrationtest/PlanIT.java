package com.braintreegateway.integrationtest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.braintreegateway.AddOn;
import com.braintreegateway.Discount;
import com.braintreegateway.Plan;
import com.braintreegateway.PlanRequest;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PlanIT extends IntegrationTest {
  private Http http;

  @BeforeEach
  public void createHttp() {
    http = new Http(this.gateway.getConfiguration());
  }

  @Test
  public void returnsAllPlans() {
    String planId = "a_plan_id" + String.valueOf(new Random().nextInt());
    PlanRequest request =
        new PlanRequest()
            .billingDayOfMonth(1)
            .billingFrequency(1)
            .currencyIsoCode("USD")
            .description("java test description")
            .id(planId)
            .name("java test plan")
            .numberOfBillingCycles(12)
            .price(new BigDecimal("100.00"))
            .trialPeriod(false);
    http.post(
        gateway.getConfiguration().getMerchantPath() + "/plans/create_plan_for_tests", request);

    FakeModificationRequest addOnRequest =
        new FakeModificationRequest()
            .amount(new BigDecimal("100.00"))
            .description("java test add-on description")
            .kind("add_on")
            .name("java test add-on name")
            .neverExpires(false)
            .numberOfBillingCycles(12)
            .planId(planId);
    http.post(
        gateway.getConfiguration().getMerchantPath()
            + "/modifications/create_modification_for_tests",
        addOnRequest);

    FakeModificationRequest discountRequest =
        new FakeModificationRequest()
            .amount(new BigDecimal("100.00"))
            .description("java test add-on description")
            .kind("discount")
            .name("java test discount name")
            .neverExpires(false)
            .numberOfBillingCycles(12)
            .planId(planId);
    http.post(
        gateway.getConfiguration().getMerchantPath()
            + "/modifications/create_modification_for_tests",
        discountRequest);

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

  @Test
  public void create() {
    PlanRequest request =
        new PlanRequest()
            .billingDayOfMonth(12)
            .billingFrequency(1)
            .currencyIsoCode("USD")
            .description("java test description")
            .name("java new test plan")
            .numberOfBillingCycles(12)
            .price(new BigDecimal("9.99"))
            .trialPeriod(false);

    Result<Plan> createResult = gateway.plan().create(request);
    assertTrue(createResult.isSuccess());
    Plan plan = createResult.getTarget();

    assertEquals(12, plan.getBillingDayOfMonth());
    assertEquals(1, plan.getBillingFrequency());
    assertEquals(new BigDecimal("9.99"), plan.getPrice());
    assertEquals("java test description", plan.getDescription());
    assertEquals("java new test plan", plan.getName());
    assertEquals("USD", plan.getCurrencyIsoCode());
    assertEquals(12, plan.getNumberOfBillingCycles());
  }

  @Test
  public void find() {
    PlanRequest request =
        new PlanRequest()
            .billingDayOfMonth(12)
            .billingFrequency(1)
            .currencyIsoCode("USD")
            .description("java test description")
            .name("java new test plan")
            .numberOfBillingCycles(12)
            .price(new BigDecimal("9.99"))
            .trialPeriod(false);

    Result<Plan> createResult = gateway.plan().create(request);
    assertTrue(createResult.isSuccess());
    Plan plan = createResult.getTarget();

    Plan foundPlan = gateway.plan().find(plan.getId());
    assertEquals(foundPlan.getId(), plan.getId());
    assertEquals(foundPlan.getBillingDayOfMonth(), plan.getBillingDayOfMonth());
    assertEquals(foundPlan.getName(), plan.getName());
    assertEquals(foundPlan.getPrice(), plan.getPrice());
    assertEquals(foundPlan.getCurrencyIsoCode(), plan.getCurrencyIsoCode());
  }

  @Test
  public void findWithEmptyIds() {
    try {
      gateway.plan().find(" ");
      fail("should throw NotFoundException");
    } catch (NotFoundException e) {
    }
  }

  @Test
  public void update() {
    PlanRequest createRequest =
        new PlanRequest()
            .billingDayOfMonth(1)
            .billingFrequency(1)
            .currencyIsoCode("USD")
            .description("java test description")
            .name("java new test plan")
            .numberOfBillingCycles(12)
            .price(new BigDecimal("9.99"))
            .trialPeriod(false);

    Result<Plan> createResult = gateway.plan().create(createRequest);
    assertTrue(createResult.isSuccess());
    Plan plan = createResult.getTarget();

    PlanRequest updateRequest =
        new PlanRequest()
            .name("updated plan name")
            .price(new BigDecimal("12.99"))
            .description("updated description");

    Result<Plan> result = gateway.plan().update(plan.getId(), updateRequest);
    assertTrue(result.isSuccess());
    Plan updatedPlan = result.getTarget();

    assertEquals(updatedPlan.getName(), "updated plan name");
    assertEquals(updatedPlan.getPrice(), new BigDecimal("12.99"));
    assertEquals(updatedPlan.getDescription(), "updated description");
  }
}
