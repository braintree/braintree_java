package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public class SubscriptionDetails {

    private Calendar billingPeriodEndDate;
    private Calendar billingPeriodStartDate;

    public SubscriptionDetails(NodeWrapper node) {
        billingPeriodEndDate = node.findDate("billing-period-end-date");
        billingPeriodStartDate = node.findDate("billing-period-start-date");
    }

    public Calendar getBillingPeriodEndDate() {
        return billingPeriodEndDate;
    }

    public Calendar getBillingPeriodStartDate() {
        return billingPeriodStartDate;
    }

}
