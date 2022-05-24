package com.braintreegateway.unittest;
import com.braintreegateway.ExchangeRateQuoteRequest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeRateQuoteRequestTest {
  @Test
  public void testToGraphQLVariables() {
    ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
        .addExchangeRateQuoteInput()
          .baseCurrency("USD")
          .quoteCurrency("EUR")
          .baseAmount("5.00")
          .markup("3.00")
          .done()
        .addExchangeRateQuoteInput()
          .baseCurrency("EUR")
          .quoteCurrency("CAD")
          .baseAmount("15.00")
          .markup("2.64")
          .done();

    Map<String, Object> map = request.toGraphQLVariables();
    Map<String, Object> requestMap = (Map<String, Object>)map.get("exchangeRateQuoteRequest");
    assertNotNull(requestMap);
    Object[] quotes = (Object[])requestMap.get("quotes");
    assertNotNull(quotes);
    assertEquals(2, quotes.length);
    Map<String, Object> firstQuote = (Map<String, Object>)quotes[0];
    Map<String, Object> secondQuote = (Map<String, Object>)quotes[1];
    assertEquals("USD", firstQuote.get("baseCurrency"));
    assertEquals("EUR", firstQuote.get("quoteCurrency"));
    assertEquals("5.00", firstQuote.get("baseAmount"));
    assertEquals("3.00", firstQuote.get("markup"));
    assertEquals("EUR", secondQuote.get("baseCurrency"));
    assertEquals("CAD", secondQuote.get("quoteCurrency"));
    assertEquals("15.00", secondQuote.get("baseAmount"));
    assertEquals("2.64", secondQuote.get("markup"));
  }

  @Test
  public void testToGraphQLVariablesWithMissingFields() {
    ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
      .addExchangeRateQuoteInput()
        .baseCurrency("USD")
        .quoteCurrency("EUR")
        .baseAmount("5.00")
        .done()
      .addExchangeRateQuoteInput()
        .baseCurrency("EUR")
        .quoteCurrency("CAD")
        .done();

    Map<String, Object> map = request.toGraphQLVariables();
    Map<String, Object> requestMap = (Map<String, Object>)map.get("exchangeRateQuoteRequest");
    assertNotNull(requestMap);
    Object[] quotes = (Object[])requestMap.get("quotes");
    assertNotNull(quotes);
    assertEquals(2, quotes.length);
    Map<String, Object> firstQuote = (Map<String, Object>)quotes[0];
    Map<String, Object> secondQuote = (Map<String, Object>)quotes[1];
    assertEquals("USD", firstQuote.get("baseCurrency"));
    assertEquals("EUR", firstQuote.get("quoteCurrency"));
    assertEquals("5.00", firstQuote.get("baseAmount"));
    assertEquals(null, firstQuote.get("markup"));
    assertEquals("EUR", secondQuote.get("baseCurrency"));
    assertEquals("CAD", secondQuote.get("quoteCurrency"));
    assertEquals(null, secondQuote.get("baseAmount"));
    assertEquals(null, secondQuote.get("markup"));
  }
}
