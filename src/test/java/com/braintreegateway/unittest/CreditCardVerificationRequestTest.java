package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardVerificationRequest;

public class CreditCardVerificationRequestTest {

    @Test
    @SuppressWarnings("deprecation")
    public void buildsXmlWithAllNestedBuilders() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest()
                .intendedTransactionSource("unscheduled")
                .paymentMethodNonce("a-nonce")
                .threeDSecureAuthenticationID("a-3ds-auth-id")
                .threeDSecureToken("a-3ds-token");

        request.creditCard().number("4111111111111111").expirationDate("05/2025").done();
        request.options().amount("10.00").done();
        request.externalVault().willVault().done();
        request.riskData().customerBrowser("Chrome").done();
        request.verificationThreeDSecurePassThruRequest().eciFlag("05").done();

        String xml = request.toXML();
        assertTrue(xml.contains("<verification>"), xml);
        assertTrue(xml.contains("<intendedTransactionSource>unscheduled</intendedTransactionSource>"), xml);
        assertTrue(xml.contains("<paymentMethodNonce>a-nonce</paymentMethodNonce>"), xml);
        assertTrue(xml.contains("<threeDSecureAuthenticationID>a-3ds-auth-id</threeDSecureAuthenticationID>"), xml);
        assertTrue(xml.contains("<threeDSecureToken>a-3ds-token</threeDSecureToken>"), xml);
        assertTrue(xml.contains("<creditCard>"), xml);
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<externalVault>"), xml);
        assertTrue(xml.contains("<riskData>"), xml);
        assertTrue(xml.contains("<threeDSecurePassThru>"), xml);
    }
}
