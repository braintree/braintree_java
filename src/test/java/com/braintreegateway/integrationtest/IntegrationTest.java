package com.braintreegateway.integrationtest;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.SandboxValues;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.TransactionSearchRequest;

import org.junit.Before;

public class IntegrationTest {

    protected BraintreeGateway gateway;
    protected Transaction disputedTransaction;

    protected static OutputStream logCapturingStream;
    protected static StreamHandler customLogHandler;


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

    protected void attachLogCapturer()
    {
        Configuration configuration = this.gateway.getConfiguration();
        Logger logger = configuration.getLogger();
        logCapturingStream = new ByteArrayOutputStream();
        Handler[] handlers = logger.getParent().getHandlers();
        customLogHandler = new StreamHandler(logCapturingStream, handlers[0].getFormatter());
        customLogHandler.setLevel(Level.FINE);
        logger.addHandler(customLogHandler);
    }

    protected String getTestCapturedLog()
    {
      customLogHandler.flush();
      return logCapturingStream.toString();
    }
}
