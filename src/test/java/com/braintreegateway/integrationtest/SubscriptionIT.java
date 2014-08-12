package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.Subscription.Status;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class SubscriptionIT implements MerchantAccountTestConstants {

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
        assertTrue(result.isSuccess());
        this.customer = result.getTarget();
        this.creditCard = customer.getCreditCards().get(0);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createSimpleSubscriptionWithoutTrial() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Calendar expectedBillingPeriodEndDate = Calendar.getInstance();
        expectedBillingPeriodEndDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedBillingPeriodEndDate.add(Calendar.MONTH, plan.getBillingFrequency());
        expectedBillingPeriodEndDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar expectedNextBillingDate = Calendar.getInstance();
        expectedNextBillingDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedNextBillingDate.add(Calendar.MONTH, plan.getBillingFrequency());
        Calendar expectedBillingPeriodStartDate = Calendar.getInstance();
        expectedBillingPeriodStartDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        Calendar expectedFirstDate = Calendar.getInstance();
        expectedFirstDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));

        assertEquals(creditCard.getToken(), subscription.getPaymentMethodToken());
        assertEquals(plan.getId(), subscription.getPlanId());
        assertEquals(plan.getPrice(), subscription.getPrice());
        assertEquals(new BigDecimal("0.00"), subscription.getBalance());
        assertEquals(new Integer(1), subscription.getCurrentBillingCycle());
        assertEquals(new BigDecimal("12.34"), subscription.getNextBillAmount());
        assertEquals(new BigDecimal("12.34"), subscription.getNextBillingPeriodAmount());
        assertTrue(subscription.getId().matches("^\\w{6}$"));
        assertEquals(Subscription.Status.ACTIVE, subscription.getStatus());
        assertEquals(new Integer(0), subscription.getFailureCount());
        assertEquals(false, subscription.hasTrialPeriod());
        assertEquals(DEFAULT_MERCHANT_ACCOUNT_ID, subscription.getMerchantAccountId());

        TestHelper.assertDatesEqual(expectedBillingPeriodEndDate, subscription.getBillingPeriodEndDate());
        TestHelper.assertDatesEqual(expectedBillingPeriodStartDate, subscription.getBillingPeriodStartDate());
        TestHelper.assertDatesEqual(expectedBillingPeriodEndDate, subscription.getPaidThroughDate());
        TestHelper.assertDatesEqual(expectedNextBillingDate, subscription.getNextBillingDate());
        TestHelper.assertDatesEqual(expectedFirstDate, subscription.getFirstBillingDate());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createSimpleSubscriptionWithPaymentMethodNonce() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        String customerId = creditCard.getCustomerId();
        String nonce = TestHelper.generateUnlockedNonce(gateway, customerId, "4111111111111111");
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodNonce(nonce).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());

        Subscription subscription = createResult.getTarget();
        assertEquals("1111", subscription.getTransactions().get(0).getCreditCard().getLast4());
    }

    @Test
    public void createReturnsTransactionWithSubscriptionBillingPeriod() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        Transaction transaction = subscription.getTransactions().get(0);
        assertEquals(subscription.getBillingPeriodStartDate(), transaction.getSubscription().getBillingPeriodStartDate());
        assertEquals(subscription.getBillingPeriodEndDate(), transaction.getSubscription().getBillingPeriodEndDate());
    }

    @Test
    public void createSimpleSubscriptionWithTrial() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Calendar expectedFirstAndNextBillingDate = Calendar.getInstance();
        expectedFirstAndNextBillingDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        expectedFirstAndNextBillingDate.add(Calendar.DAY_OF_MONTH, plan.getTrialDuration());

        assertEquals(plan.getId(), subscription.getPlanId());
        assertEquals(plan.getPrice(), subscription.getPrice());
        assertEquals(creditCard.getToken(), subscription.getPaymentMethodToken());
        assertTrue(subscription.getId().matches("^\\w{6}$"));
        assertEquals(Subscription.Status.ACTIVE, subscription.getStatus());

        assertEquals(null, subscription.getBillingPeriodStartDate());
        assertEquals(null, subscription.getBillingPeriodEndDate());
        assertEquals(new Integer(0), subscription.getCurrentBillingCycle());

        assertEquals(new Integer(0), subscription.getFailureCount());
        assertEquals(true, subscription.hasTrialPeriod());
        assertEquals(plan.getTrialDuration(), subscription.getTrialDuration());
        assertEquals(plan.getTrialDurationUnit().toString(), subscription.getTrialDurationUnit().toString());

        TestHelper.assertDatesEqual(expectedFirstAndNextBillingDate, subscription.getNextBillingDate());
        TestHelper.assertDatesEqual(expectedFirstAndNextBillingDate, subscription.getFirstBillingDate());
    }

    @Test
    public void createWithPayPalAccountToken() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> paypalResult = gateway.paymentMethod().create(paymentMethodRequest);
        assertTrue(paypalResult.isSuccess());

        PaymentMethod paymentMethod = paypalResult.getTarget();

        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(paymentMethod.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(paymentMethod.getToken(), subscription.getPaymentMethodToken());
    }

    @Test
    public void overridePlanAddTrial() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                trialPeriod(true).
                trialDuration(2).
                trialDurationUnit(Subscription.DurationUnit.MONTH);

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(true, subscription.hasTrialPeriod());
        assertEquals(new Integer(2), subscription.getTrialDuration());
        assertEquals(Subscription.DurationUnit.MONTH, subscription.getTrialDurationUnit());
    }

    @Test
    public void overridePlanRemoveTrial() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                trialPeriod(false);

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(false, subscription.hasTrialPeriod());
    }

    @Test
    public void overridePlanPrice() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                price(new BigDecimal("482.48"));

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(new BigDecimal("482.48"), subscription.getPrice());
    }

    @Test
    public void overridePlanNumberOfBillingCycles() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Subscription subscription = gateway.subscription().create(request).getTarget();
        assertEquals(plan.getNumberOfBillingCycles(), subscription.getNumberOfBillingCycles());

        SubscriptionRequest overrideRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                numberOfBillingCycles(10);

        Subscription overriddenSubsription = gateway.subscription().create(overrideRequest).getTarget();
        assertEquals(new Integer(10), overriddenSubsription.getNumberOfBillingCycles());
        assertFalse(overriddenSubsription.neverExpires());
    }

    @Test
    public void setNeverExpires() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                neverExpires(true);

        Subscription subscription = gateway.subscription().create(request).getTarget();
        assertNull(subscription.getNumberOfBillingCycles());
        assertTrue(subscription.neverExpires());
    }

    @Test
    public void setNumberOfBillingCyclesAndUpdateToNeverExpire() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                numberOfBillingCycles(10);

        Subscription subscription = gateway.subscription().create(request).getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().
                neverExpires(true);

        Subscription updatedSubscription = gateway.subscription().update(subscription.getId(), updateRequest).getTarget();

        assertNull(updatedSubscription.getNumberOfBillingCycles());
    }

    @Test
    public void setNumberOfBillingCyclesAndUpdate() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                numberOfBillingCycles(10);

        Subscription subscription = gateway.subscription().create(request).getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().
                numberOfBillingCycles(14);

        Subscription updatedSubscription = gateway.subscription().update(subscription.getId(), updateRequest).getTarget();

        assertEquals(new Integer(14), updatedSubscription.getNumberOfBillingCycles());
    }

    @Test
    public void inheritBillingDayOfMonth() {
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.BILLING_DAY_OF_MONTH_PLAN.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(new Integer(5), subscription.getBillingDayOfMonth());
    }

    @Test
    public void overrideBillingDayOfMonth() {
        SubscriptionRequest request = new SubscriptionRequest().
                billingDayOfMonth(19).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.BILLING_DAY_OF_MONTH_PLAN.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(new Integer(19), subscription.getBillingDayOfMonth());
    }

    @Test
    public void overrideBillingDayOfMonthWithStartImmediately() {
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.BILLING_DAY_OF_MONTH_PLAN.getId()).
                options().
                startImmediately(true).
                done();

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(1, subscription.getTransactions().size());
    }

    @Test
    public void setFirstBillingDate() {
        Calendar firstBillingDate = Calendar.getInstance();
        firstBillingDate.add(Calendar.DAY_OF_MONTH, 3);
        firstBillingDate.setTimeZone(TimeZone.getTimeZone("UTC"));

        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.BILLING_DAY_OF_MONTH_PLAN.getId()).
                firstBillingDate(firstBillingDate);

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        TestHelper.assertDatesEqual(firstBillingDate, subscription.getFirstBillingDate());
        assertEquals(Subscription.Status.PENDING, subscription.getStatus());
    }

    @Test
    public void setFirstBillingDateInThePast() {
        Calendar firstBillingDate = Calendar.getInstance();
        firstBillingDate.add(Calendar.DAY_OF_MONTH, -3);

        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.BILLING_DAY_OF_MONTH_PLAN.getId()).
                firstBillingDate(firstBillingDate);

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertFalse(createResult.isSuccess());

        List<ValidationError> errors = createResult.getErrors().forObject("subscription").onField("firstBillingDate");
        assertEquals(ValidationErrorCode.SUBSCRIPTION_FIRST_BILLING_DATE_CANNOT_BE_IN_THE_PAST, errors.get(0).getCode());
    }

    @Test
    public void setId() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        String newId = "new-id-" + new Random().nextInt();
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                price(new BigDecimal("482.48")).
                id(newId);


        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(newId, subscription.getId());
    }

    @Test
    public void setMerchantAccountId() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                price(new BigDecimal("482.48")).
                merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID);

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(NON_DEFAULT_MERCHANT_ACCOUNT_ID, subscription.getMerchantAccountId());
    }

    @Test
    public void hasTransactionOnCreateWithNoTrial() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                price(new BigDecimal("482.48"));


        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        Transaction transaction = subscription.getTransactions().get(0);

        assertEquals(1, subscription.getTransactions().size());
        assertEquals(new BigDecimal("482.48"), transaction.getAmount());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(subscription.getId(), transaction.getSubscriptionId());
    }

    @Test
    public void hasTransactionOnCreateWhenTransactionFails() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                price(SandboxValues.TransactionAmount.DECLINE.amount);

        Result<Subscription> result = gateway.subscription().create(request);
        assertFalse(result.isSuccess());
        assertEquals(Transaction.Status.PROCESSOR_DECLINED, result.getTransaction().getStatus());
    }

    @Test
    public void hasNoTransactionOnCreateWithATrial() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());


        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        assertEquals(0, subscription.getTransactions().size());
    }

    @Test
    public void createInheritsNoAddOnsAndDiscountsWhenOptionIsPassed() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                options().
                doNotInheritAddOnsOrDiscounts(true).
                done();

        Result<Subscription> result = gateway.subscription().create(request);
        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        assertEquals(0, subscription.getAddOns().size());
        assertEquals(0, subscription.getDiscounts().size());
    }

    @Test
    public void createInheritsAddOnsAndDiscountsFromPlan() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> result = gateway.subscription().create(request);
        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        List<AddOn> addOns = subscription.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        assertEquals(2, addOns.size());

        assertEquals("increase_10", addOns.get(0).getId());
        assertEquals(new BigDecimal("10.00"), addOns.get(0).getAmount());
        assertEquals(new Integer(1), addOns.get(0).getQuantity());
        assertTrue(addOns.get(0).neverExpires());
        assertNull(addOns.get(0).getNumberOfBillingCycles());
        assertEquals(new Integer(0), addOns.get(0).getCurrentBillingCycle());

        assertEquals("increase_20", addOns.get(1).getId());
        assertEquals(new BigDecimal("20.00"), addOns.get(1).getAmount());
        assertEquals(new Integer(1), addOns.get(1).getQuantity());
        assertTrue(addOns.get(1).neverExpires());
        assertNull(addOns.get(1).getNumberOfBillingCycles());
        assertEquals(new Integer(0), addOns.get(1).getCurrentBillingCycle());

        List<Discount> discounts = subscription.getDiscounts();
        Collections.sort(discounts, new TestHelper.CompareModificationsById());

        assertEquals(2, discounts.size());

        assertEquals("discount_11", discounts.get(0).getId());
        assertEquals(new BigDecimal("11.00"), discounts.get(0).getAmount());
        assertEquals(new Integer(1), discounts.get(0).getQuantity());
        assertTrue(discounts.get(0).neverExpires());
        assertNull(discounts.get(0).getNumberOfBillingCycles());
        assertEquals(new Integer(0), discounts.get(0).getCurrentBillingCycle());

        assertEquals("discount_7", discounts.get(1).getId());
        assertEquals(new BigDecimal("7.00"), discounts.get(1).getAmount());
        assertEquals(new Integer(1), discounts.get(1).getQuantity());
        assertTrue(discounts.get(1).neverExpires());
        assertNull(discounts.get(1).getNumberOfBillingCycles());
        assertEquals(new Integer(0), discounts.get(1).getCurrentBillingCycle());
    }

    @Test
    public void createOverridesInheritedAddOnsAndDiscounts() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
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
        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        List<AddOn> addOns = subscription.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        assertEquals(2, addOns.size());

        assertEquals("increase_10", addOns.get(0).getId());
        assertEquals(new BigDecimal("30.00"), addOns.get(0).getAmount());
        assertEquals(new Integer(3), addOns.get(0).getNumberOfBillingCycles());
        assertFalse(addOns.get(0).neverExpires());
        assertEquals(new Integer(9), addOns.get(0).getQuantity());
        assertEquals(new Integer(0), addOns.get(0).getCurrentBillingCycle());

        assertEquals("increase_20", addOns.get(1).getId());
        assertEquals(new BigDecimal("40.00"), addOns.get(1).getAmount());
        assertEquals(new Integer(1), addOns.get(1).getQuantity());
        assertEquals(new Integer(0), addOns.get(1).getCurrentBillingCycle());

        List<Discount> discounts = subscription.getDiscounts();
        Collections.sort(discounts, new TestHelper.CompareModificationsById());

        assertEquals(2, discounts.size());

        assertEquals("discount_11", discounts.get(0).getId());
        assertEquals(new BigDecimal("23.00"), discounts.get(0).getAmount());
        assertNull(discounts.get(0).getNumberOfBillingCycles());
        assertTrue(discounts.get(0).neverExpires());
        assertEquals(new Integer(1), discounts.get(0).getQuantity());
        assertEquals(new Integer(0), discounts.get(0).getCurrentBillingCycle());

        assertEquals("discount_7", discounts.get(1).getId());
        assertEquals(new BigDecimal("15.00"), discounts.get(1).getAmount());
        assertEquals(new Integer(1), discounts.get(1).getQuantity());
        assertEquals(new Integer(0), discounts.get(1).getCurrentBillingCycle());
    }

    @Test
    public void createRemovesInheritedAddOnsAndDiscounts() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
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
        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        assertEquals(0, subscription.getAddOns().size());
        assertEquals(0, subscription.getDiscounts().size());
    }

    @Test
    public void createRemovesInheritedAddOnsAndDiscountsWithListsOrChaining() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                addOns().
                remove(Arrays.asList("increase_10", "increase_20")).
                done().
                discounts().
                remove("discount_7").
                remove("discount_11").
                done();

        Result<Subscription> result = gateway.subscription().create(request);
        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        assertEquals(0, subscription.getAddOns().size());
        assertEquals(0, subscription.getDiscounts().size());
    }

    @Test
    public void createAddsNewAddOnsAndDiscounts() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
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
        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        assertEquals(1, subscription.getAddOns().size());

        assertEquals(new BigDecimal("40.00"), subscription.getAddOns().get(0).getAmount());
        assertEquals(new Integer(6), subscription.getAddOns().get(0).getNumberOfBillingCycles());
        assertFalse(subscription.getAddOns().get(0).neverExpires());
        assertEquals(new Integer(3), subscription.getAddOns().get(0).getQuantity());
        assertEquals(new Integer(0), subscription.getAddOns().get(0).getCurrentBillingCycle());

        assertEquals(1, subscription.getDiscounts().size());

        assertEquals(new BigDecimal("17.00"), subscription.getDiscounts().get(0).getAmount());
        assertNull(subscription.getDiscounts().get(0).getNumberOfBillingCycles());
        assertTrue(subscription.getDiscounts().get(0).neverExpires());
        assertEquals(new Integer(2), subscription.getDiscounts().get(0).getQuantity());
        assertEquals(new Integer(0), subscription.getDiscounts().get(0).getCurrentBillingCycle());
    }

    @Test
    public void createWithBadQuantityCorrectlyParsesValidationErrors() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
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
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.SUBSCRIPTION_MODIFICATION_AMOUNT_IS_INVALID,
                result.getErrors().forObject("subscription").forObject("addOns").forObject("update").forIndex(0).onField("amount").get(0).getCode());
        assertEquals(ValidationErrorCode.SUBSCRIPTION_MODIFICATION_QUANTITY_IS_INVALID,
                result.getErrors().forObject("subscription").forObject("addOns").forObject("update").forIndex(1).onField("quantity").get(0).getCode());
    }

    @Test
    public void find() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Subscription foundSubscription = gateway.subscription().find(subscription.getId());
        assertEquals(subscription.getId(), foundSubscription.getId());
        assertEquals(subscription.getPaymentMethodToken(), creditCard.getToken());
        assertEquals(subscription.getPlanId(), plan.getId());
    }

    @Test
    public void findWithEmptyIds() {
        try {
            gateway.subscription().find(" ");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }

    }

    @Test
    public void updateId() {
        String oldId = "old-id-" + new Random().nextInt();
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                id(oldId);

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());

        String newId = "new-id-" + new Random().nextInt();
        SubscriptionRequest updateRequest = new SubscriptionRequest().id(newId);
        Result<Subscription> result = gateway.subscription().update(oldId, updateRequest);

        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        assertEquals(newId, subscription.getId());
        assertNotNull(gateway.subscription().find(newId));
    }

    @Test
    public void updateMerchantAccountId() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());

        SubscriptionRequest updateRequest = new SubscriptionRequest()
                .merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID);
        Result<Subscription> result = gateway.subscription().update(createResult.getTarget().getId(), updateRequest);

        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();

        assertEquals(NON_DEFAULT_MERCHANT_ACCOUNT_ID, subscription.getMerchantAccountId());
    }

    @Test
    public void updatePlan() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        Plan newPlan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest updateRequest = new SubscriptionRequest().planId(newPlan.getId());
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);

        assertTrue(result.isSuccess());
        subscription = result.getTarget();

        assertEquals(newPlan.getId(), subscription.getPlanId());
    }

    @Test
    public void updatePaymentMethod() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
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

        assertTrue(result.isSuccess());
        subscription = result.getTarget();

        assertEquals(newCreditCard.getToken(), subscription.getPaymentMethodToken());
    }

    @Test
    public void createAProrationTransactionOnPriceIncreaseWhenFlagIsNotPassed() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId()).
                price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().price(new BigDecimal("4.56"));
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);

        assertTrue(result.isSuccess());
        subscription = result.getTarget();

        assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        assertEquals(2, subscription.getTransactions().size());
    }

    @Test
    public void createAProrationTransactionOnPriceIncreaseWhenProrationFlagIsTrue() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId()).
                price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().
                price(new BigDecimal("4.56")).
                options().
                prorateCharges(true).
                done();
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);

        assertTrue(result.isSuccess());
        subscription = result.getTarget();

        assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        assertEquals(2, subscription.getTransactions().size());
    }

    @Test
    public void doNotCreateAProrationTransactionOnPriceIncreaseWhenProrationFlagIsFalse() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId()).
                price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().
                price(new BigDecimal("4.56")).
                options().
                prorateCharges(false).
                done();
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);

        assertTrue(result.isSuccess());
        subscription = result.getTarget();

        assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        assertEquals(1, subscription.getTransactions().size());
    }

    @Test
    public void doNotUpdateIfReverting() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId()).
                price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription createdSubscription = createResult.getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().
                price(new BigDecimal("2100")).
                options().
                prorateCharges(true).
                revertSubscriptionOnProrationFailure(true).
                done();
        Result<Subscription> result = gateway.subscription().update(createdSubscription.getId(), updateRequest);

        assertFalse(result.isSuccess());
        Subscription subscription = result.getSubscription();

        assertEquals(createdSubscription.getTransactions().size() + 1, subscription.getTransactions().size());
        assertEquals(Transaction.Status.PROCESSOR_DECLINED, subscription.getTransactions().get(0).getStatus());

        assertEquals(new BigDecimal("0.00"), subscription.getBalance());
        assertEquals(new BigDecimal("1.23"), subscription.getPrice());
    }

    @Test
    public void UpdateIfNotReverting() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId()).
                price(new BigDecimal("1.23"));

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription createdSubscription = createResult.getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().
                price(new BigDecimal("2100")).
                options().
                prorateCharges(true).
                revertSubscriptionOnProrationFailure(false).
                done();
        Result<Subscription> result = gateway.subscription().update(createdSubscription.getId(), updateRequest);

        assertTrue(result.isSuccess());
        Subscription subscription = result.getTarget();
        assertEquals(createdSubscription.getTransactions().size() + 1, subscription.getTransactions().size());
        assertEquals(Transaction.Status.PROCESSOR_DECLINED, subscription.getTransactions().get(0).getStatus());

        assertEquals(subscription.getTransactions().get(0).getAmount(), subscription.getBalance());
        assertEquals(new BigDecimal("2100.00"), subscription.getPrice());
    }

    @Test
    public void dontIncreasePriceAndDontAddTransaction() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().price(new BigDecimal("4.56"));
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);

        assertTrue(result.isSuccess());
        subscription = result.getTarget();

        assertEquals(new BigDecimal("4.56"), subscription.getPrice());
        assertEquals(1, subscription.getTransactions().size());
    }

    @Test
    public void updateAddOnsAndDiscounts() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
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
        assertTrue(result.isSuccess());
        Subscription updatedSubscription = result.getTarget();

        List<AddOn> addOns = updatedSubscription.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        assertEquals(2, addOns.size());

        assertEquals(new BigDecimal("30.00"), addOns.get(0).getAmount());
        assertEquals(new Integer(9), addOns.get(0).getQuantity());

        assertEquals(new BigDecimal("31.00"), addOns.get(1).getAmount());
        assertEquals(new Integer(7), addOns.get(1).getQuantity());

        List<Discount> discounts = updatedSubscription.getDiscounts();
        Collections.sort(discounts, new TestHelper.CompareModificationsById());

        assertEquals(2, discounts.size());

        assertEquals("discount_15", discounts.get(0).getId());
        assertEquals(new BigDecimal("23.00"), discounts.get(0).getAmount());
        assertEquals(new Integer(1), discounts.get(0).getQuantity());

        assertEquals("discount_7", discounts.get(1).getId());
        assertEquals(new BigDecimal("15.00"), discounts.get(1).getAmount());
        assertEquals(new Integer(1), discounts.get(1).getQuantity());
    }

    @Test
    public void updateWithPaymentMethodNonce() {
        Plan originalPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(originalPlan.getId());

        Result<Subscription> createResult = gateway.subscription().create(createRequest);
        assertTrue(createResult.isSuccess());
        Subscription subscription = createResult.getTarget();
        String customerId = creditCard.getCustomerId();

        CreditCardRequest request = new CreditCardRequest().
                customerId(customer.getId()).
                cardholderName("John Doe").
                cvv("123").
                number("5105105105105100").
                expirationDate("05/12");

        CreditCard newCreditCard = gateway.creditCard().create(request).getTarget();
        String nonce = TestHelper.generateUnlockedNonce(gateway, customerId, "4111111111111111");

        SubscriptionRequest updateRequest = new SubscriptionRequest().paymentMethodNonce(nonce);
        Result<Subscription> result = gateway.subscription().update(subscription.getId(), updateRequest);

        assertTrue(result.isSuccess());
        subscription = result.getTarget();
    }

    @Test
    public void updateCanReplaceAllAddOnsAndDiscounts() {
        Plan plan = PlanFixture.ADD_ON_DISCOUNT_PLAN;
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
        assertTrue(result.isSuccess());
        Subscription updatedSubscription = result.getTarget();

        assertEquals(1, updatedSubscription.getAddOns().size());
        assertEquals(1, updatedSubscription.getDiscounts().size());
    }

    @Test
    public void updateWithDescriptor() {
        Plan plan = PlanFixture.PLAN_WITH_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                numberOfBillingCycles(10).
                descriptor().
                name("123*123456789012345678").
                phone("1234567890").
                done();

        Subscription subscription = gateway.subscription().create(request).getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().
                descriptor().
                name("999*99").
                phone("1234567891").
                done();

        Subscription updatedSubscription = gateway.subscription().update(subscription.getId(), updateRequest).getTarget();

        assertEquals("999*99", updatedSubscription.getDescriptor().getName());
        assertEquals("1234567891", updatedSubscription.getDescriptor().getPhone());
    }

    @Test
    public void createWithBadPlanId() {
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId("noSuchPlanId");

        Result<Subscription> result = gateway.subscription().create(createRequest);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.SUBSCRIPTION_PLAN_ID_IS_INVALID, result.getErrors().forObject("subscription").onField("planId").get(0).getCode());
    }

    @Test
    public void createWithBadPaymentMethod() {
        SubscriptionRequest createRequest = new SubscriptionRequest().
                paymentMethodToken("invalidToken").
                planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId());

        Result<Subscription> result = gateway.subscription().create(createRequest);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.SUBSCRIPTION_PAYMENT_METHOD_TOKEN_IS_INVALID, result.getErrors().forObject("subscription").onField("paymentMethodToken").get(0).getCode());
    }

    @Test
    public void createWithDescriptor() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                url("ebay.com").
                done();

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());

        Subscription subscription = createResult.getTarget();
        assertEquals("123*123456789012345678", subscription.getDescriptor().getName());
        assertEquals("3334445555", subscription.getDescriptor().getPhone());

        Transaction transaction = subscription.getTransactions().get(0);
        assertEquals("123*123456789012345678", transaction.getDescriptor().getName());
        assertEquals("3334445555", transaction.getDescriptor().getPhone());
        assertEquals("ebay.com", transaction.getDescriptor().getUrl());
    }

    @Test
    public void createWithDescriptorValidation() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                descriptor().
                name("xxxx").
                phone("xxx").
                url("12345678901234").
                done();

        Result<Subscription> result = gateway.subscription().create(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.DESCRIPTOR_NAME_FORMAT_IS_INVALID,
                result.getErrors().forObject("subscription").forObject("descriptor").onField("name").get(0).getCode());

        assertEquals(ValidationErrorCode.DESCRIPTOR_PHONE_FORMAT_IS_INVALID,
                result.getErrors().forObject("subscription").forObject("descriptor").onField("phone").get(0).getCode());

        assertEquals(ValidationErrorCode.DESCRIPTOR_URL_FORMAT_IS_INVALID,
                result.getErrors().forObject("subscription").forObject("descriptor").onField("url").get(0).getCode());
    }

    @Test
    public void validationErrorsOnCreate() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                id("invalid id");

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertFalse(createResult.isSuccess());
        assertNull(createResult.getTarget());
        ValidationErrors errors = createResult.getErrors();
        assertEquals(ValidationErrorCode.SUBSCRIPTION_TOKEN_FORMAT_IS_INVALID, errors.forObject("subscription").onField("id").get(0).getCode());
    }

    @Test
    public void validationErrorsOnUpdate() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertTrue(createResult.isSuccess());
        Subscription createdSubscription = createResult.getTarget();

        SubscriptionRequest updateRequest = new SubscriptionRequest().id("invalid id");
        Result<Subscription> result = gateway.subscription().update(createdSubscription.getId(), updateRequest);

        assertFalse(result.isSuccess());
        assertNull(result.getTarget());
        ValidationErrors errors = result.getErrors();
        assertEquals(ValidationErrorCode.SUBSCRIPTION_TOKEN_FORMAT_IS_INVALID, errors.forObject("subscription").onField("id").get(0).getCode());


    }

    @Test
    public void getParamsOnError() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                id("invalid id");

        Result<Subscription> createResult = gateway.subscription().create(request);
        assertFalse(createResult.isSuccess());
        assertNull(createResult.getTarget());
        Map<String, String> parameters = createResult.getParameters();
        assertEquals(plan.getId(), parameters.get("plan_id"));
        assertEquals("invalid id", parameters.get("id"));
    }

    @Test
    public void cancel() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId());

        Result<Subscription> createResult = gateway.subscription().create(request);
        Result<Subscription> cancelResult = gateway.subscription().cancel(createResult.getTarget().getId());

        assertTrue(cancelResult.isSuccess());
        assertEquals(Subscription.Status.CANCELED, cancelResult.getTarget().getStatus());
        assertEquals(Subscription.Status.CANCELED, gateway.subscription().find(createResult.getTarget().getId()).getStatus());
    }

    @Test
    public void searchOnBillingCyclesRemaining() {
        SubscriptionRequest request12 = new SubscriptionRequest().
                numberOfBillingCycles(12).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(5));
        Subscription subscription12 = gateway.subscription().create(request12).getTarget();

        SubscriptionRequest request11 = new SubscriptionRequest().
                numberOfBillingCycles(11).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(5));
        Subscription subscription11 = gateway.subscription().create(request11).getTarget();

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                billingCyclesRemaining().is(12).
                price().is(new BigDecimal(5));

        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(results, subscription12));
        assertFalse(TestHelper.includesSubscription(results, subscription11));
    }

    @Test
    public void searchOnDaysPastDue() {
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId());

        Subscription subscription = gateway.subscription().create(request).getTarget();

        makePastDue(subscription, 3);

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                daysPastDue().between(2, 10);
        ResourceCollection<Subscription> results = gateway.subscription().search(search);

        assertTrue(results.getMaximumSize() > 0);
        for (Subscription foundSubscription : results) {
            assertTrue(foundSubscription.getDaysPastDue() >= 2 && foundSubscription.getDaysPastDue() <= 10);
        }
    }

    @Test
    public void searchOnIdIs() {
        Random rand = new Random();
        SubscriptionRequest request1 = new SubscriptionRequest().
                id("find_me" + rand.nextInt()).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(2));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();

        SubscriptionRequest request2 = new SubscriptionRequest().
                id("do_not_find_me" + rand.nextInt()).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(2));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                id().startsWith("find_me").
                price().is(new BigDecimal(2));

        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnInTrialPeriod() {
        Random rand = new Random();
        SubscriptionRequest request1 = new SubscriptionRequest().
                id("find_me" + rand.nextInt()).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(2));
        Subscription subscriptionWithTrial = gateway.subscription().create(request1).getTarget();

        SubscriptionRequest request2 = new SubscriptionRequest().
                id("do_not_find_me" + rand.nextInt()).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId()).
                price(new BigDecimal(2));
        Subscription subscriptionWithoutTrial = gateway.subscription().create(request2).getTarget();

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                inTrialPeriod().is(true);

        ResourceCollection<Subscription> subscriptionsWithTrialPeriods = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(subscriptionsWithTrialPeriods, subscriptionWithTrial));
        assertFalse(TestHelper.includesSubscription(subscriptionsWithTrialPeriods, subscriptionWithoutTrial));

        search = new SubscriptionSearchRequest().
                inTrialPeriod().is(false);

        ResourceCollection<Subscription> subscriptionsWithoutTrialPeriods = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(subscriptionsWithoutTrialPeriods, subscriptionWithoutTrial));
        assertFalse(TestHelper.includesSubscription(subscriptionsWithoutTrialPeriods, subscriptionWithTrial));
    }

    @Test
    public void searchOnMerchantAccountIdIs() {
        SubscriptionRequest request1 = new SubscriptionRequest().
                merchantAccountId(DEFAULT_MERCHANT_ACCOUNT_ID).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(3));
        Subscription subscriptionDefaultMerchantAccount = gateway.subscription().create(request1).getTarget();

        SubscriptionRequest request2 = new SubscriptionRequest().
                merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(3));
        Subscription subscriptionNonDefaultMerchantAccount = gateway.subscription().create(request2).getTarget();

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                merchantAccountId().is(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
                price().is(new BigDecimal(3));

        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(results, subscriptionNonDefaultMerchantAccount));
        assertFalse(TestHelper.includesSubscription(results, subscriptionDefaultMerchantAccount));
    }

    @Test
    public void searchOnBogusMerchantAccountIdIs() {
        SubscriptionRequest request1 = new SubscriptionRequest().
                merchantAccountId(DEFAULT_MERCHANT_ACCOUNT_ID).
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(5));
        Subscription subscription = gateway.subscription().create(request1).getTarget();

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                merchantAccountId().is(subscription.getMerchantAccountId()).
                price().is(new BigDecimal(5));

        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(results, subscription));

        search = new SubscriptionSearchRequest().
                merchantAccountId().in(subscription.getMerchantAccountId(), "totally_bogus").
                price().is(new BigDecimal(5));

        results = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(results, subscription));

        search = new SubscriptionSearchRequest().
                merchantAccountId().is("totally_bogus").
                price().is(new BigDecimal(5));

        results = gateway.subscription().search(search);
        assertFalse(TestHelper.includesSubscription(results, subscription));
    }

    @Test
    public void searchOnNextBillingDate() {
        SubscriptionRequest request1 = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId());

        SubscriptionRequest request2 = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId());

        Subscription trialSubscription = gateway.subscription().create(request1).getTarget();
        Subscription triallessSubscription = gateway.subscription().create(request2).getTarget();

        Calendar expectedNextBillingDate = Calendar.getInstance();
        expectedNextBillingDate.add(Calendar.DAY_OF_MONTH, 5);

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                nextBillingDate().greaterThanOrEqualTo(expectedNextBillingDate);
        ResourceCollection<Subscription> results = gateway.subscription().search(search);

        assertTrue(TestHelper.includesSubscription(results, triallessSubscription));
        assertFalse(TestHelper.includesSubscription(results, trialSubscription));
    }

    @Test
    public void searchOnPlanIdIs() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
        Plan triallessPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnPlanIdIsNot() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
        Plan triallessPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription2));
        assertFalse(TestHelper.includesSubscription(results, subscription1));
    }

    @Test
    public void searchOnPlanIdEndsWith() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
        Plan triallessPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnPlanIdStartsWith() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
        Plan triallessPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnPlanIdContains() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
        Plan triallessPlan = PlanFixture.PLAN_WITHOUT_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnPlanIdIn() {
        SubscriptionRequest request1 = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITH_TRIAL.getId()).
                price(new BigDecimal(6));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();

        SubscriptionRequest request2 = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId()).
                price(new BigDecimal(6));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();

        SubscriptionRequest request3 = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.ADD_ON_DISCOUNT_PLAN.getId()).
                price(new BigDecimal(6));
        Subscription subscription3 = gateway.subscription().create(request3).getTarget();

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                planId().in(PlanFixture.PLAN_WITH_TRIAL.getId(), PlanFixture.PLAN_WITHOUT_TRIAL.getId()).
                price().is(new BigDecimal(6));

        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertTrue(TestHelper.includesSubscription(results, subscription2));
        assertFalse(TestHelper.includesSubscription(results, subscription3));
    }

    @Test
    public void searchOnStatusIn() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnStatusExpired() {
        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                status().in(Status.EXPIRED);
        ResourceCollection<Subscription> results = gateway.subscription().search(search);

        assertTrue(results.getMaximumSize() > 0);
        for (Subscription subscription : results) {
            assertEquals(Status.EXPIRED, subscription.getStatus());
        }
    }

    @Test
    public void searchOnStatusInWithMultipleStatusesAsList() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertTrue(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnStatusInWithMultipleStatuses() {
        Plan trialPlan = PlanFixture.PLAN_WITH_TRIAL;
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
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertTrue(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void searchOnTransactionId() {
        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest request1 = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                price(new BigDecimal(14));
        Subscription subscription1 = gateway.subscription().create(request1).getTarget();

        SubscriptionRequest request2 = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(plan.getId()).
                price(new BigDecimal(14));
        Subscription subscription2 = gateway.subscription().create(request2).getTarget();

        SubscriptionSearchRequest search = new SubscriptionSearchRequest().
                transactionId().is(subscription1.getTransactions().get(0).getId());

        ResourceCollection<Subscription> results = gateway.subscription().search(search);
        assertTrue(TestHelper.includesSubscription(results, subscription1));
        assertFalse(TestHelper.includesSubscription(results, subscription2));
    }

    @Test
    public void unrecognizedStatus() {
        String xml = "<subscription><status>foobar</status></subscription>";
        Subscription transaction = new Subscription(NodeWrapperFactory.instance.create(xml));
        assertEquals(Subscription.Status.UNRECOGNIZED, transaction.getStatus());
    }

    @Test
    public void unrecognizedDurationUnit() {
        String xml = "<subscription><trial-duration-unit>foobar</trial-duration-unit></subscription>";
        Subscription transaction = new Subscription(NodeWrapperFactory.instance.create(xml));
        assertEquals(Subscription.DurationUnit.UNRECOGNIZED, transaction.getTrialDurationUnit());
    }

    @Test
    public void retryChargeWithAmount() {
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId());

        Subscription subscription = gateway.subscription().create(request).getTarget();

        makePastDue(subscription, 1);

        Result<Transaction> result = gateway.subscription().retryCharge(subscription.getId(), TransactionAmount.AUTHORIZE.amount);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();
        assertEquals(TransactionAmount.AUTHORIZE.amount, transaction.getAmount());
        assertNotNull(transaction.getProcessorAuthorizationCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getUpdatedAt().get(Calendar.YEAR));
    }

    @Test
    public void retryChargeWithoutAmount() {
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId());

        Subscription subscription = gateway.subscription().create(request).getTarget();
        makePastDue(subscription, 1);

        Result<Transaction> result = gateway.subscription().retryCharge(subscription.getId());
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();
        assertEquals(subscription.getPrice(), transaction.getAmount());
        assertNotNull(transaction.getProcessorAuthorizationCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getUpdatedAt().get(Calendar.YEAR));
    }

    @Test
    public void pastDueSubscriptionReportsCorrectStatus() {
        SubscriptionRequest request = new SubscriptionRequest().
                paymentMethodToken(creditCard.getToken()).
                planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId());

        Subscription subscription = gateway.subscription().create(request).getTarget();

        makePastDue(subscription, 1);

        Subscription foundSubscription = gateway.subscription().find(subscription.getId());
        assertEquals(Status.PAST_DUE, foundSubscription.getStatus());
    }

    private void makePastDue(Subscription subscription, int numberOfDaysPastDue) {
        NodeWrapper response = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.DEVELOPMENT.certificateFilenames, BraintreeGateway.VERSION).put("/subscriptions/" + subscription.getId() + "/make_past_due?days_past_due=" + numberOfDaysPastDue);
        assertTrue(response.isSuccess());
    }
}
