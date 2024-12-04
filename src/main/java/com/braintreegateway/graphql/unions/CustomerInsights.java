package com.braintreegateway.graphql.unions;

import java.util.List;

import com.braintreegateway.graphql.types.PaymentRecommendation;

public class CustomerInsights {

  private final List<PaymentRecommendation> paymentRecommendations;

  public CustomerInsights(
      java.util.List<PaymentRecommendation> paymentRecommendations) {
    this.paymentRecommendations = paymentRecommendations;
  }

  public List<PaymentRecommendation> getPaymentRecommendations() {
    return paymentRecommendations;
  }
}
