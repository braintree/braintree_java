package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A collection used to page through results.
 *
 * @param <T>
 *            type of object being paged, e.g. {@link MerchantAccount}
 */
public class PaginatedCollection<T> implements Iterable<T> {

    private class PagedIterator<E> implements Iterator<E> {
        private PaginatedCollection<E> paginatedCollection;
        private int pageSize;
        private int currentPage;
        private int index;
        private int totalSize;
        private List<E> items;

        public PagedIterator(PaginatedCollection<E> paginatedCollection) {
            this.paginatedCollection = paginatedCollection;
            this.pageSize = 0;
            this.currentPage = 0;
            this.index = 0;
            this.totalSize = 0;
            this.items = new ArrayList<E>();
        }

        public boolean hasNext() {
            if (currentPage == 0 || this.index % this.pageSize == 0) {
                this.currentPage++;
                PaginatedResult<E> results = paginatedCollection.pager.getPage(this.currentPage);
                this.totalSize = results.getTotalItems();
                this.items = results.getCurrentPage();
                this.pageSize = results.getPageSize();
            }

            return this.index < this.totalSize;
        }

        public E next() {
            E item = items.get(index % this.pageSize);
            index++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private SimplePager<T> pager;
    private int pageSize;

    public PaginatedCollection(SimplePager<T> pager) {
        this.pager = pager;
    }

    public Iterator<T> iterator() {
        return new PagedIterator<T>(this);
    }
}
