package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.Request;
import com.braintreegateway.Result;
import com.braintreegateway.SettlementBatchSummary;
import com.braintreegateway.SettlementBatchSummaryGateway;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class SettlementBatchSummaryGatewayTest {
    private Http http;
    private Configuration configuration;
    private SettlementBatchSummaryGateway gateway;
    private String merchantPath;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new SettlementBatchSummaryGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<settlement-batch-summary>" +
                "<records type=\"array\">" +
                "<record>" +
                "<kind>sale</kind>" +
                "<count>2</count>" +
                "<amount-settled>100.00</amount-settled>" +
                "</record>" +
                "</records>" +
                "</settlement-batch-summary>");
        when(http.post(eq(merchantPath + "/settlement_batch_summary"), nullable(Request.class))).thenReturn(node);
    }

    @Test
    public void generate() throws ParseException {
        Result<SettlementBatchSummary> result = gateway.generate(CalendarTestUtils.date("2016-01-14"));

        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getRecords().size());
        assertEquals("sale", result.getTarget().getRecords().get(0).get("kind"));

        String xml = capturePostedRequestXml();
        assertTrue(xml.contains("<settlement-date>2016-01-14</settlement-date>"), xml);
        assertFalse(xml.contains("group-by-custom-field"), xml);
    }

    @Test
    public void generateWithGroupByCustomField() throws ParseException {
        Result<SettlementBatchSummary> result =
                gateway.generate(CalendarTestUtils.date("2016-01-14"), "a_custom_field");

        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getRecords().size());

        String xml = capturePostedRequestXml();
        assertTrue(xml.contains("<settlement-date>2016-01-14</settlement-date>"), xml);
        assertTrue(xml.contains("<group-by-custom-field>a_custom_field</group-by-custom-field>"), xml);
    }

    private String capturePostedRequestXml() {
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(http).post(eq(merchantPath + "/settlement_batch_summary"), captor.capture());
        return captor.getValue().toXML();
    }
}
