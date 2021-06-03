package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.exceptions.TestOperationPerformedInProductionException;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestingGatewayIT implements MerchantAccountTestConstants {

    @Test
    public void testSettleRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        assertThrows(TestOperationPerformedInProductionException.class, () -> {
            gateway.testing().settle("transaction_id");
        });
    }

    @Test
    public void testSettlementConfirmRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        assertThrows(TestOperationPerformedInProductionException.class, () -> {
            gateway.testing().settlementConfirm("transaction_id");
        });
    }

    @Test
    public void testSettlementDeclineRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        assertThrows(TestOperationPerformedInProductionException.class, () -> {
            gateway.testing().settlementDecline("transaction_id");
        });
    }

    @Test
    public void testSettlementPendingRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        assertThrows(TestOperationPerformedInProductionException.class, () -> {
            gateway.testing().settlementPending("transaction_id");
        });
    }

    @Test
    public void testSettlementPendingReturnsSettlementPendingTransaction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");

        String nonce = TestHelper.generateUnlockedNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(nonce).
            options().
                submitForSettlement(true).
                done();
        Result<Transaction> result = gateway.transaction().sale(request);

        result = gateway.testing().settlementPending(result.getTarget().getId());
        assertEquals(result.getTarget().getStatus(), Transaction.Status.SETTLEMENT_PENDING);
    }
}
