package com.braintreegateway;

public class AdvancedTransactionPager implements Pager<Transaction> {
    private TransactionGateway gateway;
    private TransactionSearchRequest query;

    public AdvancedTransactionPager(TransactionGateway gateway, TransactionSearchRequest query) {
        this.gateway = gateway;
        this.query = query;
    }
    
    public ResourceCollection<Transaction> getPage(int page) {
        return gateway.search(query, page);
    }
}
