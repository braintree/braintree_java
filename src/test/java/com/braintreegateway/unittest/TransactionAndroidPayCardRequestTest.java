package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.TransactionAndroidPayCardRequest;
import com.braintreegateway.TransactionRequest;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import org.junit.jupiter.api.Test;

public class TransactionAndroidPayCardRequestTest {
  @Test
  public void toXMLIncludesAllElements() throws IOException, SAXException {
    TransactionAndroidPayCardRequest request = new TransactionAndroidPayCardRequest(new TransactionRequest()).
      cryptogram("some-cryptogram").
      eciIndicator("eci-indicator").
      expirationMonth("some-month").
      expirationYear("some-year").
      googleTransactionId("some-id").
      number("some-card-number").
      sourceCardLastFour("1234").
      sourceCardType("some-type");

    String expectedXML =
      "<androidPayCard>\n"
      + "  <cryptogram>some-cryptogram</cryptogram>\n"
      + "  <eciIndicator>eci-indicator</eciIndicator>\n"
      + "  <expirationMonth>some-month</expirationMonth>\n"
      + "  <expirationYear>some-year</expirationYear>\n"
      + "  <googleTransactionId>some-id</googleTransactionId>\n"
      + "  <number>some-card-number</number>\n"
      + "  <sourceCardLastFour>1234</sourceCardLastFour>\n"
      + "  <sourceCardType>some-type</sourceCardType>\n"
      + "</androidPayCard>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}
