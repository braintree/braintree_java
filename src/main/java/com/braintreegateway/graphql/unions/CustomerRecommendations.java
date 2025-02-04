package com.braintreegateway.graphql.unions;

import java.util.List;

import com.braintreegateway.graphql.types.PaymentOptions;

/**
 * A union of all possible customer recommendations associated with a PayPal customer session.
 */
public class CustomerRecommendations {

  private final List<PaymentOptions> paymentOptions;

  public CustomerRecommendations(
      List<PaymentOptions> paymentOptions) {
    this.paymentOptions = paymentOptions;
  }

  /**
   * 
   * @return A set of payment recommendations.
   */
  public List<PaymentOptions> getPaymentOptions() {
    return paymentOptions;
  }
}
