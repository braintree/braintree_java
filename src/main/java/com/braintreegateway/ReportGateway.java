package com.braintreegateway;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import com.braintreegateway.util.GraphQLClient;
import com.braintreegateway.util.Http;

/**
 * Provides methods to interact with reports.
 * This class does not need to be instantiated directly.
 * Instead, use {@link BraintreeGateway#transaction()} to get an instance of this class:
 *
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.report().transactionLevelFees(...)
 * </pre>
 */
public class ReportGateway {

    private Http http;
    private GraphQLClient graphQLClient;
    private Configuration configuration;

    private static final String TRANSACTION_LEVEL_FEES_QUERY =
        "query TransactionLevelFeeReport($date: Date!, $merchantAccountId: ID) {"
        + "    report {"
        + "        transactionLevelFees(date: $date, merchantAccountId: $merchantAccountId) {"
        + "            url"
        + "        }"
        + "    }"
        + "}";

    public ReportGateway(Http http, GraphQLClient graphQLClient, Configuration configuration) {
        this.http = http;
        this.graphQLClient = graphQLClient;
        this.configuration = configuration;
    }

    /**
     * Retrieves a Transaction-Level Fee Report.
     *
     * @param request the request.
     * @return a {@link Result}
     * @throws IOException when data is malforned
     * @throws ParseException when parsing fails
     */
    public Result<TransactionLevelFeeReport> transactionLevelFees(TransactionLevelFeeReportRequest request)
        throws IOException, ParseException {
        String url = null;

        Map<String, Object> response = graphQLClient.query(TRANSACTION_LEVEL_FEES_QUERY, request);
        ValidationErrors errors = GraphQLClient.getErrors(response);
        if (errors != null) {
            return new Result<TransactionLevelFeeReport>(errors);
        }

        try {
            Map<String, Object> data = (Map) response.get("data");
            Map<String, Object> report = (Map) data.get("report");
            Map<String, Object> transactionLevelFees = (Map) report.get("transactionLevelFees");

            url = (String) transactionLevelFees.get("url");
        } catch (NullPointerException e) {
            url = null;
        }

        return new Result<TransactionLevelFeeReport>(new TransactionLevelFeeReport(url));
    }
}
