package com.braintreegateway;

public class DisputePager implements SimplePager<Dispute> {
    private DisputeGateway gateway;
    private DisputeSearchRequest query;

    public DisputePager(DisputeGateway gateway, DisputeSearchRequest query) {
        this.gateway = gateway;
        this.query = query;
    }

    public PaginatedResult<Dispute> getPage(int page) {
        return gateway.fetchDisputes(query, page);
    }
}
