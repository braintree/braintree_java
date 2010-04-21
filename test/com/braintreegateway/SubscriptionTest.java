
package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.Subscription.Status;

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
        
        Assert.assertEquals(1, subscription.getTransactions().size());
        Assert.assertEquals(new BigDecimal("482.48"), subscription.getTransactions().get(0).getAmount());
        Assert.assertEquals(Transaction.Type.SALE, subscription.getTransactions().get(0).getType());
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


    @Test(expected = NotFoundException.class)
    public void createWithBadPlanId() {
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId("noSuchPlanId");

        gateway.subscription().create(createRequest);
    }
    
    @Test(expected = NotFoundException.class)
    public void createWithBadPaymentMethod() {
        SubscriptionRequest createRequest = new SubscriptionRequest().
            paymentMethodToken("invalidToken").
            planId(Plan.PLAN_WITHOUT_TRIAL.getId());

        gateway.subscription().create(createRequest);
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
    
}
