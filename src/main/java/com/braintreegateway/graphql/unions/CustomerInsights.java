package com.braintreegateway.graphql.unions;

import java.util.List;

import com.braintreegateway.graphql.types.PaymentRecommendation;

public class CustomerInsights {

  private List<PaymentRecommendation> paymentRecommendations;

  public CustomerInsights paymentRecommendations(
      List<PaymentRecommendation> paymentRecommendations) {
    this.paymentRecommendations = paymentRecommendations;
    return this;
  }

  public List<PaymentRecommendation> getPaymentRecommendations() {
    return paymentRecommendations;
  }
}
