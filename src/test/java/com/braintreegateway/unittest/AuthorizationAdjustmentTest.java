package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AuthorizationAdjustment;
import com.braintreegateway.ProcessorResponseType;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

public class AuthorizationAdjustmentTest {

    @Test
    public void parsesAllFields() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<authorization-adjustment>" +
                "<amount>25.00</amount>" +
                "<success type=\"boolean\">true</success>" +
                "<timestamp type=\"datetime\">2018-10-11T21:28:37Z</timestamp>" +
                "<processor-response-code>1000</processor-response-code>" +
                "<processor-response-text>Approved</processor-response-text>" +
                "<processor-response-type>approved</processor-response-type>" +
                "</authorization-adjustment>");

        AuthorizationAdjustment adjustment = new AuthorizationAdjustment(node);

        assertAll("authorization adjustment fields",
                () -> assertEquals(new BigDecimal("25.00"), adjustment.getAmount()),
                () -> assertTrue(adjustment.isSuccess()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), adjustment.getTimestamp()),
                () -> assertEquals("1000", adjustment.getProcessorResponseCode()),
                () -> assertEquals("Approved", adjustment.getProcessorResponseText()),
                () -> assertEquals(ProcessorResponseType.APPROVED, adjustment.getProcessorResponseType()));
    }

    @Test
    public void mapsUnrecognizedProcessorResponseType() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<authorization-adjustment>" +
                "<amount>10.00</amount>" +
                "<success type=\"boolean\">false</success>" +
                "<processor-response-type>not-a-real-type</processor-response-type>" +
                "</authorization-adjustment>");

        AuthorizationAdjustment adjustment = new AuthorizationAdjustment(node);

        assertEquals(ProcessorResponseType.UNRECOGNIZED, adjustment.getProcessorResponseType());
    }
}
