package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardVerificationRequest;
import com.braintreegateway.RiskDataVerificationRequest;

public class RiskDataVerificationRequestTest {

    @Test
    public void buildsXmlQueryStringAndChainsBackToParent() {
        CreditCardVerificationRequest parent = new CreditCardVerificationRequest();
        RiskDataVerificationRequest request = new RiskDataVerificationRequest(parent);

        CreditCardVerificationRequest returned = request
                .customerBrowser("Chrome")
                .customerIP("127.0.0.1")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<riskData>"), xml);
        assertTrue(xml.contains("<customerBrowser>Chrome</customerBrowser>"), xml);
        assertTrue(xml.contains("<customerIP>127.0.0.1</customerIP>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("risk_data%5Bcustomer_browser%5D=Chrome"), queryString);
        assertTrue(queryString.contains("risk_data%5Bcustomer_ip%5D=127.0.0.1"), queryString);
    }

    @Test
    public void noArgConstructorBuildsXml() {
        String xml = new RiskDataVerificationRequest().customerBrowser("Firefox").toXML();
        assertTrue(xml.contains("<customerBrowser>Firefox</customerBrowser>"), xml);
    }
}
