package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class CustomActionsPaymentMethodField {

  private String displayValue;
  private String name;

  public CustomActionsPaymentMethodField(NodeWrapper node) {
    name = node.findString("name");
    displayValue = node.findString("display-value");
  }

  public String getName() {
    return name;
  }

  public String getDisplayValue() {
    return displayValue;
  }
}
