package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateQuoteInput extends Request {

    private String baseCurrency;
    private String quoteCurrency;
    private String baseAmount; //Optional
    private String markup; //Optional
    private ExchangeRateQuoteRequest parent;

    public ExchangeRateQuoteInput(ExchangeRateQuoteRequest parent) {
        this.parent = parent;
    }

    public ExchangeRateQuoteInput baseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
        return this;
    }

    public ExchangeRateQuoteInput quoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
        return this;
    }

    public ExchangeRateQuoteInput baseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public ExchangeRateQuoteInput markup(String markup) {
        this.markup = markup;
        return this;
    }

    public ExchangeRateQuoteRequest done() {
        return this.parent;
    }

    @Override
    public Map<String, Object> toGraphQLVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("baseCurrency", baseCurrency);
        variables.put("quoteCurrency", quoteCurrency);
        if (baseAmount != null) {
            variables.put("baseAmount", baseAmount);
        }
        if (markup != null) {
            variables.put("markup", markup);
        }
        return variables;
    }
}
