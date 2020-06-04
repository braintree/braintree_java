package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.exceptions.TestOperationPerformedInProductionException;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestingGatewayIT implements MerchantAccountTestConstants {

    @Test(expected = TestOperationPerformedInProductionException.class)
    public void testSettleRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        gateway.testing().settle("transaction_id");
    }

    @Test(expected = TestOperationPerformedInProductionException.class)
    public void testSettlementConfirmRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        gateway.testing().settlementConfirm("transaction_id");
    }

    @Test(expected = TestOperationPerformedInProductionException.class)
    public void testSettlementDeclineRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        gateway.testing().settlementDecline("transaction_id");
    }

    @Test(expected = TestOperationPerformedInProductionException.class)
    public void testSettlementPendingRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        gateway.testing().settlementPending("transaction_id");
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
