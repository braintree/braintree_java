package com.braintreegateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeRateQuoteRequest extends Request {

    private List<ExchangeRateQuoteInput> quotes = new ArrayList<>();

    public ExchangeRateQuoteInput addExchangeRateQuoteInput() {
        ExchangeRateQuoteInput newInput = new ExchangeRateQuoteInput(this);
        quotes.add(newInput);
        return newInput;
    }

    @Override
    public Map<String, Object> toGraphQLVariables() {

        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> input = new HashMap<>();

        List<Map<String, Object>> quotesList = new ArrayList<>();

        for (ExchangeRateQuoteInput quote : this.quotes) {
            quotesList.add(quote.toGraphQLVariables());
        }

        input.put("quotes", quotesList.toArray());
        variables.put("exchangeRateQuoteRequest", input);

        return variables;
    }
}
