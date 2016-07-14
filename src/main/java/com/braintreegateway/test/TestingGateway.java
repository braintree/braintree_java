package com.braintreegateway.test;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.TestOperationPerformedInProductionException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class TestingGateway {
    private Http http;
    private Configuration configuration;

    public TestingGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }

    public Result<Transaction> settle(String transactionId) {
        checkEnvironment();
        NodeWrapper node = http.put(configuration.getMerchantPath() + "/transactions/" + transactionId + "/settle");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementConfirm(String transactionId) {
        checkEnvironment();
        NodeWrapper node = http.put(configuration.getMerchantPath() + "/transactions/" + transactionId + "/settlement_confirm");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementDecline(String transactionId) {
        checkEnvironment();
        NodeWrapper node = http.put(configuration.getMerchantPath() + "/transactions/" + transactionId + "/settlement_decline");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementPending(String transactionId) {
        checkEnvironment();
        NodeWrapper node = http.put(configuration.getMerchantPath() + "/transactions/" + transactionId + "/settlement_pending");
        return new Result<Transaction>(node, Transaction.class);
    }

    private void checkEnvironment() throws TestOperationPerformedInProductionException {
        if (configuration.getEnvironment() == Environment.PRODUCTION) {
          throw new TestOperationPerformedInProductionException();
        }
    }
}
