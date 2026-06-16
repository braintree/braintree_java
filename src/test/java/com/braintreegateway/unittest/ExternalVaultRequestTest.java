package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.ExternalVaultRequest;
import com.braintreegateway.TransactionRequest;

public class ExternalVaultRequestTest {

    @Test
    public void willVaultBuildsXmlAndChainsBackToParent() {
        TransactionRequest parent = new TransactionRequest();
        ExternalVaultRequest request = new ExternalVaultRequest(parent);

        TransactionRequest returned = request
                .willVault()
                .previousNetworkTransactionId("a-network-txn-id")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<externalVault>"), xml);
        assertTrue(xml.contains("<status>will_vault</status>"), xml);
        assertTrue(xml.contains("<previousNetworkTransactionId>a-network-txn-id</previousNetworkTransactionId>"), xml);
    }

    @Test
    public void vaultedSetsStatus() {
        String xml = new ExternalVaultRequest(new TransactionRequest()).vaulted().toXML();

        assertTrue(xml.contains("<status>vaulted</status>"), xml);
    }
}
