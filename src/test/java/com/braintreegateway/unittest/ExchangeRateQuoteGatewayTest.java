package com.braintreegateway.unittest;

import com.braintreegateway.ExchangeRateQuote;
import com.braintreegateway.ExchangeRateQuoteGateway;
import com.braintreegateway.ExchangeRateQuotePayload;
import com.braintreegateway.ExchangeRateQuoteRequest;
import com.braintreegateway.Request;
import com.braintreegateway.Result;
import com.braintreegateway.util.GraphQLClient;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.jr.ob.JSON;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ExchangeRateQuoteGatewayTest {

  @Test
  public void testGenerateSuccess() {
    ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
        .addExchangeRateQuoteInput()
          .baseCurrency("USD")
          .quoteCurrency("EUR")
          .baseAmount("12.19")
          .markup("1.89")
          .done();

    String rawResponse = "{\n"
        + "      \"data\": {\n"
        + "      \"generateExchangeRateQuote\": {\n"
        + "        \"quotes\": [\n"
        + "        {\n"
        + "          \"id\": \"ZXhjaGFuZ2VyYXRlcXVvdGVfMDEyM0FCQw\",\n"
        + "          \"baseAmount\": {\n"
        + "              \"value\": \"12.19\",\n"
        + "              \"currencyCode\": \"USD\"\n"
        + "          },\n"
        + "          \"quoteAmount\": {\n"
        + "            \"value\": \"12.16\",\n"
        + "            \"currencyCode\": \"EUR\"\n"
        + "          },\n"
        + "          \"exchangeRate\": \"0.997316360864\",\n"
        + "            \"expiresAt\": \"2021-06-16T02:00:00.000000Z\",\n"
        + "            \"refreshesAt\": \"2021-06-16T00:00:00.000000Z\"\n"
        + "        }\n"
        + "      ]\n"
        + "      }\n"
        + "    },\n"
        + "      \"extensions\": {\n"
        + "      \"requestId\": \"5ef2e69a-fb0e-4d71-82a3-ea59722ac64d\"\n"
        + "    }\n"
        + "  }";

    Map<String, Object> response = null;
    try {
      response = JSON.std.mapFrom(rawResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
    GraphQLClient graphQLClient = Mockito.mock(GraphQLClient.class);
    when(graphQLClient.query(anyString(), any(Request.class))).thenReturn(response);

    ExchangeRateQuoteGateway exchangeRateQuoteGateway = new ExchangeRateQuoteGateway(null,
        graphQLClient, null);
    Result<ExchangeRateQuotePayload> result = exchangeRateQuoteGateway.generate(request);
    List<ExchangeRateQuote> quotes = result.getTarget().getQuotes();
    assertNotNull(quotes);
    assertEquals(1, quotes.size());

    ExchangeRateQuote quote1 = quotes.get(0);
    assertEquals("12.19", String.valueOf(quote1.getBaseAmount().getValue()));
    assertEquals("USD", String.valueOf(quote1.getBaseAmount().getCurrencyCode()));
    assertEquals("12.16", String.valueOf(quote1.getQuoteAmount().getValue()));
    assertEquals("EUR", String.valueOf(quote1.getQuoteAmount().getCurrencyCode()));
    assertEquals("0.997316360864", String.valueOf(quote1.getExchangeRate()));
    assertEquals("2021-06-16T02:00:00.000000Z", String.valueOf(quote1.getExpiresAt()));
    assertEquals("2021-06-16T00:00:00.000000Z", String.valueOf(quote1.getRefreshesAt()));
    assertEquals("ZXhjaGFuZ2VyYXRlcXVvdGVfMDEyM0FCQw", String.valueOf(quote1.getId()));
  }

  @Test
  public void testGenerateError() {
    ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
        .addExchangeRateQuoteInput()
        .baseCurrency("USD")
        .done();

    String rawResponse = "{\n"
        + "  \"errors\": [\n"
        + "    {\n"
        + "      \"message\": \"Field 'quoteCurrency' of variable 'exchangeRateQuoteRequest' has coerced Null value for NonNull type 'CurrencyCodeAlpha!'\",\n"
        + "      \"locations\": [\n"
        + "        {\n"
        + "          \"line\": 1,\n"
        + "          \"column\": 11\n"
        + "        }\n"
        + "      ]\n"
        + "    }\n"
        + "  ],\n"
        + "  \"extensions\": {\n"
        + "    \"requestId\": \"96c023c9-0192-4008-8f28-25a7f8714bab\"\n"
        + "  }\n"
        + "}";

    Map<String, Object> response = null;
    try {
      response = JSON.std.mapFrom(rawResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
    GraphQLClient graphQLClient = Mockito.mock(GraphQLClient.class);
    when(graphQLClient.query(anyString(), any(Request.class))).thenReturn(response);

    ExchangeRateQuoteGateway exchangeRateQuoteGateway = new ExchangeRateQuoteGateway(null,
        graphQLClient, null);
    Result<ExchangeRateQuotePayload> result = exchangeRateQuoteGateway.generate(request);
    assertTrue(result.getErrors().getAllValidationErrors().get(0).getMessage().contains("'quoteCurrency'"));
  }

}
