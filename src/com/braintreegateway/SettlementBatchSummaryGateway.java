package com.braintreegateway;

import java.util.Calendar;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class SettlementBatchSummaryGateway {

    private Http http;

    public SettlementBatchSummaryGateway(Http http) {
        this.http = http;
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
        NodeWrapper node = http.post("/settlement_batch_summary", request);
        return new Result<SettlementBatchSummary>(node, SettlementBatchSummary.class);
    }
    
}
