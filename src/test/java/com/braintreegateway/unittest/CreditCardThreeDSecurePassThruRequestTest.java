package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.CreditCardThreeDSecurePassThruRequest;

public class CreditCardThreeDSecurePassThruRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        CreditCardRequest parent = new CreditCardRequest();
        CreditCardThreeDSecurePassThruRequest request = new CreditCardThreeDSecurePassThruRequest(parent);

        CreditCardRequest returned = request
                .cavv("cavv-value")
                .dsTransactionId("ds-transaction-id")
                .eciFlag("05")
                .threeDSecureVersion("2.0.0")
                .xid("xid-value")
                .authenticationResponse("auth-response")
                .directoryResponse("directory-response")
                .cavvAlgorithm("algorithm")
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
    }
}
