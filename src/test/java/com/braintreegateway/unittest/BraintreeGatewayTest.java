package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.BankAccountInstantVerificationGateway;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

public class BraintreeGatewayTest {

    private BraintreeGateway gateway() {
        return new BraintreeGateway(Environment.DEVELOPMENT, "merchant_id", "public_key", "private_key");
    }

    @Test
    public void testStringEnvironmentConstructor() {
       BraintreeGateway gateway = new BraintreeGateway("development", "merchant_id", "public_key", "private_key");
       assertEquals(Environment.DEVELOPMENT, gateway.getConfiguration().getEnvironment());
    }

    @Test
    public void clientIdAndClientSecretConstructor() {
        BraintreeGateway gateway = new BraintreeGateway(
                "client_id$development$integration_client_id",
                "client_secret$development$integration_client_secret");
        assertEquals("client_id$development$integration_client_id", gateway.getConfiguration().getClientId());
        assertEquals("client_secret$development$integration_client_secret", gateway.getConfiguration().getClientSecret());
    }

    @Test
    public void accessTokenConstructor() {
        BraintreeGateway gateway = new BraintreeGateway("access_token$development$integration_merchant_id$4bff9793ed");
        assertEquals("access_token$development$integration_merchant_id$4bff9793ed", gateway.getConfiguration().getAccessToken());
        assertEquals(Environment.DEVELOPMENT, gateway.getConfiguration().getEnvironment());
    }

    @Test
    public void partnerIdAlias() {
        BraintreeGateway gateway = BraintreeGateway.forPartner(Environment.DEVELOPMENT, "partner_id", "publicKey", "privateKey");
        assertEquals("publicKey", gateway.getConfiguration().getPublicKey());
        assertEquals("privateKey", gateway.getConfiguration().getPrivateKey());
    }

    @Test
    public void setProxyConfiguresProxyOnConfiguration() {
        BraintreeGateway gateway = gateway();
        assertFalse(gateway.getConfiguration().usesProxy());

        gateway.setProxy("localhost", 3000);

        assertTrue(gateway.getConfiguration().usesProxy());
        assertNotNull(gateway.getConfiguration().getProxy());
    }

    @Test
    public void bankAccountInstantVerificationReturnsGatewayInstance() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "merchant_id", "public_key", "private_key");
        BankAccountInstantVerificationGateway bankAccountInstantVerificationGateway = gateway.bankAccountInstantVerification();

        assertNotNull(bankAccountInstantVerificationGateway);
        assertSame(BankAccountInstantVerificationGateway.class, bankAccountInstantVerificationGateway.getClass());
    }

    @Test
    public void gatewayAccessorsReturnInstances() {
        BraintreeGateway gateway = gateway();

        assertAll("gateway accessors",
                () -> assertNotNull(gateway.address(), "address"),
                () -> assertNotNull(gateway.creditCardVerification(), "creditCardVerification"),
                () -> assertNotNull(gateway.usBankAccountVerification(), "usBankAccountVerification"),
                () -> assertNotNull(gateway.customer(), "customer"),
                () -> assertNotNull(gateway.customerSession(), "customerSession"),
                () -> assertNotNull(gateway.localPaymentContext(), "localPaymentContext"),
                () -> assertNotNull(gateway.discount(), "discount"),
                () -> assertNotNull(gateway.dispute(), "dispute"),
                () -> assertNotNull(gateway.paymentMethodNonce(), "paymentMethodNonce"),
                () -> assertNotNull(gateway.paypalAccount(), "paypalAccount"),
                () -> assertNotNull(gateway.paypalPaymentResource(), "paypalPaymentResource"),
                () -> assertNotNull(gateway.sepaDirectDebitAccount(), "sepaDirectDebitAccount"),
                () -> assertNotNull(gateway.usBankAccount(), "usBankAccount"),
                () -> assertNotNull(gateway.plan(), "plan"),
                () -> assertNotNull(gateway.settlementBatchSummary(), "settlementBatchSummary"),
                () -> assertNotNull(gateway.subscription(), "subscription"),
                () -> assertNotNull(gateway.transaction(), "transaction"),
                () -> assertNotNull(gateway.transactionLineItem(), "transactionLineItem"),
                () -> assertNotNull(gateway.webhookNotification(), "webhookNotification"),
                () -> assertNotNull(gateway.webhookTesting(), "webhookTesting"),
                () -> assertNotNull(gateway.merchantAccount(), "merchantAccount"),
                () -> assertNotNull(gateway.oauth(), "oauth"),
                () -> assertNotNull(gateway.testing(), "testing"),
                () -> assertNotNull(gateway.documentUpload(), "documentUpload"),
                () -> assertNotNull(gateway.report(), "report"),
                () -> assertNotNull(gateway.exchangeRateQuote(), "exchangeRateQuote"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void merchantReturnsGatewayInstance() {
        BraintreeGateway gateway = gateway();
        assertNotNull(gateway.merchant());
    }
}
