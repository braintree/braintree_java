package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class TestingGateway {
    private Http http;

    public TestingGateway(Http http) {
        this.http = http;
    }

    public Result<Transaction> settle(String transactionId) {
        NodeWrapper node = this.http.put("/transactions/" + transactionId + "/settle");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementConfirm(String transactionId) {
        NodeWrapper node = this.http.put("/transactions/" + transactionId + "/settlement_confirm");
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> settlementDecline(String transactionId) {
        NodeWrapper node = this.http.put("/transactions/" + transactionId + "/settlement_decline");
        return new Result<Transaction>(node, Transaction.class);
    }
}
