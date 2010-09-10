package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.Subscription.Status;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class SubscriptionTest {

    private BraintreeGateway gateway;
    private Customer customer;
    private CreditCard creditCard;
    
    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
        CustomerRequest request = new CustomerRequest();
        request.
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                expirationDate("05/12").
                done();

        Result<Customer> result = gateway.customer().create(request);
        Assert.assertTrue(result.isSuccess());
        this.customer = result.getTarget();
        this.creditCard = customer.getCreditCards().get(0);
    }

    @Test
    public void createSimpleSubscriptionWithoutTrial() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Calendar expectedBillingPeriodEndDate = Calendar.getInstance();
        expectedBillingPeriodEndDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedBillingPeriodEndDate.roll(Calendar.MONTH, plan.getBillingFrequency());
        expectedBillingPeriodEndDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar expectedNextBillingDate = Calendar.getInstance();
        expectedNextBillingDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedNextBillingDate.roll(Calendar.MONTH, plan.getBillingFrequency());
        Calendar expectedBillingPeriodStartDate = Calendar.getInstance();
        expectedBillingPeriodStartDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        Calendar expectedFirstDate = Calendar.getInstance();
        expectedFirstDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        Calendar expectedPaidThroughDate = expectedBillingPeriodEndDate;
        
        Assert.assertEquals(creditCard.getToken(), subscription.getPaymentMethodToken());
        Assert.assertEquals(plan.getId(), subscription.getPlanId());
        Assert.assertEquals(plan.getPrice(), subscription.getPrice());
        Assert.assertEquals(new BigDecimal("0.00"), subscription.getBalance());
        Assert.assertEquals(new BigDecimal("12.34"), subscription.getNextBillAmount());
        Assert.assertTrue(subscription.getId().matches("^\\w{6}$"));
        Assert.assertEquals(Subscription.Status.ACTIVE, subscription.getStatus());
        Assert.assertEquals(new Integer(0), subscription.getFailureCount());
        Assert.assertEquals(false, subscription.hasTrialPeriod());
        Assert.assertEquals(MerchantAccount.DEFAULT_MERCHANT_ACCOUNT_ID, subscription.getMerchantAccountId());
        
        TestHelper.assertDatesEqual(expectedBillingPeriodEndDate, subscription.getBillingPeriodEndDate());
        TestHelper.assertDatesEqual(expectedBillingPeriodStartDate, subscription.getBillingPeriodStartDate());
        TestHelper.assertDatesEqual(expectedPaidThroughDate, subscription.getPaidThroughDate());
        TestHelper.assertDatesEqual(expectedNextBillingDate, subscription.getNextBillingDate());
        TestHelper.assertDatesEqual(expectedFirstDate, subscription.getFirstBillingDate());
    }
    
    @Test
    public void createSimpleSubscriptionWithTrial() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Calendar expectedFirstAndNextBillingDate = Calendar.getInstance();
        expectedFirstAndNextBillingDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedFirstAndNextBillingDate.add(Calendar.DAY_OF_MONTH, plan.getTrialDuration());

        Assert.assertEquals(plan.getId(), subscription.getPlanId());
        Assert.assertEquals(plan.getPrice(), subscription.getPrice());
        Assert.assertEquals(creditCard.getToken(), subscription.getPaymentMethodToken());
        Assert.assertTrue(subscription.getId().matches("^\\w{6}$"));
        Assert.assertEquals(Subscription.Status.ACTIVE, subscription.getStatus());
        
        Assert.assertEquals(null, subscription.getBillingPeriodStartDate());        
        Assert.assertEquals(null, subscription.getBillingPeriodEndDate());

        Assert.assertEquals(new Integer(0), subscription.getFailureCount());
        Assert.assertEquals(true, subscription.hasTrialPeriod());
        Assert.assertEquals(plan.getTrialDuration(), subscription.getTrialDuration());
        Assert.assertEquals(plan.getTrialDurationUnit(), subscription.getTrialDurationUnit());
        
        TestHelper.assertDatesEqual(expectedFirstAndNextBillingDate, subscription.getNextBillingDate());
        TestHelper.assertDatesEqual(expectedFirstAndNextBillingDate, subscription.getFirstBillingDate());
    }
    
    @Test
    public void overridePlanAddTrial() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            trialPeriod(true).
            trialDuration(2).
            trialDurationUnit(Subscription.DurationUnit.MONTH);
            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        Assert.assertEquals(true, subscription.hasTrialPeriod());
        Assert.assertEquals(new Integer(2), subscription.getTrialDuration());
        Assert.assertEquals(Subscription.DurationUnit.MONTH, subscription.getTrialDurationUnit());
    }
    
    @Test
    public void overridePlanRemoveTrial() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            trialPeriod(false);
            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        Assert.assertEquals(false, subscription.hasTrialPeriod());
    }
    
    @Test
    public void overridePlanPrice() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            price(new BigDecimal("482.48"));
            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Assert.assertEquals(new BigDecimal("482.48"), subscription.getPrice());
    }
    
    @Test
    public void overridePlanNumberOfBillingCycles() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());
            
        Subscription subscription = gateway.subscription().create(request).getTarget();
        Assert.assertEquals(plan.getNumberOfBillingCycles(), subscription.getNumberOfBillingCycles());
        
        SubscriptionRequest overrideRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            numberOfBillingCycles(10);
        
        Subscription overriddenSubsription = gateway.subscription().create(overrideRequest).getTarget();
        Assert.assertEquals(new Integer(10), overriddenSubsription.getNumberOfBillingCycles());
        Assert.assertFalse(overriddenSubsription.neverExpires());
    }
    
    @Test
    public void setNeverExpires() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            neverExpires(true);
            
        Subscription subscription = gateway.subscription().create(request).getTarget();
        Assert.assertNull(subscription.getNumberOfBillingCycles());
        Assert.assertTrue(subscription.neverExpires());
    }
    
    @Test
    public void setNumberOfBillingCyclesAndUpdateToNeverExpire() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            numberOfBillingCycles(10);
            
        Subscription subscription = gateway.subscription().create(request).getTarget();
        
        SubscriptionRequest updateRequest = new SubscriptionRequest().
            neverExpires(true);
        
        Subscription updatedSubscription = gateway.subscription().update(subscription.getId(), updateRequest).getTarget();
        
        Assert.assertNull(updatedSubscription.getNumberOfBillingCycles());
    }
    
    @Test
    public void setNumberOfBillingCyclesAndUpdate() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            numberOfBillingCycles(10);
            
        Subscription subscription = gateway.subscription().create(request).getTarget();
        
        SubscriptionRequest updateRequest = new SubscriptionRequest().
            numberOfBillingCycles(14);
        
        Subscription updatedSubscription = gateway.subscription().update(subscription.getId(), updateRequest).getTarget();
        
        Assert.assertEquals(new Integer(14), updatedSubscription.getNumberOfBillingCycles());
    }
    
    @Test
    public void inheritBillingDayOfMonth() {
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.BILLING_DAY_OF_MONTH_PLAN.getId());
            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Assert.assertEquals(new Integer(5), subscription.getBillingDayOfMonth());
    }

    @Test
    public void overrideBillingDayOfMonth() {
        SubscriptionRequest request = new SubscriptionRequest().
            billingDayOfMonth(19).
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.BILLING_DAY_OF_MONTH_PLAN.getId());
            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Assert.assertEquals(new Integer(19), subscription.getBillingDayOfMonth());
    }
    
    @Test
    public void overrideBillingDayOfMonthWithStartImmediately() {
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.BILLING_DAY_OF_MONTH_PLAN.getId()).
            options().
                startImmediately(true).
                done();
            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Assert.assertEquals(1, subscription.getTransactions().size());
    }
    
    @Test
    public void setFirstBillingDate() {
        Calendar firstBillingDate = Calendar.getInstance();
        firstBillingDate.add(Calendar.DAY_OF_MONTH, 3);

        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.BILLING_DAY_OF_MONTH_PLAN.getId()).
            firstBillingDate(firstBillingDate);
        
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
    
        TestHelper.assertDatesEqual(firstBillingDate, subscription.getFirstBillingDate());
        Assert.assertEquals(Subscription.Status.PENDING, subscription.getStatus());
    }
    
    @Test
    public void setFirstBillingDateInThePast() {
        Calendar firstBillingDate = Calendar.getInstance();
        firstBillingDate.add(Calendar.DAY_OF_MONTH, -3);

        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.BILLING_DAY_OF_MONTH_PLAN.getId()).
            firstBillingDate(firstBillingDate);
        
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertFalse(createResult.isSuccess());
    
        Assert.assertEquals(ValidationErrorCode.SUBSCRIPTION_FIRST_BILLING_DATE_CANNOT_BE_IN_THE_PAST,
            createResult.getErrors().forObject("subscription").onField("firstBillingDate").get(0).getCode());
    }

    @Test
    public void setId() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        String newId = "new-id-" + new Random().nextInt(); 
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            price(new BigDecimal("482.48")).
            id(newId);

            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Assert.assertEquals(newId, subscription.getId());
    }
    
    @Test
    public void setMerchantAccountId() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            price(new BigDecimal("482.48")).
            merchantAccountId(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID);

        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Assert.assertEquals(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID, subscription.getMerchantAccountId());
    }
    
    @Test
    public void hasTransactionOnCreateWithNoTrial() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            price(new BigDecimal("482.48"));

            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        Transaction transaction = subscription.getTransactions().get(0);
        
        Assert.assertEquals(1, subscription.getTransactions().size());
        Assert.assertEquals(new BigDecimal("482.48"), transaction.getAmount());
        Assert.assertEquals(Transaction.Type.SALE, transaction.getType());
        Assert.assertEquals(subscription.getId(), transaction.getSubscriptionId());
    }   
    
    @Test
    public void hasTransactionOnCreateWhenTransactionFails() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            price(SandboxValues.TransactionAmount.DECLINE.amount);
        
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(Transaction.Status.PROCESSOR_DECLINED, result.getTransaction().getStatus());
    }   
    
    @Test
    public void hasNoTransactionOnCreateWithATrial() {
        Plan plan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());

            
        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        Assert.assertEquals(0, subscription.getTransactions().size());
    }
    
    @Test
    public void createInheritsNoAddOnsAndDiscountsWhenOptionIsPassed() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            options().
                doNotInheritAddOnsOrDiscounts(true).
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        Assert.assertEquals(0, subscription.getAddOns().size());
        Assert.assertEquals(0, subscription.getDiscounts().size());
    }

    @Test
    public void createInheritsAddOnsAndDiscountsFromPlan() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        List<AddOn> addOns = subscription.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        Assert.assertEquals(2, addOns.size());

        Assert.assertEquals("increase_10", addOns.get(0).getId());
        Assert.assertEquals(new BigDecimal("10.00"), addOns.get(0).getAmount());
        Assert.assertEquals(new Integer(1), addOns.get(0).getQuantity());
        Assert.assertTrue(addOns.get(0).neverExpires());
        Assert.assertNull(addOns.get(0).getNumberOfBillingCycles());
        
        Assert.assertEquals("increase_20", addOns.get(1).getId());
        Assert.assertEquals(new BigDecimal("20.00"), addOns.get(1).getAmount());
        Assert.assertEquals(new Integer(1), addOns.get(1).getQuantity());
        Assert.assertTrue(addOns.get(1).neverExpires());
        Assert.assertNull(addOns.get(1).getNumberOfBillingCycles());
        
        List<Discount> discounts = subscription.getDiscounts();
        Collections.sort(discounts, new TestHelper.CompareModificationsById());
        
        Assert.assertEquals(2, discounts.size());
        
        Assert.assertEquals("discount_11", discounts.get(0).getId());
        Assert.assertEquals(new BigDecimal("11.00"), discounts.get(0).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());
        Assert.assertTrue(discounts.get(0).neverExpires());
        Assert.assertNull(discounts.get(0).getNumberOfBillingCycles());

        Assert.assertEquals("discount_7", discounts.get(1).getId());
        Assert.assertEquals(new BigDecimal("7.00"), discounts.get(1).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(1).getQuantity());
        Assert.assertTrue(discounts.get(1).neverExpires());
        Assert.assertNull(discounts.get(1).getNumberOfBillingCycles());
    }

    @Test
    public void createOverridesInheritedAddOnsAndDiscounts() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            addOns().
                update("increase_10").
                    amount(new BigDecimal("30.00")).
                    numberOfBillingCycles(3).
                    quantity(9).
                    done().
                update("increase_20").
                    amount(new BigDecimal("40.00")).
                    done().
                done().
            discounts().
                update("discount_7").
                    amount(new BigDecimal("15.00")).
                    neverExpires(true).
                    done().
                update("discount_11").
                    amount(new BigDecimal("23.00")).
                    done().
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        List<AddOn> addOns = subscription.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        Assert.assertEquals(2, addOns.size());

        Assert.assertEquals("increase_10", addOns.get(0).getId());
        Assert.assertEquals(new BigDecimal("30.00"), addOns.get(0).getAmount());
        Assert.assertEquals(new Integer(3), addOns.get(0).getNumberOfBillingCycles());
        Assert.assertFalse(addOns.get(0).neverExpires());
        Assert.assertEquals(new Integer(9), addOns.get(0).getQuantity());
        
        Assert.assertEquals("increase_20", addOns.get(1).getId());
        Assert.assertEquals(new BigDecimal("40.00"), addOns.get(1).getAmount());
        Assert.assertEquals(new Integer(1), addOns.get(1).getQuantity());
        
        List<Discount> discounts = subscription.getDiscounts();
        Collections.sort(discounts, new TestHelper.CompareModificationsById());
        
        Assert.assertEquals(2, discounts.size());

        Assert.assertEquals("discount_11", discounts.get(0).getId());
        Assert.assertEquals(new BigDecimal("23.00"), discounts.get(0).getAmount());
        Assert.assertNull(discounts.get(0).getNumberOfBillingCycles());
        Assert.assertTrue(discounts.get(0).neverExpires());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());

        Assert.assertEquals("discount_7", discounts.get(1).getId());
        Assert.assertEquals(new BigDecimal("15.00"), discounts.get(1).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(1).getQuantity());
    }
    
    @Test
    public void createRemovesInheritedAddOnsAndDiscounts() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            addOns().
                remove("increase_10", "increase_20").
                done().
            discounts().
                remove("discount_7", "discount_11").
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        Assert.assertEquals(0, subscription.getAddOns().size());
        Assert.assertEquals(0, subscription.getDiscounts().size());
    }

    @Test
    public void createRemovesInheritedAddOnsAndDiscountsWithListsOrChaining() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            addOns().
                remove(Arrays.asList(new String[] { "increase_10", "increase_20" })).
                done().
            discounts().
                remove("discount_7").
                remove("discount_11").
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        Assert.assertEquals(0, subscription.getAddOns().size());
        Assert.assertEquals(0, subscription.getDiscounts().size());
    }
    
    @Test
    public void createAddsNewAddOnsAndDiscounts() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            addOns().
                remove("increase_10", "increase_20").
                add().
                    inheritedFromId("increase_30").
                    amount(new BigDecimal("40.00")).
                    neverExpires(false).
                    numberOfBillingCycles(6).
                    quantity(3).
                    done().
                done().
            discounts().
                remove("discount_7", "discount_11").
                add().
                    inheritedFromId("discount_15").
                    amount(new BigDecimal("17.00")).
                    neverExpires(true).
                    numberOfBillingCycles(null).
                    quantity(2).
                    done().
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        Assert.assertEquals(1, subscription.getAddOns().size());

        Assert.assertEquals(new BigDecimal("40.00"), subscription.getAddOns().get(0).getAmount());
        Assert.assertEquals(new Integer(6), subscription.getAddOns().get(0).getNumberOfBillingCycles());
        Assert.assertFalse(subscription.getAddOns().get(0).neverExpires());
        Assert.assertEquals(new Integer(3), subscription.getAddOns().get(0).getQuantity());
        
        Assert.assertEquals(1, subscription.getDiscounts().size());

        Assert.assertEquals(new BigDecimal("17.00"), subscription.getDiscounts().get(0).getAmount());
        Assert.assertNull(subscription.getDiscounts().get(0).getNumberOfBillingCycles());
        Assert.assertTrue(subscription.getDiscounts().get(0).neverExpires());
        Assert.assertEquals(new Integer(2), subscription.getDiscounts().get(0).getQuantity());
    }
    
    @Test
    public void createWithBadQuantityCorrectlyParsesValidationErrors() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            addOns().
                update("addon_7").
                    amount(new BigDecimal("-15")).
                    done().
                update("discount_7").
                    quantity(-10).
                    done().
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.SUBSCRIPTION_MODIFICATION_AMOUNT_IS_INVALID,
            result.getErrors().forObject("subscription").forObject("addOns").forObject("update").forIndex(0).onField("amount").get(0).getCode());
        Assert.assertEquals(ValidationErrorCode.SUBSCRIPTION_MODIFICATION_QUANTITY_IS_INVALID,
            result.getErrors().forObject("subscription").forObject("addOns").forObject("update").forIndex(1).onField("quantity").get(0).getCode());
    }

    @Test
    public void find() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        Subscription foundSubscription = gateway.subscription().find(subscription.getId());
        Assert.assertEquals(subscription.getId(), foundSubscription.getId());
        Assert.assertEquals(subscription.getPaymentMethodToken(), creditCard.getToken());
        Assert.assertEquals(subscription.getPlanId(), plan.getId());
    }
    
    @Test
    public void updateId() {
        String oldId = "old-id-" + new Random().nextInt(); 
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            id(oldId);

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        
        String newId ="new-id-" + new Random().nextInt(); 
        SubscriptionRequest updateRequest = new SubscriptionRequest().id(newId);
        Result<Subscription> result = gateway.subscription().update(oldId, updateRequest);
        
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();
        
        Assert.assertEquals(newId, subscription.getId());
        Assert.assertNotNull(gateway.subscription().find(newId));
    }
    
    @Test
    public void updateMerchantAccountId() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());

        SubscriptionRequest updateRequest = new SubscriptionRequest()
            .merchantAccountId(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID);
        Result<Subscription> result = gateway.subscription().update(createResult.getTarget().getId(), updateRequest);

        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        Assert.assertEquals(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID, subscription.getMerchantAccountId());
    }

    @Test
    public void updatePlan() {
        Plan originalPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(originalPlan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        Plan newPlan = Plan.PLAN_WITH_TRIAL; 
        SubscriptionRequest updateRequest = new SubscriptionRequest().planId(newPlan.getId());
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);
        
        Assert.assertTrue(result.isSuccess());
        subscription = result.getTarget();
        
        Assert.assertEquals(newPlan.getId(), subscription.getPlanId()); 
    }

    @Test
    public void updatePaymentMethod() {
        Plan originalPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(originalPlan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");

        CreditCard newCreditCard = gateway.creditCard().create(request).getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().paymentMethodToken(newCreditCard.getToken());
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);
        
        Assert.assertTrue(result.isSuccess());
        subscription = result.getTarget();
        
        Assert.assertEquals(newCreditCard.getToken(), subscription.getPaymentMethodToken()); 
    }
    
    @Test
    public void createAProrationTransactionOnPriceIncreaseWhenFlagIsNotPassed() {
        Plan originalPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(originalPlan.getId()).
            price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        SubscriptionRequest updateRequest = new SubscriptionRequest().price(new BigDecimal("4.56"));
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);
        
        Assert.assertTrue(result.isSuccess());
        subscription = result.getTarget();
        
        Assert.assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        Assert.assertEquals(2, subscription.getTransactions().size());
    }
    
    @Test
    public void createAProrationTransactionOnPriceIncreaseWhenProrationFlagIsTrue() {
        Plan originalPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(originalPlan.getId()).
            price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        SubscriptionRequest updateRequest = new SubscriptionRequest().
        price(new BigDecimal("4.56")).
        options().
            prorateCharges(true).
            done();
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);
        
        Assert.assertTrue(result.isSuccess());
        subscription = result.getTarget();
        
        Assert.assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        Assert.assertEquals(2, subscription.getTransactions().size());
    }
    
    @Test
    public void doNotCreateAProrationTransactionOnPriceIncreaseWhenProrationFlagIsFalse() {
        Plan originalPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(originalPlan.getId()).
            price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        SubscriptionRequest updateRequest = new SubscriptionRequest().
            price(new BigDecimal("4.56")).
            options().
                prorateCharges(false).
                done();
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);
        
        Assert.assertTrue(result.isSuccess());
        subscription = result.getTarget();
        
        Assert.assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        Assert.assertEquals(1, subscription.getTransactions().size());
    }

    @Test
    public void dontIncreasePriceAndDontAddTransaction() {
        Plan originalPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(originalPlan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        Assert.assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        
        SubscriptionRequest updateRequest = new SubscriptionRequest().price(new BigDecimal("4.56"));
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);
        
        Assert.assertTrue(result.isSuccess());
        subscription = result.getTarget();
        
        Assert.assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        Assert.assertEquals(1, subscription.getTransactions().size());
    }

    @Test
    public void updateAddOnsAndDiscounts() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());
        Subscription subscription = gateway.subscription().create(createRequest).getTarget();
        
        SubscriptionRequest request = new SubscriptionRequest().
            addOns().
                update("increase_10").
                    amount(new BigDecimal("30.00")).
                    quantity(9).
                    done().
                remove("increase_20").
                add().
                    inheritedFromId("increase_30").
                    amount(new BigDecimal("31.00")).
                    quantity(7).
                    done().
                done().
            discounts().
                update("discount_7").
                    amount(new BigDecimal("15.00")).
                    done().
                remove("discount_11").
                add().
                    inheritedFromId("discount_15").
                    amount(new BigDecimal("23.00")).
                    done().
                done();
            
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), request);
        Assert.assertTrue(result.isSuccess());
        Subscription updatedSubscription = result.getTarget();

        List<AddOn> addOns = updatedSubscription.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        Assert.assertEquals(2, addOns.size());

        Assert.assertEquals(new BigDecimal("30.00"), addOns.get(0).getAmount());
        Assert.assertEquals(new Integer(9), addOns.get(0).getQuantity());
        
        Assert.assertEquals(new BigDecimal("31.00"), addOns.get(1).getAmount());
        Assert.assertEquals(new Integer(7), addOns.get(1).getQuantity());
        
        List<Discount> discounts = updatedSubscription.getDiscounts();
        Collections.sort(discounts, new TestHelper.CompareModificationsById());
        
        Assert.assertEquals(2, discounts.size());

        Assert.assertEquals("discount_15", discounts.get(0).getId());
        Assert.assertEquals(new BigDecimal("23.00"), discounts.get(0).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());

        Assert.assertEquals("discount_7", discounts.get(1).getId());
        Assert.assertEquals(new BigDecimal("15.00"), discounts.get(1).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(1).getQuantity());
    }
    
    @Test
    public void updateCanReplaceAllAddOnsAndDiscounts() {
        Plan plan = Plan.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());
        Subscription subscription = gateway.subscription().create(createRequest).getTarget();
        
        SubscriptionRequest request = new SubscriptionRequest().
            addOns().
                add().
                    inheritedFromId("increase_30").
                    done().
                done().
            discounts().
                add().
                    inheritedFromId("discount_15").
                    done().
                done().
            options().
                replaceAllAddOnsAndDiscounts(true).
                done();
            
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), request);
        Assert.assertTrue(result.isSuccess());
        Subscription updatedSubscription = result.getTarget();

        Assert.assertEquals(1, updatedSubscription.getAddOns().size());
        Assert.assertEquals(1, updatedSubscription.getDiscounts().size());
    }

    @Test
    public void createWithBadPlanId() {
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId("noSuchPlanId");

        Result<Subscription> result = gateway.subscription().create(createRequest);
        Assert.assertFalse(result.isSuccess());
        
        Assert.assertEquals(ValidationErrorCode.SUBSCRIPTION_PLAN_ID_IS_INVALID, result.getErrors().forObject("subscription").onField("planId").get(0).getCode());
    }
    
    @Test
    public void createWithBadPaymentMethod() {
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken("invalidToken").
            planId(Plan.PLAN_WITHOUT_TRIAL.getId());

        Result<Subscription> result = gateway.subscription().create(createRequest);
        Assert.assertFalse(result.isSuccess());
        
        Assert.assertEquals(ValidationErrorCode.SUBSCRIPTION_PAYMENT_METHOD_TOKEN_IS_INVALID, result.getErrors().forObject("subscription").onField("paymentMethodToken").get(0).getCode());
    }
    
    @Test
    public void validationErrorsOnCreate() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            id("invalid id");

        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertFalse(createResult.isSuccess());
        Assert.assertNull(createResult.getTarget());
        ValidationErrors errors = createResult.getErrors();
        Assert.assertEquals(ValidationErrorCode.SUBSCRIPTION_TOKEN_FORMAT_IS_INVALID, errors.forObject("subscription").onField("id").get(0).getCode());
    }
    
    @Test
    public void validationErrorsOnUpdate() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertTrue(createResult.isSuccess());
        Subscription createdSubscription = createResult.getTarget();
        
        SubscriptionRequest updateRequest = new SubscriptionRequest().id("invalid id");
        Result<Subscription> result = gateway.subscription().update(createdSubscription.getId(), updateRequest);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertNull(result.getTarget());
        ValidationErrors errors = result.getErrors();
        Assert.assertEquals(ValidationErrorCode.SUBSCRIPTION_TOKEN_FORMAT_IS_INVALID, errors.forObject("subscription").onField("id").get(0).getCode());

        
    }
    @Test
    public void getParamsOnError() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId()).
            id("invalid id");

        Result<Subscription> createResult = gateway.subscription().create(request);
        Assert.assertFalse(createResult.isSuccess());
        Assert.assertNull(createResult.getTarget());
        Map<String, String> parameters = createResult.getParameters();
        Assert.assertEquals(creditCard.getToken(), parameters.get("payment_method_token"));
        Assert.assertEquals(plan.getId(), parameters.get("plan_id"));
        Assert.assertEquals("invalid id", parameters.get("id"));
    }
    
    @Test
    public void cancel() {
        Plan plan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        Result<Subscription> cancelResult = gateway.subscription().cancel(createResult.getTarget().getId());
        
        Assert.assertTrue(cancelResult.isSuccess());
        Assert.assertEquals(Subscription.Status.CANCELED, cancelResult.getTarget().getStatus());
        Assert.assertEquals(Subscription.Status.CANCELED, gateway.subscription().find(createResult.getTarget().getId()).getStatus());
    }
    
    @Test
    public void searchOnBillingCyclesRemaining() {
        SubscriptionRequest request12 = new SubscriptionRequest().
            numberOfBillingCycles(12).
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId()).
            price(new BigDecimal(5));
        Subscription subscription12 = gateway.subscription().create(request12).getTarget();
        
        SubscriptionRequest request11 = new SubscriptionRequest().
            numberOfBillingCycles(11).
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId()).
            price(new BigDecimal(5));
        Subscription subscription11 = gateway.subscription().create(request11).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            billingCyclesRemaining().is(12).
            price().is(new BigDecimal(5));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription12));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription11));        
    }

    @Test
    public void searchOnDaysPastDue() {
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId());
            
        Subscription subscription = gateway.subscription().create(request).getTarget();

        makePastDue(subscription, 3);

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            daysPastDue().between(2, 10);
        ResourceCollection<Subscription> results = gateway.subscription().search(search);

        Assert.assertTrue(results.getMaximumSize() > 0);
        for (Subscription foundSubscription : results) {
            Assert.assertTrue(foundSubscription.getDaysPastDue() >= 2 && foundSubscription.getDaysPastDue() <= 10);
        }
    }

    @Test
    public void searchOnIdIs() {
        Random rand = new Random();
        SubscriptionRequest request1 = new SubscriptionRequest().
            id("find_me" + rand.nextInt()).
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId()).
            price(new BigDecimal(2));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            id("do_not_find_me" + rand.nextInt()).
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId()).
            price(new BigDecimal(2));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            id().startsWith("find_me").
            price().is(new BigDecimal(2));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnMerchantAccountIdIs() {
        SubscriptionRequest request1 = new SubscriptionRequest().
            merchantAccountId(MerchantAccount.DEFAULT_MERCHANT_ACCOUNT_ID).
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId()).
            price(new BigDecimal(3));
        Subscription subscriptionDefaultMerchantAccount = gateway.subscription().create(request1).getTarget();

        SubscriptionRequest request2 = new SubscriptionRequest().
            merchantAccountId(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId()).
            price(new BigDecimal(3));
        Subscription subscriptionNonDefaultMerchantAccount = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            merchantAccountId().is(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            price().is(new BigDecimal(3));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscriptionNonDefaultMerchantAccount));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscriptionDefaultMerchantAccount));
    }

    @Test
    public void searchOnPlanIdIs() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        Plan triallessPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(7));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId()).
            price(new BigDecimal(7));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().is(trialPlan.getId()).
            price().is(new BigDecimal(7));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void searchOnPlanIdIsNot() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        Plan triallessPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(8));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId()).
            price(new BigDecimal(8));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().isNot(trialPlan.getId()).
            price().is(new BigDecimal(8));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription2));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription1));
    }
    
    @Test
    public void searchOnPlanIdEndsWith() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        Plan triallessPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(9));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId()).
            price(new BigDecimal(9));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().endsWith("trial_plan").
            price().is(new BigDecimal(9));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void searchOnPlanIdStartsWith() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        Plan triallessPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(10));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId()).
            price(new BigDecimal(10));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().startsWith("integration_trial_p").
            price().is(new BigDecimal(10));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void searchOnPlanIdContains() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        Plan triallessPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(11));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId()).
            price(new BigDecimal(11));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().contains("trial_p").
            price().is(new BigDecimal(11));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnPlanIdIn() {
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITH_TRIAL.getId()).
            price(new BigDecimal(6));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITHOUT_TRIAL.getId()).
            price(new BigDecimal(6));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionRequest request3 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.ADD_ON_DISCOUNT_PLAN.getId()).
            price(new BigDecimal(6));
        Subscription subscription3 = gateway.subscription().create(request3).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().in(Plan.PLAN_WITH_TRIAL.getId(), Plan.PLAN_WITHOUT_TRIAL.getId()).
            price().is(new BigDecimal(6));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription2));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription3));
    }
 
    @Test
    public void searchOnStatusIn() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(12));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(12));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        gateway.subscription().cancel(subscription2.getId());
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            status().in(Status.ACTIVE).
            price().is(new BigDecimal(12));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void searchOnStatusExpired() {
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            status().in(Status.EXPIRED);
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        
        Assert.assertTrue(results.getMaximumSize() > 0);
        for (Subscription subscription : results) {
            Assert.assertEquals(Status.EXPIRED, subscription.getStatus());
        }
    }
    
    @Test
    public void searchOnStatusInWithMultipleStatusesAsList() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(13));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(13));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        gateway.subscription().cancel(subscription2.getId());
        
        List<Status> statuses = new ArrayList<Status>();
        statuses.add(Status.ACTIVE);
        statuses.add(Status.CANCELED);
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            status().in(statuses).
            price().is(new BigDecimal(13));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void searchOnStatusInWithMultipleStatuses() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(14));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId()).
            price(new BigDecimal(14));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        gateway.subscription().cancel(subscription2.getId());
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            status().in(Status.ACTIVE, Status.CANCELED).
            price().is(new BigDecimal(14));
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void unrecognizedStatus() {
        String xml = "<subscription><status>foobar</status></subscription>";
        Subscription transaction = new Subscription(new NodeWrapper(xml));
        Assert.assertEquals(Subscription.Status.UNRECOGNIZED, transaction.getStatus());
    }

    @Test
    public void unrecognizedDurationUnit() {
        String xml = "<subscription><trial-duration-unit>foobar</trial-duration-unit></subscription>";
        Subscription transaction = new Subscription(new NodeWrapper(xml));
        Assert.assertEquals(Subscription.DurationUnit.UNRECOGNIZED, transaction.getTrialDurationUnit());
    }

    @Test
    public void retryChargeWithAmount() {
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITHOUT_TRIAL.getId());

        Subscription subscription = gateway.subscription().create(request).getTarget();

        makePastDue(subscription, 1);

        Result<Transaction> result = gateway.subscription().retryCharge(subscription.getId(), TransactionAmount.AUTHORIZE.amount);
        Assert.assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();
        Assert.assertEquals(TransactionAmount.AUTHORIZE.amount, transaction.getAmount());
        Assert.assertNotNull(transaction.getProcessorAuthorizationCode());
        Assert.assertEquals(Transaction.Type.SALE, transaction.getType());
        Assert.assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getCreatedAt().get(Calendar.YEAR));
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getUpdatedAt().get(Calendar.YEAR));
    }
    
    @Test
    public void retryChargeWithoutAmount() {
        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(Plan.PLAN_WITHOUT_TRIAL.getId());

        Subscription subscription = gateway.subscription().create(request).getTarget();
        makePastDue(subscription, 1);
        
        Result<Transaction> result = gateway.subscription().retryCharge(subscription.getId());
        Assert.assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();
        Assert.assertEquals(subscription.getPrice(), transaction.getAmount());
        Assert.assertNotNull(transaction.getProcessorAuthorizationCode());
        Assert.assertEquals(Transaction.Type.SALE, transaction.getType());
        Assert.assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getCreatedAt().get(Calendar.YEAR));
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getUpdatedAt().get(Calendar.YEAR));
    }
    
    private void makePastDue(Subscription subscription, int numberOfDaysPastDue) {
        NodeWrapper response = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), BraintreeGateway.VERSION).put("/subscriptions/" + subscription.getId() + "/make_past_due?days_past_due=" + numberOfDaysPastDue);
        Assert.assertTrue(response.isSuccess());
    }
}
