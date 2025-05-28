package com.braintreegateway.unittest.graphql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.graphql.inputs.PayPalPayeeInput; 
import com.braintreegateway.graphql.inputs.PayPalPurchaseUnitInput; 
import com.braintreegateway.MonetaryAmountInput;

class CreateCustomerSessionInputTest {
  @Test
  void testToGraphQLVariables() {
    CustomerSessionInput customerSessionInput = CustomerSessionInput.builder().build();

    MonetaryAmountInput amount1 = new MonetaryAmountInput();
    amount1.setValue(new BigDecimal("100.00")); 
    amount1.setCurrencyCode("USD");

    PayPalPayeeInput payee =  
       PayPalPayeeInput.builder()
            .clientId("123")
            .emailAddress("test@example.com")
            .build();

    PayPalPurchaseUnitInput purchaseUnit = 
      PayPalPurchaseUnitInput.builder(amount1)
            .payee(payee)
            .build();

    List<PayPalPurchaseUnitInput> purchaseUnits = new ArrayList<>();
    purchaseUnits.add(purchaseUnit); 


    CreateCustomerSessionInput input =
        CreateCustomerSessionInput.builder()
            .merchantAccountId("merchant-account-id")
            .sessionId("session-id")
            .customer(customerSessionInput)   
            .purchaseUnits(purchaseUnits)
            .domain("a-domain")
            .build();

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("merchant-account-id", map.get("merchantAccountId"));
    assertEquals("session-id", map.get("sessionId"));
    assertEquals(customerSessionInput.toGraphQLVariables(), map.get("customer"));
    assertEquals("a-domain", map.get("domain"));

    assertTrue(map.containsKey("purchaseUnits"));
  }
}
