package com.braintreegateway;

import java.util.List;

public interface Pager<T> {
    public List<T> getPage(List<String> ids);
}
