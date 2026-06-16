package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CustomerRequest;
import com.braintreegateway.TaxIdentifierRequest;

public class TaxIdentifierRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        CustomerRequest parent = new CustomerRequest();
        TaxIdentifierRequest request = new TaxIdentifierRequest(parent);

        CustomerRequest returned = request
                .countryCode("US")
                .identifier("tax-id-123")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<taxIdentifier>"), xml);
        assertTrue(xml.contains("<countryCode>US</countryCode>"), xml);
        assertTrue(xml.contains("<identifier>tax-id-123</identifier>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("tax_identifier%5Bcountry_code%5D=US"), queryString);
    }

    @Test
    public void noArgConstructorBuildsXml() {
        String xml = new TaxIdentifierRequest()
                .countryCode("US")
                .identifier("tax-id-123")
                .toXML();

        assertTrue(xml.contains("<countryCode>US</countryCode>"), xml);
    }
}
