package com.braintreegateway;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TransactionLevelFeeReportTest {
    @Test
    public void interchangeReportIsParsed() throws IOException, ParseException {
        String url = "file://"
                     + new File("src/test/resources/fixtures/transaction_level_interchange_fee_report.csv").getAbsolutePath();
        TransactionLevelFeeReport report = new TransactionLevelFeeReport(url);
        assertEquals(9, report.getRows().size());

        TransactionLevelFeeReportRow firstRow = report.getRows().get(0);
        assertEquals("ACME-anvils", firstRow.getMerchantAccount());
        assertEquals("lskdjfkb", firstRow.getTransactionId());
        assertEquals("", firstRow.getOriginalTransactionId());
        assertEquals("sale", firstRow.getTransactionType());
        assertEquals("USD", firstRow.getPresentmentCurrency());
        assertEquals("USD", firstRow.getSettlementCurrency());
        assertEquals(new BigDecimal("9.27"), firstRow.getSettlementAmount());
        assertEquals(parseDate("2018-03-14"), firstRow.getSettlementDate());
        assertEquals(parseDate("2018-03-15"), firstRow.getDisbursementDate());
        assertEquals("credit_card", firstRow.getPaymentInstrument());
        assertEquals("MasterCard", firstRow.getCardBrand());
        assertEquals("Debit", firstRow.getCardType());
        assertEquals("555519", firstRow.getFirst6OfCreditCard());
        assertEquals("CITIBANK N.A.", firstRow.getIssuingBank());
        assertEquals("BB278-TRANSACTION CLEARED AS REGULATED", firstRow.getInterchangeDescription());
        assertEquals("USD", firstRow.getInterchangeCurrency());
        assertEquals(new BigDecimal("0.00"), firstRow.getRefundedAmount());
        assertEquals(new BigDecimal("0.0005"), firstRow.getEstInterchangeRate());
        assertEquals(new BigDecimal("0.00"), firstRow.getEstInterchangeRateCredit());
        assertEquals(new BigDecimal("22.0"), firstRow.getEstInterchangeFixed());
        assertEquals(new BigDecimal("0.00"), firstRow.getEstInterchangeFixedCredit());
        assertEquals(new BigDecimal("0.22"), firstRow.getEstInterchangeTotalAmount());
        assertEquals(new BigDecimal("1.00"), firstRow.getExchangeRate());
        assertEquals("472ee959", firstRow.getOrderId());
        assertEquals("33389508073715855327822", firstRow.getAcquirerReferenceNumber());
        assertEquals("USA", firstRow.getCardIssuingCountry());
        assertEquals(new BigDecimal("0.00"), firstRow.getDiscount());
        assertEquals(new BigDecimal("0.00"), firstRow.getDiscountCredit());
        assertEquals(new BigDecimal("0.00"), firstRow.getPerTransactionFee());
        assertEquals(new BigDecimal("0.00"), firstRow.getPerTransactionFeeCredit());
        assertEquals(new BigDecimal("0.00"), firstRow.getBraintreeTotalAmount());
        assertEquals(new BigDecimal("0.22"), firstRow.getEstTotalFeeAmount());
        assertNull(firstRow.getOriginalSaleAmount());
        assertNull(firstRow.getMulticurrencyFeeAmount());
        assertNull(firstRow.getMulticurrencyFeeCredit());
        assertNull(firstRow.getTotalFeeAmount());
    }

    @Test
    public void flatFeeReportIsParsed() throws IOException, ParseException {
        String url =
            "file://" + new File("src/test/resources/fixtures/transaction_level_fee_report.csv").getAbsolutePath();
        TransactionLevelFeeReport report = new TransactionLevelFeeReport(url);
        assertEquals(9, report.getRows().size());

        TransactionLevelFeeReportRow firstRow = report.getRows().get(0);
        assertEquals("ACME-anvils", firstRow.getMerchantAccount());
        assertEquals("lsikdfsm", firstRow.getTransactionId());
        assertEquals("", firstRow.getOriginalTransactionId());
        assertEquals("sale", firstRow.getTransactionType());
        assertEquals("USD", firstRow.getPresentmentCurrency());
        assertEquals("USD", firstRow.getSettlementCurrency());
        assertEquals(new BigDecimal("24.18"), firstRow.getSettlementAmount());
        assertEquals(parseDate("2018-03-03"), firstRow.getSettlementDate());
        assertEquals(parseDate("2018-03-06"), firstRow.getDisbursementDate());
        assertEquals("credit_card", firstRow.getPaymentInstrument());
        assertEquals("Visa", firstRow.getCardBrand());
        assertEquals("Debit", firstRow.getCardType());
        assertEquals("973702", firstRow.getFirst6OfCreditCard());
        assertEquals("Wells Fargo Bank, National Association", firstRow.getIssuingBank());
        assertEquals(new BigDecimal("0.00"), firstRow.getRefundedAmount());
        assertEquals(new BigDecimal("1.00"), firstRow.getExchangeRate());
        assertEquals("5a9a0c", firstRow.getOrderId());
        assertEquals("09483509834509830459818", firstRow.getAcquirerReferenceNumber());
        assertEquals("USA", firstRow.getCardIssuingCountry());
        assertEquals(new BigDecimal("0.0185"), firstRow.getDiscount());
        assertEquals(new BigDecimal("0"), firstRow.getDiscountCredit());
        assertEquals(new BigDecimal("0.13"), firstRow.getPerTransactionFee());
        assertEquals(new BigDecimal("0.00"), firstRow.getPerTransactionFeeCredit());
        assertEquals(new BigDecimal("0.57"), firstRow.getBraintreeTotalAmount());
        assertEquals(new BigDecimal("0.00"), firstRow.getMulticurrencyFeeAmount());
        assertEquals(new BigDecimal("0.00"), firstRow.getMulticurrencyFeeCredit());
        assertEquals(new BigDecimal("0.57"), firstRow.getTotalFeeAmount());
        assertNull(firstRow.getOriginalSaleAmount());
        assertNull(firstRow.getInterchangeDescription());
        assertNull(firstRow.getInterchangeCurrency());
        assertNull(firstRow.getEstInterchangeRate());
        assertNull(firstRow.getEstInterchangeRateCredit());
        assertNull(firstRow.getEstInterchangeFixed());
        assertNull(firstRow.getEstInterchangeFixedCredit());
        assertNull(firstRow.getEstInterchangeTotalAmount());
        assertNull(firstRow.getEstTotalFeeAmount());
    }

    @Test
    public void itHandlesEmptyURLs() throws ParseException, IOException {
        TransactionLevelFeeReport report = new TransactionLevelFeeReport("");
        assertEquals(0, report.getRows().size());
    }

    private Calendar parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(dateFormat.parse(dateString));
        return calendar;
    }
}
