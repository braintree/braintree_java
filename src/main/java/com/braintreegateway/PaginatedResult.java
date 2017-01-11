package com.braintreegateway;

import java.util.List;

public class PaginatedResult<T> {
    private int totalItems;
    private int pageSize;
    private List<T> currentPage;

    public PaginatedResult(int totalItems, int pageSize, List<T> currentPage) {
        this.totalItems = totalItems;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getCurrentPage() {
        return currentPage;
    }
}
