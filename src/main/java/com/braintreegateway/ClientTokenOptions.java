package com.braintreegateway;

public class ClientTokenOptions {
  private Boolean makeDefault;
  private Boolean verifyCard;
  private Boolean failOnDuplicatePaymentMethod;
  private String customerId;

  public ClientTokenOptions() {}

  public ClientTokenOptions verifyCard(Boolean verifyCard) {
    this.verifyCard = verifyCard;
    return this;
  }

  public ClientTokenOptions customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public ClientTokenOptions makeDefault(Boolean makeDefault) {
    this.makeDefault = makeDefault;
    return this;
  }

  public ClientTokenOptions failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
    this.failOnDuplicatePaymentMethod = failOnDuplicatePaymentMethod;
    return this;
  }

  public String getCustomerId() {
    return customerId;
  }

  public Boolean getFailOnDuplicatePaymentMethod () {
    return failOnDuplicatePaymentMethod;
  }

  public Boolean getVerifyCard () {
    return verifyCard;
  }

  public Boolean getMakeDefault() {
    return makeDefault;
  }
}
