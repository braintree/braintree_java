package com.braintreegateway.integrationtest;

import com.braintreegateway.Plan;
import com.braintreegateway.Request;
import com.braintreegateway.RequestBuilder;

import java.math.BigDecimal;

public class PlanRequest extends Request {
    private String id;
    private String merchantId;
    private Integer billingDayOfMonth;
    private Integer billingFrequency;
    private String currencyIsoCode;
    private String description;
    private String name;
    private Integer numberOfBillingCycles;
    private BigDecimal price;
    private Boolean hasTrialPeriod;
    private Integer trialDuration;
    private Plan.DurationUnit trialDurationUnit;

    public PlanRequest id(String id) {
        this.id = id;
        return this;
    }

    public PlanRequest billingFrequency(int billingFrequency) {
        this.billingFrequency = billingFrequency;
        return this;
    }

    public PlanRequest description(String description) {
        this.description = description;
        return this;
    }

    public PlanRequest numberOfBillingCycles(int numberOfBillingCycles) {
        this.numberOfBillingCycles = numberOfBillingCycles;
        return this;
    }

    public PlanRequest price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public PlanRequest trialPeriod(boolean trialPeriod) {
        this.hasTrialPeriod = trialPeriod;
        return this;
    }

    public PlanRequest trialDuration(int trialDuration) {
        this.trialDuration = trialDuration;
        return this;
    }

    public PlanRequest trialDurationUnit(Plan.DurationUnit trialDurationUnit) {
        this.trialDurationUnit = trialDurationUnit;
        return this;
    }

    public PlanRequest merchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public PlanRequest billingDayOfMonth(int billingDayOfMonth) {
        this.billingDayOfMonth = billingDayOfMonth;
        return this;
    }

    public PlanRequest currencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
        return this;
    }

    public PlanRequest name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("plan").toXML();
    }

    private RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("id", id).
            addElement("merchantId", merchantId).
            addElement("billingDayOfMonth", billingDayOfMonth).
            addElement("billingFrequency", billingFrequency).
            addElement("currencyIsoCode", currencyIsoCode).
            addElement("description", description).
            addElement("name", name).
            addElement("numberOfBillingCycles", numberOfBillingCycles).
            addElement("price", price).
            addElement("trialPeriod", hasTrialPeriod).
            addElement("trialDuration", trialDuration);

        if (trialDurationUnit != null) {
            builder.addElement("trialDurationUnit", trialDurationUnit.toString().toLowerCase());
        }

        return builder;
    }

}
