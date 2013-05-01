package com.braintreegateway;

import java.util.List;

public class TransactionPager implements Pager<Transaction> {
    private TransactionGateway gateway;
    private TransactionSearchRequest query;

    public TransactionPager(TransactionGateway gateway, TransactionSearchRequest query) {
        this.gateway = gateway;
        this.query = query;
    }
    
    public List<Transaction> getPage(List<String> ids) {
        return gateway.fetchTransactions(query, ids);
    }
}
