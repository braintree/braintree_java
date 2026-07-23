package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.PaymentMethodThreeDSecurePassThruRequest;
import com.braintreegateway.enums.ThreeDSecurePassThruNetwork;

public class PaymentMethodThreeDSecurePassThruRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        PaymentMethodRequest parent = new PaymentMethodRequest();
        PaymentMethodThreeDSecurePassThruRequest request = new PaymentMethodThreeDSecurePassThruRequest(parent);

        PaymentMethodRequest returned = request
                .cavv("cavv-value")
                .dsTransactionId("ds-transaction-id")
                .eciFlag("05")
                .threeDSecureVersion("2.0.0")
                .xid("xid-value")
                .authenticationResponse("auth-response")
                .directoryResponse("directory-response")
                .cavvAlgorithm("algorithm")
                .network(ThreeDSecurePassThruNetwork.EFTPOS)
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<cavv>cavv-value</cavv>"), xml);
        assertTrue(xml.contains("<dsTransactionId>ds-transaction-id</dsTransactionId>"), xml);
        assertTrue(xml.contains("<eciFlag>05</eciFlag>"), xml);
        assertTrue(xml.contains("<threeDSecureVersion>2.0.0</threeDSecureVersion>"), xml);
        assertTrue(xml.contains("<xid>xid-value</xid>"), xml);
        assertTrue(xml.contains("<authenticationResponse>auth-response</authenticationResponse>"), xml);
        assertTrue(xml.contains("<directoryResponse>directory-response</directoryResponse>"), xml);
        assertTrue(xml.contains("<cavvAlgorithm>algorithm</cavvAlgorithm>"), xml);
        assertTrue(xml.contains("<network>eftpos</network>"), xml);
    }
}
