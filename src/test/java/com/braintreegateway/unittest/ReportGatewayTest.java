package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.ReportGateway;
import com.braintreegateway.Result;
import com.braintreegateway.TransactionLevelFeeReport;
import com.braintreegateway.TransactionLevelFeeReportRequest;
import com.braintreegateway.util.GraphQLClient;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.MapUtils;

public class ReportGatewayTest {
    private GraphQLClient graphQLClient;
    private ReportGateway gateway;

    @BeforeEach
    public void setup() {
        Http http = mock(Http.class);
        graphQLClient = mock(GraphQLClient.class);
        Configuration configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new ReportGateway(http, graphQLClient, configuration);
    }

    @Test
    public void transactionLevelFeesReturnsSuccessResultWithNullUrl() throws IOException, ParseException {
        // Null URL causes TransactionLevelFeeReport to mark itself invalid without fetching — isValid() is still exercised.
        Map<String, Object> transactionLevelFees = MapUtils.toMap("url", null);
        Map<String, Object> report = MapUtils.toMap("transactionLevelFees", transactionLevelFees);
        Map<String, Object> data = MapUtils.toMap("report", report);
        Map<String, Object> response = MapUtils.toMap("data", data);
        when(graphQLClient.query(anyString(), any(TransactionLevelFeeReportRequest.class))).thenReturn(response);

        Result<TransactionLevelFeeReport> result = gateway.transactionLevelFees(
                new TransactionLevelFeeReportRequest().date(Calendar.getInstance()));

        assertTrue(result.isSuccess());
        assertFalse(result.getTarget().isValid());
    }

    @Test
    public void transactionLevelFeesReturnsErrorsWhenGraphQLErrors() throws IOException, ParseException {
        Map<String, Object> errorEntry = new HashMap<>();
        errorEntry.put("message", "An error occurred");
        Map<String, Object> response = new HashMap<>();
        response.put("errors", java.util.Arrays.asList(errorEntry));
        when(graphQLClient.query(anyString(), any(TransactionLevelFeeReportRequest.class))).thenReturn(response);

        Result<TransactionLevelFeeReport> result = gateway.transactionLevelFees(
                new TransactionLevelFeeReportRequest().date(Calendar.getInstance()));

        assertFalse(result.isSuccess());
    }

    @Test
    public void transactionLevelFeesHandlesNullDataGracefully() throws IOException, ParseException {
        Map<String, Object> response = MapUtils.toMap("data", null);
        when(graphQLClient.query(anyString(), any(TransactionLevelFeeReportRequest.class))).thenReturn(response);

        Result<TransactionLevelFeeReport> result = gateway.transactionLevelFees(
                new TransactionLevelFeeReportRequest().date(Calendar.getInstance()));

        assertTrue(result.isSuccess());
        assertFalse(result.getTarget().isValid());
    }
}
