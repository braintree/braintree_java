package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.CustomerRequest;
import com.braintreegateway.testhelpers.TestHelper;

public class CustomerRequestTest {
  @Test
  public void toXmlIncludesAllElements() throws IOException, SAXException {
    CustomerRequest request = new CustomerRequest()
      .company("some-company")
      .customerId("customer-id")
      .customField("some-custom-field", "some-custom-value")
      .defaultPaymentMethodToken("some-token")
      .deviceData("{\"device_session_id\": \"devicesession123\", \"fraud_merchant_id\": \"fraudmerchant456\"}")
      .email("some-email")
      .fax("some-fax-number")
      .firstName("Dan")
      .id("some-id")
      .lastName("Schulman")
      .paymentMethodNonce("some-nonce")
      .phone("some-phone-number")
      .website("some-website")
      .threeDSecureAuthenticationId("some-three-d-secure-authentication-id")
      .androidPayCard().number("12345").done()
      .applePayCard().number("12345").done()
      .creditCard().number("12345").done()
      .riskData().customerIP("6789").done()
      .taxIdentifier()
        .countryCode("US")
        .identifier("123")
        .done()
      .options().paypal().amount(BigDecimal.valueOf(10)).done().done();

    String expectedXML =
      "<customer>\n"
      + "  <androidPayCard>\n"
      + "    <number>12345</number>\n"
      + "  </androidPayCard>\n"
      + "  <applePayCard>\n"
      + "    <number>12345</number>\n"
      + "  </applePayCard>\n"
      + "  <company>some-company</company>\n"
      + "  <creditCard>\n"
      + "    <number>12345</number>\n"
      + "  </creditCard>\n"
      + "  <customFields>\n"
      + "    <some-custom-field>some-custom-value</some-custom-field>\n"
      + "  </customFields>\n"
      + "  <defaultPaymentMethodToken>some-token</defaultPaymentMethodToken>\n"
      + "  <deviceData>{&quot;device_session_id&quot;: &quot;devicesession123&quot;, &quot;fraud_merchant_id&quot;: &quot;fraudmerchant456&quot;}</deviceData>\n"
      + "  <email>some-email</email>\n"
      + "  <fax>some-fax-number</fax>\n"
      + "  <firstName>Dan</firstName>\n"
      + "  <id>some-id</id>\n"
      + "  <lastName>Schulman</lastName>\n"
      + "  <options>\n"
      + "    <paypal>\n"
      + "      <amount>10</amount>\n"
      + "    </paypal>\n"
      + "  </options>\n"
      + "  <paymentMethodNonce>some-nonce</paymentMethodNonce>\n"
      + "  <phone>some-phone-number</phone>\n"
      + "  <riskData>\n"
      + "    <customerIP>6789</customerIP>\n"
      + "  </riskData>\n"
      + "  <taxIdentifiers type=\"array\">\n"
      + "    <taxIdentifier>\n"
      + "      <countryCode>US</countryCode>\n"
      + "      <identifier>123</identifier>\n"
      + "    </taxIdentifier>\n"
      + "  </taxIdentifiers>\n"
      + "  <threeDSecureAuthenticationId>some-three-d-secure-authentication-id</threeDSecureAuthenticationId>\n"
      + "  <website>some-website</website>\n"
      + "</customer>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }

  @Test
  public void toXmlIncludesSecurityParams() {
    CustomerRequest request = new CustomerRequest().
      creditCard().
      deviceSessionId("devicesession123").
      fraudMerchantId("fraudmerchant456").
      done();

    TestHelper.assertIncludes("devicesession123", request.toXML());
    TestHelper.assertIncludes("fraudmerchant456", request.toXML());
  }

  @Test
  public void toXmlIncludesInternationalPhone() {
      CustomerRequest request = new CustomerRequest().internationalPhone().countryCode("1").nationalNumber("3121234567").done();
      TestHelper.assertIncludes("<internationalPhone><countryCode>1</countryCode><nationalNumber>3121234567</nationalNumber></internationalPhone>", request.toXML());
  }
}
