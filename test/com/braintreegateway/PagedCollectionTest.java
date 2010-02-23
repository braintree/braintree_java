package com.braintreegateway;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

public class PagedCollectionTest {

    @Test
    public void totalPagesForEvenlyDivisibleTotal() {
        PagedCollection pagedCollection = new PagedCollection(null, null, null, 1, 20, 5);
        Assert.assertEquals(4, pagedCollection.getTotalPages());
    }

    @Test
    public void totalPagesForNonEvenlyDivisibleTotal() {
        PagedCollection pagedCollection = new PagedCollection(null, null, null, 1, 13, 5);
        Assert.assertEquals(3, pagedCollection.getTotalPages());
    }

    @Test
    public void isLastPageOnLastPage() {
        PagedCollection pagedCollection = new PagedCollection(null, null, null, 3, 150, 50);
        Assert.assertTrue(pagedCollection.isLastPage());
    }

    @Test
    public void isLastPageNotOnLastPage() {
        PagedCollection pagedCollection = new PagedCollection(null, null, null, 3, 151, 50);
        Assert.assertFalse(pagedCollection.isLastPage());
    }

    @Test
    public void getNextPageReturnsNullOnLastPage() {
        PagedCollection pagedCollection = new PagedCollection(null, null, new ArrayList<Transaction>(), 1, 50, 50);
        Assert.assertNull(pagedCollection.getNextPage());
    }
}
