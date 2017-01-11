package com.braintreegateway;

import java.util.List;

public interface SimplePager<T> {
    public PaginatedResult<T> getPage(int page);
}
