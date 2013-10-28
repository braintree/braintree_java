package com.braintreegateway.integrationtest;

import com.braintreegateway.CustomerRequest;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerRequestIT {
    @Test
    public void toXmlIncludesBundle() {
        CustomerRequest request = new CustomerRequest().
            deviceData("{\"device_session_id\": \"devicesession123\", \"fraud_merchant_id\": \"fraudmerchant456\"}");

        TestHelper.assertIncludes("devicesession123", request.toXML());
        TestHelper.assertIncludes("fraudmerchant456", request.toXML());
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
