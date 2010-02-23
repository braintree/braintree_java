package com.braintreegateway;

import java.util.List;

public class PagedCollection {

    private int currentPageNumber;
    private int pageSize;
    private String query;
    private int totalItems;
    private TransactionGateway transactionGateway;
    private List<Transaction> transactions;

    public PagedCollection(TransactionGateway transactionGateway, String query, List<Transaction> transactions,
            int currentPageNumber, int totalItems, int pageSize) {
        this.transactionGateway = transactionGateway;
        this.query = query;
        this.transactions = transactions;
        this.currentPageNumber = currentPageNumber;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public List<Transaction> getItems() {
        return transactions;
    }

    public PagedCollection getNextPage() {
        if (isLastPage()) {
            return null;
        }
        return transactionGateway.search(query, currentPageNumber + 1);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        int totalPages = totalItems / pageSize;
        if (totalItems % pageSize != 0) {
            totalPages += 1;
        }
        return totalPages;
    }

    public boolean isLastPage() {
        return currentPageNumber == getTotalPages();
    }
}
