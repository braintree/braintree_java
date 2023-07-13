package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.testhelpers.TestHelper;

public class PaymentMethodRequestTest {
  @Test
  public void toXmlIncludesAllElements() throws IOException, SAXException {
    PaymentMethodRequest request = new PaymentMethodRequest()
        .paymentMethodNonce("some-payment-method-nonce")
        .customerId("some-customer-id")
        .cvv("123")
        .expirationDate("05/12")
        .expirationMonth("05")
        .expirationYear("2012")
        .number("4111111111111111")
        .paypalRefreshToken("PAYPAL_REFRESH_TOKEN")
        .venmoSdkPaymentMethodCode("some-venmo-sdk-method-code")
        .threeDSecureAuthenticationId("some-three-d-secure-authentication-id")
        .options()
            .verifyCard(true)
            .done()
        .token("some-token")
        .deviceData("some-device-data")
        .cardholderName("Dan")
        .billingAddress()
            .streetAddress("123 Abc Way")
            .done()
        .billingAddressId("some-billing-address-id");

    String expectedXML =
      "<payment-method>\n"
      + "   <token>some-token</token>\n"
      + "   <options>\n"
      + "       <verifyCard>true</verifyCard>\n"
      + "   </options>\n"
      + "   <billingAddress>\n"
      + "       <streetAddress>123 Abc Way</streetAddress>\n"
      + "   </billingAddress>\n"
      + "   <billingAddressId>some-billing-address-id</billingAddressId>\n"
      + "   <deviceData>some-device-data</deviceData>\n"
      + "   <customerId>some-customer-id</customerId>\n"
      + "   <cardholderName>Dan</cardholderName>\n"
      + "   <cvv>123</cvv>\n"
      + "   <number>4111111111111111</number>\n"
      + "   <expirationDate>05/12</expirationDate>\n"
      + "   <expirationMonth>05</expirationMonth>\n"
      + "   <expirationYear>2012</expirationYear>\n"
      + "   <paymentMethodNonce>some-payment-method-nonce</paymentMethodNonce>\n"
      + "   <paypalRefreshToken>PAYPAL_REFRESH_TOKEN</paypalRefreshToken>\n"
      + "   <threeDSecureAuthenticationId>some-three-d-secure-authentication-id</threeDSecureAuthenticationId>\n"
      + "   <venmoSdkPaymentMethodCode>some-venmo-sdk-method-code</venmoSdkPaymentMethodCode>\n"
      + "</payment-method>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }

  @Test
  public void canSendThreeDSecurePassThruFields() throws IOException, SAXException {
    PaymentMethodRequest request = new PaymentMethodRequest()
        .paymentMethodNonce("some-payment-method-nonce")
        .customerId("some-customer-id")
        .threeDSecurePassThruRequest()
            .eciFlag("05")
            .cavv("some-cavv")
            .xid("some-xid")
            .threeDSecureVersion("2.2.0")
            .dsTransactionId("some-ds-transaction-id")
            .authenticationResponse("some-auth-response")
            .directoryResponse("some-directory-response")
            .cavvAlgorithm("algorithm")
            .done()
        .options()
            .verifyCard(true)
            .done();

    String expectedXML =
      "<payment-method>\n"
      + "   <options>\n"
      + "       <verifyCard>true</verifyCard>\n"
      + "   </options>\n"
      + "   <threeDSecurePassThru>\n"
      + "       <eciFlag>05</eciFlag>\n"
      + "       <cavv>some-cavv</cavv>\n"
      + "       <threeDSecureVersion>2.2.0</threeDSecureVersion>\n"
      + "       <xid>some-xid</xid>\n"
      + "       <authenticationResponse>some-auth-response</authenticationResponse>\n"
      + "       <directoryResponse>some-directory-response</directoryResponse>\n"
      + "       <cavvAlgorithm>algorithm</cavvAlgorithm>\n"
      + "       <dsTransactionId>some-ds-transaction-id</dsTransactionId>\n"
      + "   </threeDSecurePassThru>\n"
      + "   <customerId>some-customer-id</customerId>\n"
      + "   <paymentMethodNonce>some-payment-method-nonce</paymentMethodNonce>\n"
      + "</payment-method>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }

}
