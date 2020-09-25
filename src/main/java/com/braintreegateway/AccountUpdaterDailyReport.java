package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public final class AccountUpdaterDailyReport {
    private final String reportUrl;
    private final Calendar reportDate;

    public AccountUpdaterDailyReport(NodeWrapper node) {
        this.reportUrl = node.findString("report-url");
        this.reportDate = node.findDate("report-date");
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public Calendar getReportDate() {
        return reportDate;
    }
}

