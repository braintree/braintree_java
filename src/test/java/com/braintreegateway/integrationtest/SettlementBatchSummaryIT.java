package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.testhelpers.TestHelper;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class SettlementBatchSummaryIT extends IntegrationTestNew {

    private TimeZone eastern_timezone;

    @BeforeEach
    public void setTimezone() {
        this.eastern_timezone = TimeZone.getTimeZone("America/New_York");
    }

    @Test
    public void returnEmptyCollectionIfThereIsNoData() {
        Calendar settlementDate = Calendar.getInstance();
        settlementDate.add(Calendar.YEAR, -5);

        Result<SettlementBatchSummary> result = gateway.settlementBatchSummary().generate(settlementDate);
        assertTrue(result.isSuccess());
    }

    @Test
    public void formatsDateString() {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(2011, 7, 31);
        assertEquals(SettlementBatchSummaryRequest.dateString(time), "2011-08-31");
    }


    @Test
    public void formatsDateStringOnBoundary() {
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        Calendar time = Calendar.getInstance(tz);
        time.clear();
        time.set(2011, 7, 31, 23, 00);
        assertEquals(SettlementBatchSummaryRequest.dateString(time), "2011-08-31");
    }

    @Test
    public void returnsDataForTheGivenSettlementDate() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                cvv("321").
                expirationDate("05/2009").
                done().
            options().
               submitForSettlement(true).
               done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        TestHelper.settle(gateway, result.getTarget().getId());

        Result<SettlementBatchSummary> summaryResult = gateway.settlementBatchSummary().generate(Calendar.getInstance(eastern_timezone));
        assertTrue(summaryResult.isSuccess());

        assertTrue(summaryResult.getTarget().getRecords().size() > 0);

        Map<String, String> first = summaryResult.getTarget().getRecords().get(0);
        assertTrue(first.containsKey("kind"));
        assertTrue(first.containsKey("count"));
        assertTrue(first.containsKey("amount_settled"));
        assertTrue(first.containsKey("merchant_account_id"));
    }

    @Test
    public void returnsDataGroupedByTheGivenCustomField() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                cvv("321").
                expirationDate("05/2009").
                done().
            customField("store_me", "1").
            options().
               submitForSettlement(true).
               done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        TestHelper.settle(gateway, result.getTarget().getId());

        Result<SettlementBatchSummary> summaryResult = gateway.settlementBatchSummary().generate(Calendar.getInstance(eastern_timezone), "store_me");
        assertTrue(summaryResult.isSuccess());

        assertTrue(summaryResult.getTarget().getRecords().size() > 0);

        Map<String, String> first = summaryResult.getTarget().getRecords().get(0);
        assertTrue(first.containsKey("store_me"));
    }
}
