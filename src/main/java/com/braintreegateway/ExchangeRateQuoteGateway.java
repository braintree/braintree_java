package com.braintreegateway;

import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.GraphQLClient;
import com.braintreegateway.util.Http;
import java.util.Map;

/**
 * Provides methods to request currency conversion rate for a specific currency pair from PayPal Pricing
 */

public class ExchangeRateQuoteGateway {

    private Http http;
    private GraphQLClient graphQLClient;
    private Configuration configuration;

    private static final String EXCHANGE_RATE_QUOTE_MUTATION =
        "mutation ($exchangeRateQuoteRequest: GenerateExchangeRateQuoteInput!) { "
        + "generateExchangeRateQuote(input: $exchangeRateQuoteRequest) {"
        + "    quotes {"
        + "      id"
        + "      baseAmount {value, currencyCode}"
        + "      quoteAmount {value, currencyCode}"
        + "      exchangeRate"
        + "      tradeRate"
        + "      expiresAt"
        + "      refreshesAt"
        + "    }"
        + "  }"
        + "}";

    public ExchangeRateQuoteGateway(Http http, GraphQLClient graphQLClient, Configuration configuration) {
        this.http = http;
        this.graphQLClient = graphQLClient;
        this.configuration = configuration;
    }

    /**
     * Generate Exchange Rate Quote.
     *
     * @param request the ExchangeRateQuoteRequest.
     * @return a {@link Result}
     */
    public Result<ExchangeRateQuotePayload> generate(ExchangeRateQuoteRequest request) {

        Map<String, Object> response = graphQLClient.query(EXCHANGE_RATE_QUOTE_MUTATION, request);
        ValidationErrors errors = GraphQLClient.getErrors(response);
        ExchangeRateQuotePayload exchangeRateQuotePayload;
        if (errors != null) {
            return new Result<>(errors);
        }

        try {
            Map<String, Object> data = (Map) response.get("data");
            Map<String, Object> result = (Map) data.get("generateExchangeRateQuote");
            exchangeRateQuotePayload = new ExchangeRateQuotePayload(result);

        } catch (Exception e) {
            throw new UnexpectedException("Couldn't parse response: " + e.getMessage());
        }

        return new Result<>(exchangeRateQuotePayload);
    }
}
