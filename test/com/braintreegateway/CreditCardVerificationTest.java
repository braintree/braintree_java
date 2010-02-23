package com.braintreegateway;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.util.NodeWrapper;


public class CreditCardVerificationTest {
    @Test
    public void constructFromResponse() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <verification>");
        builder.append("    <avs-error-response-code nil=\"true\"></avs-error-response-code>");
        builder.append("    <avs-postal-code-response-code>I</avs-postal-code-response-code>");
        builder.append("    <status>processor_declined</status>");
        builder.append("    <processor-response-code>2000</processor-response-code>");
        builder.append("    <avs-street-address-response-code>I</avs-street-address-response-code>");
        builder.append("    <processor-response-text>Do Not Honor</processor-response-text>");
        builder.append("    <cvv-response-code>M</cvv-response-code>");
        builder.append("  </verification>");
        builder.append("  <errors>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");

        CreditCardVerification verification = new CreditCardVerification(new NodeWrapper(builder.toString()));
        Assert.assertEquals(null, verification.getAvsErrorResponseCode());
        Assert.assertEquals("I", verification.getAvsPostalCodeResponseCode());
        Assert.assertEquals("processor_declined", verification.getStatus());
        Assert.assertEquals("2000", verification.getProcessorResponseCode());
        Assert.assertEquals("I", verification.getAvsStreetAddressResponseCode());
        Assert.assertEquals("Do Not Honor", verification.getProcessorResponseText());
        Assert.assertEquals("M", verification.getCvvResponseCode());
    }

    @Test
    public void constructFromResponseWithNoVerification() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <errors>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");

        CreditCardVerification verification = new CreditCardVerification(new NodeWrapper(builder.toString()));
        Assert.assertEquals(null, verification.getAvsErrorResponseCode());
        Assert.assertEquals(null, verification.getAvsPostalCodeResponseCode());
        Assert.assertEquals(null, verification.getStatus());
        Assert.assertEquals(null, verification.getProcessorResponseCode());
        Assert.assertEquals(null, verification.getAvsStreetAddressResponseCode());
        Assert.assertEquals(null, verification.getProcessorResponseText());
        Assert.assertEquals(null, verification.getCvvResponseCode());
    }
}
