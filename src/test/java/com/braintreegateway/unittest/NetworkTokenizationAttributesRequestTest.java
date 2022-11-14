package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.NetworkTokenizationAttributesRequest;
import com.braintreegateway.TransactionCreditCardRequest;
import com.braintreegateway.TransactionRequest;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import org.junit.jupiter.api.Test;

public class NetworkTokenizationAttributesRequestTest {
  @Test
  public void toXMLIncludesAllElements() throws IOException, SAXException {
    TransactionCreditCardRequest transactionCreditCardRequest = new TransactionCreditCardRequest(
        new TransactionRequest());
    NetworkTokenizationAttributesRequest request = new NetworkTokenizationAttributesRequest(
        transactionCreditCardRequest)
        .cryptogram("some-cryptogram")
        .ecommerceIndicator("ecommerce-indicator")
        .tokenRequestorId("token-requestor-id");

    String expectedXML = "<networkTokenizationAttributes>\n"
        + "    <cryptogram>some-cryptogram</cryptogram>\n"
        + "    <ecommerceIndicator>ecommerce-indicator</ecommerceIndicator>\n"
        + "    <tokenRequestorId>token-requestor-id</tokenRequestorId>\n"
        + "  </networkTokenizationAttributes>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}
