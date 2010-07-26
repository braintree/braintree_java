
package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.Subscription.Status;
import com.braintreegateway.util.NodeWrapper;

public class SubscriptionTest {

    private final class SortModificationsByPrice implements Comparator<Modification> {
        public int compare(Modification left, Modification right) {
            return left.getAmount().compareTo(right.getAmount());
        }
    }

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

        Calendar expectedBillingPeriodEndDate = new GregorianCalendar();
        expectedBillingPeriodEndDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedBillingPeriodEndDate.roll(Calendar.MONTH, plan.getBillingFrequency());
        expectedBillingPeriodEndDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar expectedNextBillingDate = new GregorianCalendar();
        expectedNextBillingDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedNextBillingDate.roll(Calendar.MONTH, plan.getBillingFrequency());
        Calendar expectedBillingPeriodStartDate = new GregorianCalendar();
        expectedBillingPeriodStartDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        Calendar expectedFirstDate = new GregorianCalendar();
        expectedFirstDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        
        Assert.assertEquals(creditCard.getToken(), subscription.getPaymentMethodToken());
        Assert.assertEquals(plan.getId(), subscription.getPlanId());
        Assert.assertEquals(plan.getPrice(), subscription.getPrice());
        Assert.assertEquals(new BigDecimal("12.34"), subscription.getNextBillAmount());
        Assert.assertTrue(subscription.getId().matches("^\\w{6}$"));
        Assert.assertEquals(Subscription.Status.ACTIVE, subscription.getStatus());
        Assert.assertEquals(new Integer(0), subscription.getFailureCount());
        Assert.assertEquals(false, subscription.hasTrialPeriod());
        Assert.assertEquals(MerchantAccount.DEFAULT_MERCHANT_ACCOUNT_ID, subscription.getMerchantAccountId());
        
        TestHelper.assertDatesEqual(expectedBillingPeriodEndDate, subscription.getBillingPeriodEndDate());
        TestHelper.assertDatesEqual(expectedBillingPeriodStartDate, subscription.getBillingPeriodStartDate());
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

        Calendar expectedFirstAndNextBillingDate = new GregorianCalendar();
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
        Collections.sort(addOns, new SortModificationsByPrice());

        Assert.assertEquals(2, addOns.size());

        Assert.assertEquals(new BigDecimal("10.00"), addOns.get(0).getAmount());
        Assert.assertEquals(new Integer(1), addOns.get(0).getQuantity());
        Assert.assertTrue(addOns.get(0).neverExpires());
        Assert.assertNull(addOns.get(0).getNumberOfBillingCycles());
        
        Assert.assertEquals(new BigDecimal("20.00"), addOns.get(1).getAmount());
        Assert.assertEquals(new Integer(1), addOns.get(1).getQuantity());
        Assert.assertTrue(addOns.get(1).neverExpires());
        Assert.assertNull(addOns.get(1).getNumberOfBillingCycles());
        
        List<Discount> discounts = subscription.getDiscounts();
        Collections.sort(discounts, new SortModificationsByPrice());
        
        Assert.assertEquals(2, discounts.size());

        Assert.assertEquals(new BigDecimal("7.00"), discounts.get(0).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());
        Assert.assertTrue(discounts.get(0).neverExpires());
        Assert.assertNull(discounts.get(0).getNumberOfBillingCycles());
        
        Assert.assertEquals(new BigDecimal("11.00"), discounts.get(1).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());
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
                    quantity(9).
                    done().
                update("increase_20").
                    amount(new BigDecimal("40.00")).
                    done().
                done().
            discounts().
                update("discount_7").
                    amount(new BigDecimal("15.00")).
                    done().
                update("discount_11").
                    amount(new BigDecimal("23.00")).
                    done().
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        List<AddOn> addOns = subscription.getAddOns();
        Collections.sort(addOns, new SortModificationsByPrice());

        Assert.assertEquals(2, addOns.size());

