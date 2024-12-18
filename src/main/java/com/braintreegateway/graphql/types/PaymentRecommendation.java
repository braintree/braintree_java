package com.braintreegateway.graphql.types;

import com.braintreegateway.graphql.enums.InsightPaymentOption;

/**
 * Represents the payment method and priority associated with a PayPal customer session.
 *
 * See our {@link https://graphql.braintreepayments.com/reference/#object--paymentrecommendation graphql reference docs} for information on attributes
 */
public class PaymentRecommendation {

  private final InsightPaymentOption paymentOption;
  private final Integer recommendedPriority;

  public PaymentRecommendation(InsightPaymentOption paymentOption, Integer recommendedPriority) {
    this.paymentOption = paymentOption;
    this.recommendedPriority = recommendedPriority;
  }

  /**
   * 
   * @return The payment option type.
   */
  public InsightPaymentOption getPaymentOption() {
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
