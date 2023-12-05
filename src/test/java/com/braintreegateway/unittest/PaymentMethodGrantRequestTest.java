package com.braintreegateway.unittest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.braintreegateway.PaymentMethodGrantRequest;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import org.junit.jupiter.api.Test;

public class PaymentMethodGrantRequestTest {
  @Test
  public void toXMLIncludesAllElements() throws IOException, SAXException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    PaymentMethodGrantRequest request = new PaymentMethodGrantRequest();

    Method method = PaymentMethodGrantRequest.class.getDeclaredMethod("sharedPaymentMethodToken", String.class);
    method.setAccessible(true);
    method.invoke(request, "shared_payment_method_token");

    request
      .allowVaulting(true)
      .externalNetworkTokenizationEnrollmentId("123456789")
      .includeBillingPostalCode(false)
      .revokeAfter("12-12-2024");

    String expectedXML = "<payment-method>\n"
                         + "  <shared-payment-method-token>shared_payment_method_token</shared-payment-method-token>\n"
                         + "  <external-network-tokenization-enrollment-id>123456789</external-network-tokenization-enrollment-id>\n"
                         + "  <allow-vaulting>true</allow-vaulting>\n"
                         + "  <include-billing-postal-code>false</include-billing-postal-code>\n"
                         + "  <revoke-after>12-12-2024</revoke-after>\n"
                         + "</payment-method>";

    XMLUnit.setIgnoreWhitespace(true);
    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}
