package com.braintreegateway.unittest;

import com.braintreegateway.TransactionVoidRequest;
import com.braintreegateway.testhelpers.TestHelper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionVoidRequestTest {
    @Test
    public void toXmlIncludesApiRequestKey() {
        TransactionVoidRequest request = new TransactionVoidRequest().apiRequestKey("test-api-key-123");
        TestHelper.assertIncludes("<api-request-key>test-api-key-123</api-request-key>", request.toXML());
    }

    @Test
    public void toXmlExcludesApiRequestKeyWhenNull() {
        TransactionVoidRequest request = new TransactionVoidRequest().apiRequestKey(null);
        assertFalse(request.toXML().contains("api-request-key"));
    }

    @Test
    public void toXmlCreatesValidXml() {
        TransactionVoidRequest request = new TransactionVoidRequest().apiRequestKey("void-key-789");
        String xml = request.toXML();
        TestHelper.assertIncludes("<transaction>", xml);
        TestHelper.assertIncludes("</transaction>", xml);
        TestHelper.assertIncludes("<api-request-key>void-key-789</api-request-key>", xml);
    }

    @Test
    public void toXmlEmptyRequestCreatesMinimalXml() {
        TransactionVoidRequest request = new TransactionVoidRequest();
        String xml = request.toXML();
        TestHelper.assertIncludes("<transaction>", xml);
        TestHelper.assertIncludes("</transaction>", xml);
        assertFalse(xml.contains("api-request-key"));
    }
}
