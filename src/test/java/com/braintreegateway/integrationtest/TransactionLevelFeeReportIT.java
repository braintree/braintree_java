package com.braintreegateway.integrationtest;

import java.io.IOException;
import java.text.ParseException;
import java.util.GregorianCalendar;

import com.braintreegateway.Result;
import com.braintreegateway.TransactionLevelFeeReport;
import com.braintreegateway.TransactionLevelFeeReportRequest;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionLevelFeeReportIT extends IntegrationTest {
    private Http http;


    @BeforeEach
    public void createHttp() {
        http = new Http(this.gateway.getConfiguration());
    }

    @Test
    public void itConnectsToTheGatewayAndParsesSuccessfully() throws IOException, ParseException {
        TransactionLevelFeeReportRequest request = new TransactionLevelFeeReportRequest()
            .date(new GregorianCalendar(2018, 1, 1))
            .merchantAccountId("14LaddersLLC_instant");

        Exception e = assertThrows(NotFoundException.class, () -> {
            gateway.report().transactionLevelFees(request);
        });

        assertEquals(e.getMessage(), "No report exists because there are no transactions on that date.");
    }

    @Test
    public void itFailsForUnknownMerchantAccount() throws IOException, ParseException {
        TransactionLevelFeeReportRequest request = new TransactionLevelFeeReportRequest()
            .date(new GregorianCalendar(2018, 1, 1))
            .merchantAccountId("some_nonexistent_merchant_account");

        Result<TransactionLevelFeeReport> result = gateway.report().transactionLevelFees(request);
        assertFalse(result.isSuccess());
        ValidationError theError = result.getErrors().getAllValidationErrors().get(0);
        assertEquals(ValidationErrorCode.REPORT_TRANSACTION_LEVEL_FEES_UNKNOWN_MERCHANT_ACCOUNT, theError.getCode());
        assertEquals("Invalid merchant account id: some_nonexistent_merchant_account", theError.getMessage());
    }
}
