package com.braintreegateway;

public interface Pager<T> {
    public ResourceCollection<T> getPage(int page);
}
