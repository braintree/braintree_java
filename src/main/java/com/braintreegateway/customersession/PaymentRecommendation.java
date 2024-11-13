package com.braintreegateway.customersession;


public class PaymentRecommendation {

    private InsightPaymentOption paymentOption;
    private Integer recommendedPriority;

    public PaymentRecommendation paymentOption(InsightPaymentOption paymentOption) {
        this.paymentOption = paymentOption;
        return this;
    }

    public PaymentRecommendation recommendedPriority(Integer recommendedPriority) {
        this.recommendedPriority = recommendedPriority;
        return this;
    }

    public InsightPaymentOption getPaymentOption() {
        return paymentOption;
    }

    public Integer getRecommendedPriority() {
        return recommendedPriority;
    }
}
