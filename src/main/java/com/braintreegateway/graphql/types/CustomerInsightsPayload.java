package com.braintreegateway.graphql.types;

import com.braintreegateway.graphql.unions.CustomerInsights;

/**
 * Represents the customer insights information associated with a PayPal
 * customer session.
 *
 * See our
 * {@link https://graphql.braintreepayments.com/reference/#object--customerinsightspayload
 * graphql reference docs} for information on attributes.
 */
public class CustomerInsightsPayload {
  private final Boolean isInPayPalNetwork;
  private final CustomerInsights insights;

  public CustomerInsightsPayload(Boolean isInPayPalNetwork, CustomerInsights insights) {
    this.isInPayPalNetwork = isInPayPalNetwork;
    this.insights = insights;
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
   * @return Customer insights information.
   */
  public CustomerInsights getInsights() {
    return insights;
  }
}
