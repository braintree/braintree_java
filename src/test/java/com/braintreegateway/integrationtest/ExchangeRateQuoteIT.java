package com.braintreegateway.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.braintreegateway.ExchangeRateQuote;
import com.braintreegateway.ExchangeRateQuotePayload;
import com.braintreegateway.ExchangeRateQuoteRequest;
import com.braintreegateway.Result;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ExchangeRateQuoteIT extends IntegrationTest {

    @Test
    public void exchangeRateQuoteWithGraphQL() {

        ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
            .addExchangeRateQuoteInput()
                .baseCurrency("USD")
                .quoteCurrency("EUR")
                .baseAmount("12.19")
                .markup("12.14")
                .done()
            .addExchangeRateQuoteInput()
                .baseCurrency("EUR")
                .quoteCurrency("CAD")
                .baseAmount("15.16")
                .markup("2.64")
                .done();

        Result<ExchangeRateQuotePayload> result = gateway.exchangeRateQuote().generate(request);
        Assert.assertTrue(result.isSuccess());
        List<ExchangeRateQuote> quotes = result.getTarget().getQuotes();
        assertNotNull(quotes);
        assertEquals(2, quotes.size());

        ExchangeRateQuote quote1 = quotes.get(0);
        assertEquals("12.19", String.valueOf(quote1.getBaseAmount().getValue()));
        assertEquals("USD", String.valueOf(quote1.getBaseAmount().getCurrencyCode()));
        assertEquals("12.16", String.valueOf(quote1.getQuoteAmount().getValue()));
        assertEquals("EUR", String.valueOf(quote1.getQuoteAmount().getCurrencyCode()));
        assertEquals("0.997316360864", String.valueOf(quote1.getExchangeRate()));
        assertEquals("0.01", String.valueOf(quote1.getTradeRate()));
        assertEquals("2021-06-16T02:00:00.000000Z", String.valueOf(quote1.getExpiresAt()));
        assertEquals("2021-06-16T00:00:00.000000Z", String.valueOf(quote1.getRefreshesAt()));
        assertEquals("ZXhjaGFuZ2VyYXRlcXVvdGVfMDEyM0FCQw", String.valueOf(quote1.getId()));

        ExchangeRateQuote quote2 = quotes.get(1);
        assertEquals("15.16", String.valueOf(quote2.getBaseAmount().getValue()));
        assertEquals("EUR", String.valueOf(quote2.getBaseAmount().getCurrencyCode()));
        assertEquals("23.30", String.valueOf(quote2.getQuoteAmount().getValue()));
        assertEquals("CAD", String.valueOf(quote2.getQuoteAmount().getCurrencyCode()));
        assertEquals("1.536744692129366", String.valueOf(quote2.getExchangeRate()));
        assertNull(quote2.getTradeRate());
        assertEquals("2021-06-16T02:00:00.000000Z", String.valueOf(quote2.getExpiresAt()));
        assertEquals("2021-06-16T00:00:00.000000Z", String.valueOf(quote2.getRefreshesAt()));
        assertEquals("ZXhjaGFuZ2VyYXRlcXVvdGVfQUJDMDEyMw", String.valueOf(quote2.getId()));
    }

    @Test
    public void exchangeRateQuoteWithGraphQLQuoteCurrencyValidationError() {

        ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
            .addExchangeRateQuoteInput()
                .baseCurrency("USD")
                .baseAmount("12.19")
                .markup("12.14")
                .done()
            .addExchangeRateQuoteInput()
                .baseCurrency("EUR")
                .quoteCurrency("CAD")
                .baseAmount("15.16")
                .markup("2.64")
                .done();

        Result<ExchangeRateQuotePayload> result = gateway.exchangeRateQuote().generate(request);
        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(result.getErrors().getAllValidationErrors().get(0).getMessage().contains("'quoteCurrency'"));
    }

    @Test
    public void exchangeRateQuoteWithGraphQLBaseCurrencyValidationError() {

        ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
            .addExchangeRateQuoteInput()
                .baseCurrency("USD")
                .quoteCurrency("EUR")
                .baseAmount("12.19")
                .markup("12.14")
                .done()
            .addExchangeRateQuoteInput()
                .quoteCurrency("CAD")
                .baseAmount("15.16")
                .markup("2.64")
                .done();

        Result<ExchangeRateQuotePayload> result = gateway.exchangeRateQuote().generate(request);
        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(result.getErrors().getAllValidationErrors().get(0).getMessage().contains("'baseCurrency'"));
    }

    @Test
    public void exchangeRateQuoteWithGraphQLWithoutbaseAmount() {

        ExchangeRateQuoteRequest request = new ExchangeRateQuoteRequest()
            .addExchangeRateQuoteInput()
                .baseCurrency("USD")
                .quoteCurrency("EUR")
                .done()
            .addExchangeRateQuoteInput()
                .baseCurrency("EUR")
                .quoteCurrency("CAD")
                .done();

        Result<ExchangeRateQuotePayload> result = gateway.exchangeRateQuote().generate(request);
        Assert.assertTrue(result.isSuccess());
    }

}
