package com.braintreegateway.graphql.unions;

import java.util.List;

import com.braintreegateway.graphql.types.PaymentRecommendation;

/**
 * A union of all possible customer insights associated with a PayPal customer session.
 *
 * See our {@link https://graphql.braintreepayments.com/reference/#union-customerinsights graphql reference docs} for information on attributes.
 */
public class CustomerInsights {

  private final List<PaymentRecommendation> paymentRecommendations;

  public CustomerInsights(
      java.util.List<PaymentRecommendation> paymentRecommendations) {
    this.paymentRecommendations = paymentRecommendations;
  }

  /**
   * 
   * @return A set of payment recommendations.
   */
  public List<PaymentRecommendation> getPaymentRecommendations() {
    return paymentRecommendations;
  }
}
