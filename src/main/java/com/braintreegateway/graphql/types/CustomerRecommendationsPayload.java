package com.braintreegateway.graphql.types;

import com.braintreegateway.graphql.unions.CustomerRecommendations;

/**
 * Represents the customer recommendations associated with a PayPal customer session.
 */
public class CustomerRecommendationsPayload {
  private final Boolean isInPayPalNetwork;
  private final CustomerRecommendations recommendations;

  public CustomerRecommendationsPayload(Boolean isInPayPalNetwork, CustomerRecommendations recommendations) {
    this.isInPayPalNetwork = isInPayPalNetwork;
    this.recommendations = recommendations;
  }

  /**
   * 
   * @return Flag to indicate whether customer is in paypal network.
   */
  public Boolean isInPayPalNetwork() {
    return isInPayPalNetwork;
  }

  /**
   * 
   * @return Customer recommendations information.
   */
  public CustomerRecommendations getRecommendations() {
    return recommendations;
  }
}
