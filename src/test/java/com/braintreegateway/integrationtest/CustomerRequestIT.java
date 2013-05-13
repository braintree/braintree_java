package com.braintreegateway.integrationtest;

import com.braintreegateway.CustomerRequest;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerRequestIT {
    @Test
    public void toXmlIncludesSecurityParams() {
        CustomerRequest request = new CustomerRequest().deviceSessionId("device_session");
        TestHelper.assertIncludes("device_session", request.toXML());
    }
}
