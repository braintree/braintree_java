package com.braintreegateway.integrationtest;

import com.braintreegateway.TransactionRequest;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionRequestIT {
    @Test
    public void toQueryStringWithNestedCustomer() {
        TransactionRequest request = new TransactionRequest().
            customer().
                firstName("Drew").
                done();

        assertEquals("transaction%5Bcustomer%5D%5Bfirst_name%5D=Drew", request.toQueryString());
    }

    @Test
    public void customFieldsEscapesKeysAndValues() {
        TransactionRequest request = new TransactionRequest().customField("ke&y", "va<lue");
        TestHelper.assertIncludes("<customFields><ke&amp;y>va&lt;lue</ke&amp;y></customFields>", request.toXML());
    }

    @Test
    public void toXmlIncludesSecurityParams() {
        TransactionRequest request = new TransactionRequest().deviceSessionId("device_session");
        TestHelper.assertIncludes("device_session", request.toXML());
    }

    @Test
    public void toXmlIncludesBundle() {
        TransactionRequest request = new TransactionRequest().deviceData("{\"device_session_id\": \"mydsid\"}");
        TestHelper.assertIncludes("mydsid", request.toXML());
    }
}
