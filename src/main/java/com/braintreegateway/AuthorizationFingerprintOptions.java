package com.braintreegateway;

public class AuthorizationFingerprintOptions {
  private Boolean makeDefault;
  private Boolean verifyCard;
  private Boolean failOnDuplicatePaymentMethod;
  private String customerId;

  public AuthorizationFingerprintOptions() {}

  public AuthorizationFingerprintOptions verifyCard(Boolean verifyCard) {
    this.verifyCard = verifyCard;
    return this;
  }

  public AuthorizationFingerprintOptions customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public AuthorizationFingerprintOptions makeDefault(Boolean makeDefault) {
    this.makeDefault = makeDefault;
    return this;
  }

  public AuthorizationFingerprintOptions failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
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
