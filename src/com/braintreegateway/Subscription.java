package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class Subscription {

    public enum DurationUnit {
        DAY, MONTH, UNRECOGNIZED
    }

    public enum Status {
        ACTIVE("Active"),
        CANCELED("Canceled"),
        EXPIRED("Expired"),
        PAST_DUE("Past Due"),
        PENDING("Pending"),
        UNRECOGNIZED("Unrecognized");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private ArrayList<AddOn> addOns;
    private BigDecimal balance;
    private Integer billingDayOfMonth;
    private Calendar billingPeriodEndDate;
    private Calendar billingPeriodStartDate;
    private Integer daysPastDue;
    private ArrayList<Discount> discounts;
    private Integer failureCount;
    private Calendar firstBillingDate;
    private Boolean hasTrialPeriod;
    private String id;
    private String merchantAccountId;
    private boolean neverExpires;
    private BigDecimal nextBillAmount;
    private Calendar nextBillingDate;
    private Integer numberOfBillingCycles;
    private Calendar paidThroughDate;
    private String paymentMethodToken;
    private String planId;
    private BigDecimal price;
    private Status status;
    private List<Transaction> transactions;
    private Integer trialDuration;
    private DurationUnit trialDurationUnit;

    public Subscription(NodeWrapper node) {
        addOns = new ArrayList<AddOn>();
        for (NodeWrapper addOnResponse : node.findAll("add-ons/add-on")) {
            addOns.add(new AddOn(addOnResponse));
        }
        balance = node.findBigDecimal("balance");
        billingDayOfMonth = node.findInteger("billing-day-of-month");
        billingPeriodEndDate = node.findDate("billing-period-end-date");
        billingPeriodStartDate = node.findDate("billing-period-start-date");
        daysPastDue = node.findInteger("days-past-due");
        discounts = new ArrayList<Discount>();
        for (NodeWrapper discountResponse : node.findAll("discounts/discount")) {
            discounts.add(new Discount(discountResponse));
        }
        failureCount = node.findInteger("failure-count");
        firstBillingDate = node.findDate("first-billing-date");
        id = node.findString("id");
        merchantAccountId = node.findString("merchant-account-id");
        neverExpires = node.findBoolean("never-expires");
        nextBillAmount = node.findBigDecimal("next-bill-amount");
        nextBillingDate = node.findDate("next-billing-date");
        numberOfBillingCycles = node.findInteger("number-of-billing-cycles");
        paidThroughDate = node.findDate("paid-through-date");
        paymentMethodToken = node.findString("payment-method-token");
        planId = node.findString("plan-id");
        price = node.findBigDecimal("price");
        status = EnumUtils.findByName(Status.class, node.findString("status"));
        hasTrialPeriod = node.findBoolean("trial-period");
        trialDuration = node.findInteger("trial-duration");
        trialDurationUnit = EnumUtils.findByName(DurationUnit.class, node.findString("trial-duration-unit"));
        transactions = new ArrayList<Transaction>();
        for (NodeWrapper transactionResponse : node.findAll("transactions/transaction")) {
            transactions.add(new Transaction(transactionResponse));
        }
    }

    public List<AddOn> getAddOns() {
        return addOns;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    
    public Integer getBillingDayOfMonth() {
        return billingDayOfMonth;
    }

    public Calendar getBillingPeriodEndDate() {
        return billingPeriodEndDate;
    }

    public Calendar getBillingPeriodStartDate() {
        return billingPeriodStartDate;
    }

    public Integer getDaysPastDue() {
        return daysPastDue;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public Calendar getFirstBillingDate() {
        return firstBillingDate;
    }

    public String getId() {
        return id;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public BigDecimal getNextBillAmount() {
        return nextBillAmount;
    }

    public Calendar getNextBillingDate() {
        return nextBillingDate;
    }

    public Integer getNumberOfBillingCycles() {
        return numberOfBillingCycles;
    }
    
    public Calendar getPaidThroughDate() {
        return paidThroughDate;
    }

    public String getPaymentMethodToken() {
        return paymentMethodToken;
    }

    public String getPlanId() {
        return planId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Status getStatus() {
        return status;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Integer getTrialDuration() {
        return trialDuration;
    }

    public DurationUnit getTrialDurationUnit() {
        return trialDurationUnit;
    }

    public Boolean hasTrialPeriod() {
        return hasTrialPeriod;
    }

    public boolean neverExpires() {
        return neverExpires;
    }
}
