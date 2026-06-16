package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionIndustryDataLegRequest;
import com.braintreegateway.TransactionIndustryDataRequest;

public class TransactionIndustryDataLegRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        TransactionIndustryDataRequest parent = new TransactionIndustryDataRequest(null);
        TransactionIndustryDataLegRequest request = new TransactionIndustryDataLegRequest(parent);

        TransactionIndustryDataRequest returned = request
                .conjunctionTicket("conj-ticket")
                .exchangeTicket("exch-ticket")
                .couponNumber("coupon-1")
                .serviceClass("Y")
                .carrierCode("AA")
                .fareBasisCode("fare-basis")
                .flightNumber("1234")
                .departureDate(Calendar.getInstance())
                .departureAirportCode("ORD")
                .departureTime("08:00")
                .arrivalAirportCode("SFO")
                .arrivalTime("11:00")
                .stopoverPermitted(true)
                .fareAmount(new BigDecimal("100.00"))
                .feeAmount(new BigDecimal("10.00"))
                .taxAmount(new BigDecimal("5.00"))
                .endorsementOrRestrictions("none")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertAll("industry data leg xml",
                () -> assertTrue(xml.contains("<item>"), xml),
                () -> assertTrue(xml.contains("<conjunctionTicket>conj-ticket</conjunctionTicket>"), xml),
                () -> assertTrue(xml.contains("<exchangeTicket>exch-ticket</exchangeTicket>"), xml),
                () -> assertTrue(xml.contains("<couponNumber>coupon-1</couponNumber>"), xml),
                () -> assertTrue(xml.contains("<serviceClass>Y</serviceClass>"), xml),
                () -> assertTrue(xml.contains("<carrierCode>AA</carrierCode>"), xml),
                () -> assertTrue(xml.contains("<fareBasisCode>fare-basis</fareBasisCode>"), xml),
                () -> assertTrue(xml.contains("<flightNumber>1234</flightNumber>"), xml),
                () -> assertTrue(xml.contains("<departureDate type=\"datetime\">"), xml),
                () -> assertTrue(xml.contains("<departureAirportCode>ORD</departureAirportCode>"), xml),
                () -> assertTrue(xml.contains("<departureTime>08:00</departureTime>"), xml),
                () -> assertTrue(xml.contains("<arrivalAirportCode>SFO</arrivalAirportCode>"), xml),
                () -> assertTrue(xml.contains("<arrivalTime>11:00</arrivalTime>"), xml),
                () -> assertTrue(xml.contains("<stopoverPermitted>true</stopoverPermitted>"), xml),
                () -> assertTrue(xml.contains("<fareAmount>100.00</fareAmount>"), xml),
                () -> assertTrue(xml.contains("<feeAmount>10.00</feeAmount>"), xml),
                () -> assertTrue(xml.contains("<taxAmount>5.00</taxAmount>"), xml),
                () -> assertTrue(xml.contains("<endorsementOrRestrictions>none</endorsementOrRestrictions>"), xml));

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("conj-ticket"), queryString);
    }
}