        Assert.assertEquals(new BigDecimal("30.00"), addOns.get(0).getAmount());
        Assert.assertEquals(new Integer(9), addOns.get(0).getQuantity());
        
        Assert.assertEquals(new BigDecimal("40.00"), addOns.get(1).getAmount());
        Assert.assertEquals(new Integer(1), addOns.get(1).getQuantity());
        
        List<Discount> discounts = subscription.getDiscounts();
        Collections.sort(discounts, new SortModificationsByPrice());
        
        Assert.assertEquals(2, discounts.size());

        Assert.assertEquals(new BigDecimal("15.00"), discounts.get(0).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());
        
        Assert.assertEquals(new BigDecimal("23.00"), discounts.get(1).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());
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
                    quantity(3).
                    done().
                done().
            discounts().
                remove("discount_7", "discount_11").
                add().
                    inheritedFromId("discount_15").
                    amount(new BigDecimal("17.00")).
                    quantity(2).
                    done().
                done();
            
        Result<Subscription> result = gateway.subscription().create(request);
        Assert.assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        Assert.assertEquals(1, subscription.getAddOns().size());

        Assert.assertEquals(new BigDecimal("40.00"), subscription.getAddOns().get(0).getAmount());
        Assert.assertEquals(new Integer(3), subscription.getAddOns().get(0).getQuantity());
        
        Assert.assertEquals(1, subscription.getDiscounts().size());

        Assert.assertEquals(new BigDecimal("17.00"), subscription.getDiscounts().get(0).getAmount());
        Assert.assertEquals(new Integer(2), subscription.getDiscounts().get(0).getQuantity());
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
    public void increasePriceAndTransaction() {
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
        Collections.sort(addOns, new SortModificationsByPrice());

        Assert.assertEquals(2, addOns.size());

        Assert.assertEquals(new BigDecimal("30.00"), addOns.get(0).getAmount());
        Assert.assertEquals(new Integer(9), addOns.get(0).getQuantity());
        
        Assert.assertEquals(new BigDecimal("31.00"), addOns.get(1).getAmount());
        Assert.assertEquals(new Integer(7), addOns.get(1).getQuantity());
        
        List<Discount> discounts = updatedSubscription.getDiscounts();
        Collections.sort(discounts, new SortModificationsByPrice());
        
        Assert.assertEquals(2, discounts.size());

        Assert.assertEquals(new BigDecimal("15.00"), discounts.get(0).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());
        
        Assert.assertEquals(new BigDecimal("23.00"), discounts.get(1).getAmount());
        Assert.assertEquals(new Integer(1), discounts.get(0).getQuantity());
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
    public void searchOnPlanIdIs() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        Plan triallessPlan = Plan.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().is(trialPlan.getId());
        
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
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().isNot(trialPlan.getId());
        
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
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().endsWith("trial_plan");
        
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
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().startsWith("integration_trial_p");
        
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
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(triallessPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            planId().contains("trial_p");
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertFalse(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void searchOnStatusIn() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        gateway.subscription().cancel(subscription2.getId());
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            status().in(Status.ACTIVE);
        
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
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        gateway.subscription().cancel(subscription2.getId());
        
        List<Status> statuses = new ArrayList<Status>();
        statuses.add(Status.ACTIVE);
        statuses.add(Status.CANCELED);
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            status().in(statuses);
        
        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription1));
        Assert.assertTrue(TestHelper.includesSubscription(results, subscription2));
    }
    
    @Test
    public void searchOnStatusInWithMultipleStatuses() {
        Plan trialPlan = Plan.PLAN_WITH_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId());
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();
        
        SubscriptionRequest request2 = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(trialPlan.getId());
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();
        gateway.subscription().cancel(subscription2.getId());
        
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
            status().in(Status.ACTIVE, Status.CANCELED);
        
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
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().status().in(Subscription.Status.PAST_DUE);
        Subscription subscription = gateway.subscription().search(search).getFirst();

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
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().status().in(Subscription.Status.PAST_DUE);
        Subscription subscription = gateway.subscription().search(search).getFirst();

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
}
