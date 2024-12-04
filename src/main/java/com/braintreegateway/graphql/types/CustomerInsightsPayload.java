package com.braintreegateway.graphql.types;

import com.braintreegateway.graphql.unions.CustomerInsights;

public class CustomerInsightsPayload {
  private final Boolean isInPayPalNetwork;
  private final CustomerInsights insights;

  public CustomerInsightsPayload(Boolean isInPayPalNetwork, CustomerInsights insights) {
    this.isInPayPalNetwork = isInPayPalNetwork;
    this.insights = insights;
  }

  public Boolean isInPayPalNetwork() {
    return isInPayPalNetwork;
  }

  public CustomerInsights getInsights() {
    return insights;
  }
}
