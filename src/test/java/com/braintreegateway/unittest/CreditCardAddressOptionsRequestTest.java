package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardAddressOptionsRequest;
import com.braintreegateway.CreditCardAddressRequest;
import com.braintreegateway.CreditCardRequest;

public class CreditCardAddressOptionsRequestTest {

    @Test
    public void buildsXmlAndQueryStringAndChainsBackToParent() {
        CreditCardAddressRequest parent = new CreditCardAddressRequest(new CreditCardRequest());
        CreditCardAddressOptionsRequest request = new CreditCardAddressOptionsRequest(parent);

        CreditCardAddressRequest returned = request.updateExisting(true).done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<updateExisting>true</updateExisting>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("options%5Bupdate_existing%5D=true"), queryString);
    }
}
