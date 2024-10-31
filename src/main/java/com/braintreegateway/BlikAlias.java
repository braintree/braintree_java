package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class BlikAlias {
  private String key;
  private String label;

  public BlikAlias(NodeWrapper node) {
    key = node.findString("key");
    label = node.findString("label");
  }

  public String getKey() {
    return key;
  }

  public String getLabel() {
    return label;
  }
}
