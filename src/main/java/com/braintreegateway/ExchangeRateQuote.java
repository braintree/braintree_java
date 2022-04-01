package com.braintreegateway;


public class ExchangeRateQuote {

    private MonetaryAmount baseAmount;
    private MonetaryAmount quoteAmount;
    private String exchangeRate;
    private String expiresAt;
    private String refreshesAt;
    private String tradeRate;
    private String id;

    public ExchangeRateQuote id(String id) {
        this.id = id;
        return this;
    }

    public ExchangeRateQuote baseAmount(MonetaryAmount baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public ExchangeRateQuote quoteAmount(MonetaryAmount quoteAmount) {
        this.quoteAmount = quoteAmount;
        return this;
    }

    public ExchangeRateQuote exchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public ExchangeRateQuote expiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public ExchangeRateQuote refreshesAt(String refreshesAt) {
        this.refreshesAt = refreshesAt;
        return this;
    }

    public ExchangeRateQuote tradeRate(String tradeRate) {
        this.tradeRate = tradeRate;
        return this;
    }

    public MonetaryAmount getBaseAmount() {
        return baseAmount;
    }

    public MonetaryAmount getQuoteAmount() {
        return quoteAmount;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public String getRefreshesAt() {
        return refreshesAt;
    }

    public String getTradeRate() {
        return tradeRate;
    }

    public String getId() {
        return id;
    }
}
