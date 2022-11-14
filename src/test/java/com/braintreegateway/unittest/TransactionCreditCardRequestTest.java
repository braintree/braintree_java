package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.TransactionCreditCardRequest;
import com.braintreegateway.TransactionRequest;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import org.junit.jupiter.api.Test;

public class TransactionCreditCardRequestTest {
  @Test
  public void toXMLIncludesAllElements() throws IOException, SAXException {
    TransactionCreditCardRequest request = new TransactionCreditCardRequest(new TransactionRequest())
        .cardholderName("some-name").cvv("some-cvv").expirationDate("some-date").expirationMonth("some-month")
        .expirationYear("some-year").number("some-card-number").token("some-token")
        .paymentReaderCardDetails()
        .encryptedCardData("some-encrypted-card-data")
        .keySerialNumber("some-serial-number")
        .done()
        .networkTokenizationAttributes()
        .cryptogram("cryptogram")
        .ecommerceIndicator("eci-indicator")
        .tokenRequestorId("token-requestor-id")
        .done();

    String expectedXML = "<creditCard>\n"
        + "  <cardholderName>some-name</cardholderName>\n"
        + "  <cvv>some-cvv</cvv>\n"
        + "  <number>some-card-number</number>\n"
        + "  <expirationDate>some-date</expirationDate>\n"
        + "  <expirationMonth>some-month</expirationMonth>\n"
        + "  <expirationYear>some-year</expirationYear>\n"
        + "  <token>some-token</token>\n"
        + "  <paymentReaderCardDetails>\n"
        + "    <encryptedCardData>some-encrypted-card-data</encryptedCardData>\n"
        + "    <keySerialNumber>some-serial-number</keySerialNumber>\n"
        + "  </paymentReaderCardDetails>\n"
        + "  <networkTokenizationAttributes>\n"
        + "    <cryptogram>cryptogram</cryptogram>\n"
        + "    <ecommerceIndicator>eci-indicator</ecommerceIndicator>\n"
        + "    <tokenRequestorId>token-requestor-id</tokenRequestorId>\n"
        + "  </networkTokenizationAttributes>\n"
        + " </creditCard>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}
