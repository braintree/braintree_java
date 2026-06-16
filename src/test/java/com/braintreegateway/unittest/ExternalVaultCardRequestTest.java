package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.ExternalVaultCardRequest;

public class ExternalVaultCardRequestTest {

    @Test
    public void buildsXmlAndChainsBackToParent() {
        CreditCardRequest parent = new CreditCardRequest();
        ExternalVaultCardRequest request = new ExternalVaultCardRequest(parent);

        CreditCardRequest returned = request
                .networkTransactionId("a-network-txn-id")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<externalVault>"), xml);
        assertTrue(xml.contains("<networkTransactionId>a-network-txn-id</networkTransactionId>"), xml);
    }
}
