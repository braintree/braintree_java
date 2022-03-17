package com.braintreegateway;

import java.math.BigDecimal;

public class MonetaryAmount {

  private BigDecimal value;

  private String currencyCode;

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }
}
