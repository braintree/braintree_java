package com.braintreegateway;

public interface SimplePager<T> {
    public PaginatedResult<T> getPage(int page);
}
