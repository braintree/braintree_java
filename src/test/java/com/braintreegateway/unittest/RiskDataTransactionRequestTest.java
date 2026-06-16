package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.RiskDataTransactionRequest;
import com.braintreegateway.TransactionRequest;

public class RiskDataTransactionRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        TransactionRequest parent = new TransactionRequest();
        RiskDataTransactionRequest request = new RiskDataTransactionRequest(parent);

        TransactionRequest returned = request
                .customerBrowser("Chrome")
                .customerDeviceId("device-id")
                .customerIP("127.0.0.1")
                .customerLocationZip("60622")
                .customerTenure(12)
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertAll("risk data xml",
                () -> assertTrue(xml.contains("<riskData>"), xml),
                () -> assertTrue(xml.contains("<customerBrowser>Chrome</customerBrowser>"), xml),
                () -> assertTrue(xml.contains("<customerDeviceId>device-id</customerDeviceId>"), xml),
                () -> assertTrue(xml.contains("<customerIP>127.0.0.1</customerIP>"), xml),
                () -> assertTrue(xml.contains("<customerLocationZip>60622</customerLocationZip>"), xml),
                () -> assertTrue(xml.contains("<customerTenure>12</customerTenure>"), xml));

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("Chrome"), queryString);
    }

    @Test
    public void noArgConstructorBuildsXml() {
        String xml = new RiskDataTransactionRequest().customerBrowser("Chrome").toXML();

        assertTrue(xml.contains("<riskData>"), xml);
        assertTrue(xml.contains("<customerBrowser>Chrome</customerBrowser>"), xml);
    }
}
