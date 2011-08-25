package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Plan {

    public enum DurationUnit {
        DAY, MONTH, UNRECOGNIZED
    }

    private String id;
    private List<AddOn> addOns;
    private String merchantId;
    private Integer billingDayOfMonth;
    private Integer billingFrequency;
    private String currencyIsoCode;
    private String description;
    private List<Discount> discounts;
    private String name;
    private Integer numberOfBillingCycles;
    private BigDecimal price;
    private Boolean trialPeriod;
    private Integer trialDuration;
    private DurationUnit trialDurationUnit;
    private Calendar createdAt;
    private Calendar updatedAt;

    public Plan(NodeWrapper node) {
        id = node.findString("id");
        addOns = new ArrayList<AddOn>();

        for (NodeWrapper addOnResponse : node.findAll("add-ons/modification")) {
            addOns.add(new AddOn(addOnResponse));
        }
        merchantId = node.findString("merchant-id");
        billingDayOfMonth = node.findInteger("billing-day-of-month");
        billingFrequency = node.findInteger("billing-frequency");
        createdAt = node.findDateTime("created-at");
        currencyIsoCode = node.findString("currency-iso-code");
        description = node.findString("description");
        discounts = new ArrayList<Discount>();
        for (NodeWrapper discountResponse : node.findAll("discounts/modification")) {
            discounts.add(new Discount(discountResponse));
        }
        name = node.findString("name");
        numberOfBillingCycles = node.findInteger("number-of-billing-cycles");
        price = node.findBigDecimal("price");
        trialPeriod = node.findBoolean("trial-period");
        trialDuration = node.findInteger("trial-duration");
        trialDurationUnit = EnumUtils.findByName(Plan.DurationUnit.class, node.findString("trial-duration-unit"));
        updatedAt = node.findDateTime("updated-at");
    }

    public List<AddOn> getAddOns() {
        return addOns;
    }

    public Integer getBillingFrequency() {
        return billingFrequency;
    }

    public String getDescription() {
        return description;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public String getId() {
        return id;
    }

    public Integer getNumberOfBillingCycles() {
        return numberOfBillingCycles;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean hasTrialPeriod() {
        return trialPeriod;
    }

    public Integer getTrialDuration() {
        return trialDuration;
    }

    public Plan.DurationUnit getTrialDurationUnit() {
        return trialDurationUnit;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public Integer getBillingDayOfMonth() {
        return billingDayOfMonth;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public String getName() {
        return name;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }
}
