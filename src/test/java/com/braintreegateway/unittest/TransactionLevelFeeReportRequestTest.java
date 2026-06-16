package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionLevelFeeReportRequest;

public class TransactionLevelFeeReportRequestTest {

    @Test
    public void toGraphQLVariablesIncludesDateAndMerchantAccountId() {
        Calendar date = Calendar.getInstance();
        date.set(2024, Calendar.JANUARY, 15);

        Map<String, Object> variables = new TransactionLevelFeeReportRequest()
                .date(date)
                .merchantAccountId("a-merchant-account")
                .toGraphQLVariables();

        assertEquals("2024-01-15", variables.get("date"));
        assertEquals("a-merchant-account", variables.get("merchantAccountId"));
    }

    @Test
    public void toGraphQLVariablesOmitsMerchantAccountIdWhenNull() {
        Calendar date = Calendar.getInstance();
        date.set(2024, Calendar.JANUARY, 15);

        Map<String, Object> variables = new TransactionLevelFeeReportRequest()
                .date(date)
                .toGraphQLVariables();

        assertTrue(variables.containsKey("date"));
        assertFalse(variables.containsKey("merchantAccountId"));
    }
}
