package com.braintreegateway;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Collections;

import com.braintreegateway.util.NodeWrapper;

public class CustomActionsPaymentMethod implements PaymentMethod {

  private Calendar createdAt;
  private String customerId;
  private String globalId;
  private boolean isDefault;
  private String imageUrl;
  private String uniqueNumberIdentifier;
  private List<Subscription> subscriptions;
  private String token;
  private Calendar updatedAt;
  private String actionName;
  private List<CustomActionsPaymentMethodField> fields;

  public CustomActionsPaymentMethod(NodeWrapper node) {
    token = node.findString("token");
    globalId = node.findString("global-id");
    createdAt = node.findDateTime("created-at");
    updatedAt = node.findDateTime("updated-at");
    customerId = node.findString("customer-id");
    imageUrl = node.findString("image-url");
    isDefault = node.findBoolean("default");
    uniqueNumberIdentifier = node.findString("unique-number-identifier");
    actionName = node.findString("action-name");
    fields = new ArrayList<CustomActionsPaymentMethodField>();
    for (NodeWrapper fieldResponse : node.findAll("fields/field")) {
      fields.add(new CustomActionsPaymentMethodField(fieldResponse));
    }
    subscriptions = new ArrayList<Subscription>();
    for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
      subscriptions.add(new Subscription(subscriptionResponse));
    }
  }

  public String getGlobalId() {
    return globalId;
  }

  public Calendar getCreatedAt() {
    return createdAt;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getUniqueNumberIdentifier() {
    return uniqueNumberIdentifier;
  }

  public List<Subscription> getSubscriptions() {
    return subscriptions;
  }

  public String getToken() {
    return token;
  }

  public Calendar getUpdatedAt() {
    return updatedAt;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public List<CustomActionsPaymentMethodField> getFields() {
    return Collections.unmodifiableList(fields);
  }

  public String getActionName() {
    return actionName;
  }
}
