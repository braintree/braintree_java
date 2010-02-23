package com.braintreegateway.util;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.util.TrUtil;

public class TrUtilTest {

    @Test
    public void buildTrData() {
        CreditCardRequest request = new CreditCardRequest().customerId("123");
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        String trData = new TrUtil(configuration).buildTrData(request, "http://example.com");
        Assert.assertTrue(new TrUtil(configuration).validateTrData(trData));
    }
}
