package com.braintreegateway;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.util.NodeWrapper;

public class PagedCollectionTest {

    @Test
    public void totalPagesForEvenlyDivisibleTotal() {
        PagedCollection<Transaction> pagedCollection = new PagedCollection<Transaction>(null, xmlFor(1, 20, 5), Transaction.class);
        Assert.assertEquals(4, pagedCollection.getTotalPages());
    }

    @Test
    public void totalPagesForNonEvenlyDivisibleTotal() {
        PagedCollection<Transaction> pagedCollection = new PagedCollection<Transaction>(null, xmlFor(1, 13, 5), Transaction.class);
        Assert.assertEquals(3, pagedCollection.getTotalPages());
    }

    @Test
    public void isLastPageOnLastPage() {
        PagedCollection<Transaction> pagedCollection = new PagedCollection<Transaction>(null, xmlFor(3, 150, 50), Transaction.class);
        Assert.assertTrue(pagedCollection.isLastPage());
    }

    @Test
    public void isLastPageNotOnLastPage() {
        PagedCollection<Transaction> pagedCollection = new PagedCollection<Transaction>(null, xmlFor(3, 151, 50), Transaction.class);
        Assert.assertFalse(pagedCollection.isLastPage());
    }

    @Test
    public void getNextPageReturnsNullOnLastPage() {
        PagedCollection<Transaction> pagedCollection = new PagedCollection<Transaction>(null, xmlFor(1, 50, 50), Transaction.class);
        Assert.assertNull(pagedCollection.getNextPage());
    }
    
    private NodeWrapper xmlFor(int currentPageNumber, int totalItems, int pageSize) {
        String xml = "<top-level><current-page-number>" + currentPageNumber + "</current-page-number><total-items>"
                + totalItems + "</total-items><page-size>" + pageSize + "</page-size></top-level>";
        return new NodeWrapper(xml);
    }
}
