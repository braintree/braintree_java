package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PaymentMethodAddressOptionsRequest;
import com.braintreegateway.PaymentMethodAddressRequest;
import com.braintreegateway.PaymentMethodOptionsRequest;
import com.braintreegateway.PaymentMethodRequest;

public class PaymentMethodAddressOptionsRequestTest {

    @Test
    public void buildsXmlAndQueryStringAndChainsBackToParent() {
        PaymentMethodAddressRequest parent = new PaymentMethodAddressRequest(new PaymentMethodRequest());
        PaymentMethodAddressOptionsRequest request = new PaymentMethodAddressOptionsRequest(parent);

        PaymentMethodAddressRequest returned = request.updateExisting(true).done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<updateExisting>true</updateExisting>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("update_existing"), queryString);
    }
}
