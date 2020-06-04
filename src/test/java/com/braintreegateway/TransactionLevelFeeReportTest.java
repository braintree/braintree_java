package com.braintreegateway;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TransactionLevelFeeReportTest {
    @Test
    public void interchangeReportIsParsed() throws IOException, ParseException {
        String url = "file://"
                     + new File("src/test/resources/fixtures/transaction_level_interchange_fee_report.csv").getAbsolutePath();
        TransactionLevelFeeReport report = new TransactionLevelFeeReport(url);
        assertEquals(9, report.getCSVRecords().size());

        Map<String, String> firstCsvRecord = report.getCSVRecords().get(0).toMap();
        assertEquals("ACME-anvils", firstCsvRecord.get("Merchant Account ID"));
        assertEquals("lskdjfkb", firstCsvRecord.get("Transaction ID"));
        assertEquals("", firstCsvRecord.get("Original Transaction ID"));
        assertEquals("sale", firstCsvRecord.get("Transaction Type"));
        assertEquals("USD", firstCsvRecord.get("Presentment Currency"));
        assertEquals("USD", firstCsvRecord.get("Settlement Currency"));
        assertEquals("9.27", firstCsvRecord.get("Settlement Amount"));
        assertEquals("2018-03-14", firstCsvRecord.get("Settlement Date"));
        assertEquals("2018-03-15", firstCsvRecord.get("Disbursement Date"));
        assertEquals("credit_card", firstCsvRecord.get("Payment Instrument"));
        assertEquals("MasterCard", firstCsvRecord.get("Card Brand"));
        assertEquals("Debit", firstCsvRecord.get("Card Type"));
        assertEquals("555519", firstCsvRecord.get("First 6 of CC"));
        assertEquals("CITIBANK N.A.", firstCsvRecord.get("Issuing Bank"));
        assertEquals("BB278-TRANSACTION CLEARED AS REGULATED", firstCsvRecord.get("Interchange Description"));
        assertEquals("USD", firstCsvRecord.get("Interchange Currency"));
        assertEquals("0.00", firstCsvRecord.get("Refunded Amount"));
        assertEquals("0.0005", firstCsvRecord.get("Est. Interchange Rate"));
        assertEquals("0.00", firstCsvRecord.get("Est. Interchange Rate Credit"));
        assertEquals("22.0", firstCsvRecord.get("Est. Interchange Fixed"));
        assertEquals("0.00", firstCsvRecord.get("Est. Interchange Fixed Credit"));
        assertEquals("0.22", firstCsvRecord.get("Est. Interchange Total Amount"));
        assertEquals("1.00", firstCsvRecord.get("Exchange Rate"));
        assertEquals("472ee959", firstCsvRecord.get("Order ID"));
        assertEquals("33389508073715855327822", firstCsvRecord.get("Acquirer Reference Number"));
        assertEquals("USA", firstCsvRecord.get("Card Issuing Country"));
        assertEquals("0.00", firstCsvRecord.get("Discount"));
        assertEquals("0.00", firstCsvRecord.get("Discount Credit"));
        assertEquals("0.00", firstCsvRecord.get("Per Transaction Fee"));
        assertEquals("0.00", firstCsvRecord.get("Per Transaction Fee Credit"));
        assertEquals("0.00", firstCsvRecord.get("Braintree Total Amount"));
        assertEquals("0.22", firstCsvRecord.get("Est. Total Fee Amount"));
    }

    @Test
    public void flatFeeReportIsParsed() throws IOException, ParseException {
        String url =
            "file://" + new File("src/test/resources/fixtures/transaction_level_fee_report.csv").getAbsolutePath();
        TransactionLevelFeeReport report = new TransactionLevelFeeReport(url);
        assertEquals(9, report.getCSVRecords().size());

        Map<String, String> firstCsvRecord = report.getCSVRecords().get(0).toMap();
        assertEquals("ACME-anvils", firstCsvRecord.get("Merchant Account ID"));
        assertEquals("lsikdfsm", firstCsvRecord.get("Transaction ID"));
        assertEquals("", firstCsvRecord.get("Original Transaction ID"));
        assertEquals("sale", firstCsvRecord.get("Transaction Type"));
        assertEquals("USD", firstCsvRecord.get("Presentment Currency"));
        assertEquals("USD", firstCsvRecord.get("Settlement Currency"));
        assertEquals("24.18", firstCsvRecord.get("Settlement Amount"));
        assertEquals("2018-03-03", firstCsvRecord.get("Settlement Date"));
        assertEquals("2018-03-06", firstCsvRecord.get("Disbursement Date"));
        assertEquals("credit_card", firstCsvRecord.get("Payment Instrument"));
        assertEquals("Visa", firstCsvRecord.get("Card Brand"));
        assertEquals("Debit", firstCsvRecord.get("Card Type"));
        assertEquals("973702", firstCsvRecord.get("First 6 of CC"));
        assertEquals("Wells Fargo Bank, National Association", firstCsvRecord.get("Issuing Bank"));
        assertEquals("0.00", firstCsvRecord.get("Refunded Amount"));
        assertEquals("1.00", firstCsvRecord.get("Exchange Rate"));
        assertEquals("5a9a0c", firstCsvRecord.get("Order ID"));
        assertEquals("09483509834509830459818", firstCsvRecord.get("Acquirer Reference Number"));
        assertEquals("USA", firstCsvRecord.get("Card Issuing Country"));
        assertEquals("0.0185", firstCsvRecord.get("Discount"));
        assertEquals("0", firstCsvRecord.get("Discount Credit"));
        assertEquals("0.13", firstCsvRecord.get("Per Transaction Fee"));
        assertEquals("0.00", firstCsvRecord.get("Per Transaction Fee Credit"));
        assertEquals("0.00", firstCsvRecord.get("Multicurrency Fee Amount"));
        assertEquals("0.00", firstCsvRecord.get("Multicurrency Fee Credit"));
        assertEquals("0.57", firstCsvRecord.get("Total Fee Amount"));
    }

    @Test
    public void itHandlesEmptyURLs() throws ParseException, IOException {
        TransactionLevelFeeReport report = new TransactionLevelFeeReport("");
        assertEquals(0, report.getCSVRecords().size());
    }
}
