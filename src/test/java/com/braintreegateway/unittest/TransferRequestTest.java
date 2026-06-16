package com.braintreegateway.unittest;

import java.io.IOException;
import com.braintreegateway.TransferRequest;
import java.text.ParseException;
import java.util.Calendar;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class TransferRequestTest {

    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException, ParseException {
        TransferRequest transferRequest = new TransferRequest();

        String[] transferTypes = {"account_to_account", "person_to_person", "wallet_transfer", "fund_transfer", "fund_disbursement", "payroll_disbursement", "prepaid_top_up"};
        Calendar dateOfBirth = CalendarTestUtils.date("2012-04-10");

         for(String transferType : transferTypes) {
             transferRequest.type(transferType)
                 .sender()
                    .firstName("Alice")
                    .lastName("Silva")
                    .middleName("A")
                    .accountReferenceNumber("1000012345")
                    .accountReferenceNumberType("IBAN")
                    .address()
                        .streetAddress("1st Main Road")
                        .locality("Los Angeles")
                        .region("CA")
                        .countryCodeAlpha2("US")
                        .done()
                    .dateOfBirth(dateOfBirth)
                    .done()
                 .receiver()
                    .firstName("Bob")
                    .lastName("Souza")
                    .middleName("A")
                    .accountReferenceNumber("9876543210")
                    .accountReferenceNumberType("PHONE_NUMBER")
                    .address()
                        .streetAddress("2nd Main Road")
                        .locality("Los Angeles")
                        .region("CA")
                        .countryCodeAlpha2("US")
                        .done()
                    .done();

             String expectedXML =
                 "<transfer>" +
                    "<type>" + transferType + "</type>" +
                    "<sender>" +
                        "<firstName>Alice</firstName>" +
                        "<lastName>Silva</lastName>" +
                        "<middleName>A</middleName>" +
                        "<accountReferenceNumber>1000012345</accountReferenceNumber>" +
                        "<accountReferenceNumberType>IBAN</accountReferenceNumberType>" +
                        "<address>" +
                            "<countryCodeAlpha2>US</countryCodeAlpha2>" +
                            "<locality>Los Angeles</locality>" +
                            "<region>CA</region>" +
                            "<streetAddress>1st Main Road</streetAddress>" +
                        "</address>" +
                        "<dateOfBirth type='datetime'>2012-04-10T00:00:00Z</dateOfBirth>" +
                    "</sender>" +
                    "<receiver>" +
                        "<firstName>Bob</firstName>" +
                        "<lastName>Souza</lastName>" +
                        "<middleName>A</middleName>" +
                        "<accountReferenceNumber>9876543210</accountReferenceNumber>" +
                        "<accountReferenceNumberType>PHONE_NUMBER</accountReferenceNumberType>" +
                        "<address>" +
                            "<countryCodeAlpha2>US</countryCodeAlpha2>" +
                            "<locality>Los Angeles</locality>" +
                            "<region>CA</region>" +
                            "<streetAddress>2nd Main Road</streetAddress>" +
                        "</address>" +
                    "</receiver>" +
                "</transfer>";

            XMLUnit.setIgnoreWhitespace(true);
            XMLAssert.assertXMLEqual(expectedXML, transferRequest.toXML());
        }
    }
}
