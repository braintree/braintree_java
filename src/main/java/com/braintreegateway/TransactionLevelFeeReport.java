package com.braintreegateway;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class TransactionLevelFeeReport {
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
            csvRecords.add(record);
        }
    }

    public List<CSVRecord> getCSVRecords() {
        return csvRecords;
    }

    public Boolean isValid() {
        return this.valid;
    }
}
