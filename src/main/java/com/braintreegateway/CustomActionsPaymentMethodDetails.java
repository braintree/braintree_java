package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.braintreegateway.util.NodeWrapper;

public class CustomActionsPaymentMethodDetails {
  private String token;
  private String actionName;
  private String globalId;
  private String uniqueNumberIdentifier;
  private List<CustomActionsPaymentMethodField> fields;

  public CustomActionsPaymentMethodDetails(NodeWrapper node) {
    token = node.findString("token");
    globalId = node.findString("global-id");
    actionName = node.findString("action-name");
    uniqueNumberIdentifier = node.findString("unique-number-identifier");
    fields = new ArrayList<CustomActionsPaymentMethodField>();
    for (NodeWrapper fieldResponse : node.findAll("fields/field")) {
      fields.add(new CustomActionsPaymentMethodField(fieldResponse));
    }
  }

  public String getGlobalId() {
    return globalId;
  }

  public String getToken() {
    return token;
  }

  public String getActionName() {
    return actionName;
  }

  public String getUniqueNumberIdentifier() {
    return uniqueNumberIdentifier;
  }

  public List<CustomActionsPaymentMethodField> getFields() {
    return Collections.unmodifiableList(fields);
  }
}
