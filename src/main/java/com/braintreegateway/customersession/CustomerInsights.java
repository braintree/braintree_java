package com.braintreegateway.customersession;

import java.util.List;

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
