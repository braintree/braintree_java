package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public class SettlementBatchSummaryGateway {

    private Http http;
    private Configuration configuration;

    public SettlementBatchSummaryGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<SettlementBatchSummary> generate(Calendar settlementDate) {
        SettlementBatchSummaryRequest request = new SettlementBatchSummaryRequest();
        request.settlementDate(settlementDate);
        return doGenerate(request);
    }

    public Result<SettlementBatchSummary> generate(Calendar settlementDate, String groupByCustomField) {
        SettlementBatchSummaryRequest request = new SettlementBatchSummaryRequest();
        request.settlementDate(settlementDate);
        request.groupByCustomField(groupByCustomField);
        return doGenerate(request);
    }

    private Result<SettlementBatchSummary> doGenerate(SettlementBatchSummaryRequest request) {
        NodeWrapper node = http.post(configuration.getMerchantPath() + "/settlement_batch_summary", request);
        return new Result<SettlementBatchSummary>(node, SettlementBatchSummary.class);
    }

}
