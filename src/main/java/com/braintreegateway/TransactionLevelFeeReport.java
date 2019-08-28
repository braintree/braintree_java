package com.braintreegateway;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class TransactionLevelFeeReport {
    private List<TransactionLevelFeeReportRow> rows = new LinkedList<TransactionLevelFeeReportRow>();
    private List<CSVRecord> csvRecords = new ArrayList<CSVRecord>();
    private Boolean valid;

    public TransactionLevelFeeReport(String urlValue) throws IOException, ParseException {
        if (urlValue == null || "".equals(urlValue)) {
            this.valid = false;
            return;
        }

        this.valid = true;
        URL url = new URL(urlValue);
        InputStreamReader is = new InputStreamReader(url.openStream());

        for (CSVRecord record : CSVFormat.EXCEL.withFirstRecordAsHeader().parse(is)) {
            rows.add(new TransactionLevelFeeReportRow(record));
            csvRecords.add(record);
        }
    }

    /**
     * Deprecated. Only supports the USA region. Please
     * @see TransactionLevelFeeReport#getCSVRecords() which supports all regions.
     */
    @Deprecated
    public List<TransactionLevelFeeReportRow> getRows() {
        return rows;
    }

    public List<CSVRecord> getCSVRecords() {
        return csvRecords;
    }

    public Boolean isValid() {
        return this.valid;
    }
}
