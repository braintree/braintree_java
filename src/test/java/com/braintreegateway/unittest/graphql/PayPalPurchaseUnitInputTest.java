package com.braintreegateway.unittest.graphql;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.PayPalPurchaseUnitInput;
import com.braintreegateway.graphql.inputs.PayPalPayeeInput; 
import com.braintreegateway.MonetaryAmountInput;

class PayPalPurchaseUnitInputTest {

  @Test
  void testToGraphQLVariables() {

    MonetaryAmountInput amount1 = new MonetaryAmountInput();
    amount1.setValue(new BigDecimal("100.00")); 
    amount1.setCurrencyCode("USD");

    PayPalPayeeInput payee =  
       PayPalPayeeInput.builder()
            .clientId("123")
            .emailAddress("test@example.com")
            .build();

    PayPalPurchaseUnitInput input = 
      PayPalPurchaseUnitInput.builder(amount1)
            .payee(payee)
            .build();

    Map<String, Object> map = input.toGraphQLVariables();

    assertTrue(map.containsKey("amount"));
    assertTrue(map.containsKey("payee"));  
  }

  @Test
  void testBuildPurchaseUnitWithoutPayee() {

    MonetaryAmountInput amount1 = new MonetaryAmountInput();
    amount1.setValue(new BigDecimal("100.00")); 
    amount1.setCurrencyCode("USD");

    PayPalPurchaseUnitInput input = 
      PayPalPurchaseUnitInput.builder(amount1)
            .build();

    Map<String, Object> map = input.toGraphQLVariables();

    assertTrue(map.containsKey("amount"));
  }
}
