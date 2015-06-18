package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.TestOperationPerformedInProductionException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class TestingGateway {
    private Http http;
    private Environment environment;

    public TestingGateway(Http http, Environment environment) {
        this.environment = environment;
        this.http = http;
    }

    public TestingGateway(BraintreeGateway gateway, Environment environment) {
        this(new Http(gateway.getConfiguration()), environment);
    }

    public Result<Transaction> settle(String transactionId) {
        checkEnvironment();
        NodeWrapper node = this.http.put("/transactions/" + transactionId + "/settle");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementConfirm(String transactionId) {
        checkEnvironment();
        NodeWrapper node = this.http.put("/transactions/" + transactionId + "/settlement_confirm");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementDecline(String transactionId) {
        checkEnvironment();
        NodeWrapper node = this.http.put("/transactions/" + transactionId + "/settlement_decline");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementPending(String transactionId) {
        checkEnvironment();
        NodeWrapper node = this.http.put("/transactions/" + transactionId + "/settlement_pending");
        return new Result<Transaction>(node, Transaction.class);
    }

    private void checkEnvironment() throws TestOperationPerformedInProductionException {
        if (this.environment == Environment.PRODUCTION) {
          throw new TestOperationPerformedInProductionException();
        }
    }
}
