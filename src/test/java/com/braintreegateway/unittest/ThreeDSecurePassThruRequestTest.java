package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.ThreeDSecurePassThruRequest;
import com.braintreegateway.enums.ThreeDSecurePassThruNetwork;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import org.junit.jupiter.api.Test;

public class ThreeDSecurePassThruRequestTest {
  @Test
  public void toXMLIncludesAllElements() throws IOException, SAXException {
    ThreeDSecurePassThruRequest request = new ThreeDSecurePassThruRequest()
        .eciFlag("05")
        .cavv("some-cavv")
        .threeDSecureVersion("2.2.0")
        .xid("some-xid")
        .authenticationResponse("some-auth-response")
        .directoryResponse("some-directory-response")
        .cavvAlgorithm("algorithm")
        .dsTransactionId("some-ds-transaction-id")
        .network(ThreeDSecurePassThruNetwork.EFTPOS);

    String expectedXML = "<threeDSecurePassThru>\n"
        + "  <eciFlag>05</eciFlag>\n"
        + "  <cavv>some-cavv</cavv>\n"
        + "  <threeDSecureVersion>2.2.0</threeDSecureVersion>\n"
        + "  <xid>some-xid</xid>\n"
        + "  <authenticationResponse>some-auth-response</authenticationResponse>\n"
        + "  <directoryResponse>some-directory-response</directoryResponse>\n"
        + "  <cavvAlgorithm>algorithm</cavvAlgorithm>\n"
        + "  <dsTransactionId>some-ds-transaction-id</dsTransactionId>\n"
        + "  <network>eftpos</network>\n"
        + "</threeDSecurePassThru>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}
