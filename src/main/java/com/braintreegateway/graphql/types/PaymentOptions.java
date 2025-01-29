package com.braintreegateway.graphql.types;

import com.braintreegateway.graphql.enums.RecommendedPaymentOption;

/**
 * Represents the payment method and priority associated with a PayPal customer session.
 */
public class PaymentOptions {

  private final RecommendedPaymentOption paymentOption;
  private final Integer recommendedPriority;

  public PaymentOptions(RecommendedPaymentOption paymentOption, Integer recommendedPriority) {
    this.paymentOption = paymentOption;
    this.recommendedPriority = recommendedPriority;
  }

  /**
   * 
   * @return The payment option type.
   */
  public RecommendedPaymentOption getPaymentOption() {
    return paymentOption;
  }

  /**
   * 
   * @return Recommended priority of the payment option.
   */
  public Integer getRecommendedPriority() {
    return recommendedPriority;
  }
}
