package com.braintreegateway.integrationtest;

import com.braintreegateway.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;

import org.junit.Before;

public class IntegrationTest {

    protected BraintreeGateway gateway;
    protected Transaction disputedTransaction;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration_merchant_id",
            "integration_public_key",
            "integration_private_key"
        );
    }

    protected Transaction createDisputedTransaction() throws InterruptedException {
        TransactionRequest request = new TransactionRequest()
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .creditCard()
            .number(SandboxValues.Dispute.CHARGEBACK)
            .expirationDate("05/2010")
            .done();

        return this.gateway.transaction().sale(request).getTarget();
    }

    protected Transaction getDisputedTransaction() throws InterruptedException {
        if (this.gateway == null) {
            createGateway();
        }

        if (this.disputedTransaction == null) {
            this.disputedTransaction = this.createDisputedTransaction();
        }

        ResourceCollection<Transaction> collection = null;

        Calendar today = Calendar.getInstance();

        Calendar oneDayEarlier = ((Calendar) today.clone());
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = ((Calendar) today.clone());
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest()
            .id().is(this.disputedTransaction.getId())
            .disputeDate().between(oneDayEarlier, oneDayLater);

        for (int i=0; i<90; i++) {
            Thread.sleep(1000);

            collection = gateway.transaction().search(searchRequest);

            if (collection.getMaximumSize() > 0)
                break;
        }

        return this.disputedTransaction;
    }

    @Before
    public void ignoreLogging(){
        if (this.gateway == null) {
            createGateway();
        }

        this.gateway.getConfiguration().setLogger(Logger.getLogger("null"));
        this.gateway.getConfiguration().getLogger().setLevel(Level.INFO);
        this.gateway.getConfiguration().getLogger().setUseParentHandlers(false);
    }
}
