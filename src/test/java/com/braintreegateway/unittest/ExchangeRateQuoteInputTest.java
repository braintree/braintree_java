package com.braintreegateway.unittest;
import com.braintreegateway.ExchangeRateQuoteInput;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateQuoteInputTest {

  @Test
  public void testToGraphQLVariables() {
    ExchangeRateQuoteInput input = new ExchangeRateQuoteInput(null)
        .baseCurrency("USD")
        .quoteCurrency("EUR")
        .baseAmount("10.15")
        .markup("5.00");
    Map<String, Object> map = input.toGraphQLVariables();
    assertEquals(map.get("baseCurrency"), "USD");
    assertEquals(map.get("quoteCurrency"), "EUR");
    assertEquals(map.get("baseAmount"), "10.15");
    assertEquals(map.get("markup"), "5.00");
  }

  @Test
  public void testToGraphQLVariablesWithoutMarkupAndBaseAmount() {
    ExchangeRateQuoteInput input = new ExchangeRateQuoteInput(null)
        .baseCurrency("USD")
        .quoteCurrency("CAD");
    Map<String, Object> map = input.toGraphQLVariables();
    assertEquals(map.get("baseCurrency"), "USD");
    assertEquals(map.get("quoteCurrency"), "CAD");
    assertEquals(map.get("baseAmount"), null);
    assertEquals(map.get("markup"), null);
  }

  @Test
  public void testToGraphQLVariablesWithAllEmptyFields() {
    ExchangeRateQuoteInput input = new ExchangeRateQuoteInput(null);
    Map<String, Object> map = input.toGraphQLVariables();
    assertEquals(map.get("baseCurrency"), null);
    assertEquals(map.get("quoteCurrency"), null);
    assertEquals(map.get("baseAmount"), null);
    assertEquals(map.get("markup"), null);
  }
}
