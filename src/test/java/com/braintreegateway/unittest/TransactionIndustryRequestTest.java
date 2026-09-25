package com.braintreegateway.unittest;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import com.braintreegateway.TransactionRequest;
import com.braintreegateway.TransactionIndustryRequest;
import com.braintreegateway.Transaction;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import org.junit.jupiter.api.Test;

public class TransactionIndustryRequestTest {
    @Test
    public void toXMLIncludesAllElements() throws IOException, SAXException {

        TransactionRequest txnRequest = new TransactionRequest();
        TransactionIndustryRequest request = new TransactionIndustryRequest(txnRequest);

        Calendar arrivalDate = Calendar.getInstance();
        arrivalDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        arrivalDate.set(2023, Calendar.FEBRUARY, 10, 22, 45, 30);

        request.industryType(Transaction.IndustryType.TRAVEL_FLIGHT)
            .data()
                .passengerFirstName("John")
                .arrivalDate(arrivalDate)
                .ticketIssuerAddress("TIAddress")
                .issuingCarrierCode("AA34")
                .ticketNumber("ticket-number")
                .done()
            .done();

		String expectedXML =
            "<industry>\n" +
            "  <industryType>travel_flight</industryType>\n" +
            "  <data>\n" +
            "    <passengerFirstName>John</passengerFirstName>\n" +
            "    <arrivalDate type='datetime'>2023-02-10T22:45:30Z</arrivalDate>\n" +
            "    <ticketIssuerAddress>TIAddress</ticketIssuerAddress>\n" +
            "    <issuingCarrierCode>AA34</issuingCarrierCode>\n" +
            "    <ticketNumber>ticket-number</ticketNumber>\n" +
            "  </data>\n" +
            "</industry>";

        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(expectedXML, request.toXML());

    }
}
