package com.braintreegateway.integrationtest;

import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreditCardRequestIT {
    @Test
    public void toXmlEscapesXmlChars() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Special Xml Chars <>&\"'");
        assertEquals("<creditCard><cardholderName>Special Xml Chars &lt;&gt;&amp;&quot;&apos;</cardholderName></creditCard>", request.toXML());
    }

    @Test
    public void toXmlIncludesBundle() {
        CreditCardRequest request = new CreditCardRequest().
            deviceData("{\"deviceSessionId\": \"dsid_abc123\", \"fraudMerchantId\": \"fmid_456\"}");

        TestHelper.assertIncludes("dsid_abc123", request.toXML());
        TestHelper.assertIncludes("fmid_456", request.toXML());
    }

    @Test
    public void toXmlIncludesSecurityParams() {
        CreditCardRequest request = new CreditCardRequest().
          deviceSessionId("dsid_abc123").
          fraudMerchantId("fmid_456");

        TestHelper.assertIncludes("dsid_abc123", request.toXML());
        TestHelper.assertIncludes("fmid_456", request.toXML());
    }

    @Test
    public void toQueryString() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Drew").
            billingAddress().
                region("Chicago").
                done();
        assertEquals("credit_card%5Bbilling_address%5D%5Bregion%5D=Chicago&credit_card%5Bcardholder_name%5D=Drew", request.toQueryString());
    }

    @Test
    public void toQueryStringWithParent() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Drew").
            billingAddress().
                region("Chicago").
                done();
        assertEquals("customer%5Bcredit_card%5D%5Bbilling_address%5D%5Bregion%5D=Chicago&customer%5Bcredit_card%5D%5Bcardholder_name%5D=Drew", request.toQueryString("customer[credit_card]"));
    }
}
