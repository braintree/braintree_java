package com.braintreegateway.graphql.types;

import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.util.Experimental;

/**
 * Represents the payment method and priority associated with a PayPal customer session.
 */
@Experimental("This class is experimental and may change in future releases.")
public class PaymentRecommendation {

  private final RecommendedPaymentOption paymentOption;
  private final Integer recommendedPriority;

  public PaymentRecommendation(RecommendedPaymentOption paymentOption, Integer recommendedPriority) {
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
