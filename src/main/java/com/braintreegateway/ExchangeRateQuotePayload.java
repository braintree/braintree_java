package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExchangeRateQuotePayload {
    private List<ExchangeRateQuote> quotes;
    private String clientMutationId;

    public ExchangeRateQuotePayload quotes(List<ExchangeRateQuote> quotes) {
        this.quotes = quotes;
        return this;
    }

    public ExchangeRateQuotePayload clientMutationId(String clientMutationId) {
        this.clientMutationId = clientMutationId;
        return this;
    }

    public ExchangeRateQuotePayload() {
    }

    public ExchangeRateQuotePayload (Map<String, Object> data) {
        this.quotes = new ArrayList<>();
        this.clientMutationId = (String)data.get("clientMutationId");
        List<Map<String, Object>> quoteObjs = (List<Map<String, Object>>)data.get("quotes");

        for (Map<String, Object> quoteObj : quoteObjs) {
            Map<String, Object> baseAmountObj = (Map<String, Object>) quoteObj.get("baseAmount");
            Map<String, Object> quoteAmountObj = (Map<String, Object>) quoteObj.get("quoteAmount");
            MonetaryAmount baseAmount = new MonetaryAmount();
            baseAmount.setValue(new BigDecimal((String) baseAmountObj.get("value")));
            baseAmount.setCurrencyCode((String) baseAmountObj.get("currencyCode"));
            MonetaryAmount quoteAmount = new MonetaryAmount();
            quoteAmount.setValue(new BigDecimal((String) quoteAmountObj.get("value")));
            quoteAmount.setCurrencyCode((String) quoteAmountObj.get("currencyCode"));

            ExchangeRateQuote quote = new ExchangeRateQuote()
                .exchangeRate((String) quoteObj.get("exchangeRate"))
                .id((String) quoteObj.get("id"))
                .tradeRate((String) quoteObj.get("tradeRate"))
                .expiresAt((String) quoteObj.get("expiresAt"))
                .refreshesAt((String) quoteObj.get("refreshesAt"))
                .baseAmount(baseAmount)
                .quoteAmount(quoteAmount);

            this.quotes.add(quote);
        }
    }

    public List<ExchangeRateQuote> getQuotes() {
        return quotes;
    }

    public String getClientMutationId() {
        return this.clientMutationId;
    }
}
