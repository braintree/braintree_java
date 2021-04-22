package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;

import com.braintreegateway.CustomerRequest;
import com.braintreegateway.testhelpers.TestHelper;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;

public class CustomerRequestTest {
  @Test
  public void toXmlIncludesAllElements() throws IOException, SAXException {
    CustomerRequest request = new CustomerRequest()
      .deviceData("{\"device_session_id\": \"devicesession123\", \"fraud_merchant_id\": \"fraudmerchant456\"}")
      .company("some-company")
      .customerId("customer-id")
      .email("some-email")
      .fax("some-fax-number")
      .firstName("Dan")
      .id("some-id")
      .lastName("Schulman")
      .phone("some-phone-number")
      .website("some-website")
      .paymentMethodNonce("some-nonce")
      .defaultPaymentMethodToken("some-token")
      .customField("some-custom-field", "some-custom-value")
      .creditCard().number("12345").done()
      .riskData().customerIP("6789").done()
      .taxIdentifier()
        .countryCode("US")
        .identifier("123")
        .done()
      .options().paypal().amount(BigDecimal.valueOf(10)).done().done();

    String expectedXML =
      "<customer>\n"
      + "  <deviceData>{&quot;device_session_id&quot;: &quot;devicesession123&quot;, &quot;fraud_merchant_id&quot;: &quot;fraudmerchant456&quot;}</deviceData>\n"
      + "  <company>some-company</company>\n"
      + "  <email>some-email</email>\n"
      + "  <fax>some-fax-number</fax>\n"
      + "  <firstName>Dan</firstName>\n"
      + "  <id>some-id</id>\n"
      + "  <lastName>Schulman</lastName>\n"
      + "  <phone>some-phone-number</phone>\n"
      + "  <website>some-website</website>\n"
      + "  <paymentMethodNonce>some-nonce</paymentMethodNonce>\n"
      + "  <defaultPaymentMethodToken>some-token</defaultPaymentMethodToken>\n"
      + "  <creditCard>\n"
      + "    <number>12345</number>\n"
      + "  </creditCard>\n"
      + "  <options>\n"
      + "    <paypal>\n"
      + "      <amount>10</amount>\n"
      + "    </paypal>\n"
      + "  </options>\n"
      + "  <customFields>\n"
      + "    <some-custom-field>some-custom-value</some-custom-field>\n"
      + "  </customFields>\n"
      + "  <riskData>\n"
      + "    <customerIP>6789</customerIP>\n"
      + "  </riskData>\n"
      + "  <taxIdentifiers type=\"array\">\n"
      + "    <taxIdentifier>\n"
      + "      <countryCode>US</countryCode>\n"
      + "      <identifier>123</identifier>\n"
      + "    </taxIdentifier>\n"
      + "  </taxIdentifiers>\n"
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
}
