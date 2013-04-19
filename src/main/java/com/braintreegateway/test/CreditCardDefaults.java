package com.braintreegateway.test;

public enum CreditCardDefaults {

  IssuingBank("NETWORK ONLY"),
  CountryOfIssuance("USA");

  private final String value;

  public String getValue() {
    return value;
  }

  private CreditCardDefaults(String value) {
    this.value = value;
  }
}
