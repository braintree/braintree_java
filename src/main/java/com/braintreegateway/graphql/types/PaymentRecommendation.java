package com.braintreegateway.graphql.types;

import com.braintreegateway.graphql.enums.InsightPaymentOption;

public class PaymentRecommendation {

  private final InsightPaymentOption paymentOption;
  private final Integer recommendedPriority;

  public PaymentRecommendation(InsightPaymentOption paymentOption, Integer recommendedPriority) {
    this.paymentOption = paymentOption;
    this.recommendedPriority = recommendedPriority;
  }

  public InsightPaymentOption getPaymentOption() {
    return paymentOption;
  }

  public Integer getRecommendedPriority() {
    return recommendedPriority;
  }
}
