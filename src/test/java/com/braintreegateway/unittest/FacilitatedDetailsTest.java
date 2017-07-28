package com.braintreegateway.unittest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.braintreegateway.FacilitatedDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class FacilitatedDetailsTest {
    @Test
    public void includesFields() {
        String nonce = "4004b83f-2559-0d79-2de0-c096d1ed9b92";
        String xml = "<facilitated-details>" +
            "<merchant-id>abc123</merchant-id>" +
            "<merchant-name>Cool Store</merchant-name>" +
            "<payment-method-nonce>" + nonce + "</payment-method-nonce>" +
            "</facilitated-details>";
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

        FacilitatedDetails details = new FacilitatedDetails(node);

        assertEquals("abc123", details.getMerchantId());
        assertEquals("Cool Store", details.getMerchantName());
        assertEquals(nonce, details.getPaymentMethodNonce());
    }
}
