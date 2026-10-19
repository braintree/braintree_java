package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionReverseRequest;
import com.braintreegateway.testhelpers.TestHelper;

public class TransactionReverseRequestTest {
    @Test
    public void toXmlIncludesApiRequestKey() {
        TransactionReverseRequest request = new TransactionReverseRequest().apiRequestKey("reverse-key-789");

        TestHelper.assertIncludes("<api-request-key>reverse-key-789</api-request-key>", request.toXML());
    }

    @Test
    public void toXmlExcludesApiRequestKeyWhenNull() {
        TransactionReverseRequest request = new TransactionReverseRequest().apiRequestKey(null);

        assertFalse(request.toXML().contains("api-request-key"));
    }

    @Test
    public void toXmlEmptyRequestCreatesMinimalXml() {
        String xml = new TransactionReverseRequest().toXML();

        TestHelper.assertIncludes("<transaction>", xml);
        TestHelper.assertIncludes("</transaction>", xml);
        assertFalse(xml.contains("api-request-key"));
    }
}
