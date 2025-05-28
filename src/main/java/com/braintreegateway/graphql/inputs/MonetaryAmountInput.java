package com.braintreegateway;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import com.braintreegateway.util.Experimental;

@Experimental("This class is experimental and may change in future releases.")
public class MonetaryAmountInput {

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

  public Map<String, Object> toGraphQLVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("value", value);
    if (currencyCode != null) {
      variables.put("currencyCode", currencyCode);
    }
    return variables;
  }

}
