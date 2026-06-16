package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AccountUpdaterDailyReport;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

public class AccountUpdaterDailyReportTest {
    @Test
    public void parsesReportUrlAndReportDate() throws ParseException {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<account-updater-daily-report>");
        builder.append("<report-url>link-to-csv-report</report-url>");
        builder.append("<report-date type=\"date\">2016-01-14</report-date>");
        builder.append("</account-updater-daily-report>");

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(builder.toString());
        AccountUpdaterDailyReport report = new AccountUpdaterDailyReport(node);

        assertEquals("link-to-csv-report", report.getReportUrl());
        assertEquals(CalendarTestUtils.date("2016-01-14"), report.getReportDate());
    }

    @Test
    public void handlesMissingFields() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<account-updater-daily-report>");
        builder.append("</account-updater-daily-report>");

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(builder.toString());
        AccountUpdaterDailyReport report = new AccountUpdaterDailyReport(node);

        assertNull(report.getReportUrl());
        assertNull(report.getReportDate());
    }
}
