package com.braintreegateway;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a fluent interface to build up requests around Transaction-Level Fee Reports.
 */
public class TransactionLevelFeeReportRequest extends Request {
    private String date;
    private String merchantAccountId;

    public TransactionLevelFeeReportRequest date(Calendar date) {
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
        return this;
    }

    public TransactionLevelFeeReportRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    @Override
    public Map<String, Object> toGraphQLVariables() {
        Map<String, Object> variables = new HashMap();

        variables.put("date", date);
        if (merchantAccountId != null) {
            variables.put("merchantAccountId", merchantAccountId);
        }

        return variables;
    }
}
