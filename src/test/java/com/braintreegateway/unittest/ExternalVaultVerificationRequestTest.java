package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardVerificationRequest;
import com.braintreegateway.ExternalVault;
import com.braintreegateway.ExternalVaultVerificationRequest;

public class ExternalVaultVerificationRequestTest {

    @Test
    public void willVaultBuildsXmlAndChainsBackToParent() {
        CreditCardVerificationRequest parent = new CreditCardVerificationRequest();
        ExternalVaultVerificationRequest request = new ExternalVaultVerificationRequest(parent);

        CreditCardVerificationRequest returned = request
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
        String xml = new ExternalVaultVerificationRequest(new CreditCardVerificationRequest()).vaulted().toXML();

        assertTrue(xml.contains("<status>vaulted</status>"), xml);
    }

    @Test
    public void statusSetterSetsStatus() {
        String xml = new ExternalVaultVerificationRequest(new CreditCardVerificationRequest())
                .status(ExternalVault.Status.WILL_VAULT)
                .toXML();

        assertTrue(xml.contains("<status>will_vault</status>"), xml);
    }
}
