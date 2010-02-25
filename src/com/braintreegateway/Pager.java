package com.braintreegateway;

public interface Pager<T> {
    public PagedCollection<T> getPage(int page);
}
