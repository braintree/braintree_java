package com.braintreegateway;

public class TransactionPager implements Pager<Transaction> {

    private TransactionGateway gateway;
    private String query;

    public TransactionPager(TransactionGateway gateway, String query) {
        this.gateway = gateway;
        this.query = query;
    }

    @Override
    public PagedCollection<Transaction> getPage(int page) {
        return gateway.search(query, page);
    }

}
