package com.braintreegateway;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.braintreegateway.util.NodeWrapper;

/**
 * A collection used to page through query or search results.
 * 
 * @param <T>
 *            type of object being paged, e.g. {@link Transaction} or
 *            {@link Customer}.
 */
public class ResourceCollection<T> implements Iterable<T> {
    
    private class PagedIterator<E> implements Iterator<E> {
        private ResourceCollection<E> resourceCollection;
        private int index;
        
        public PagedIterator(ResourceCollection<E> resourceCollection) {
            this.resourceCollection = resourceCollection;
            this.index = 0;
        }
        
        public boolean hasNext() {
            if (resourceCollection.isLastPage() && index >= resourceCollection.getItems().size()) {
                return false;
            }
            return true;
        }

        public E next() {
            if (index >= resourceCollection.getItems().size()) {
                this.resourceCollection = resourceCollection.getNextPage();
                index = 0;
            }
            E item = this.resourceCollection.getItems().get(index);
            index++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int currentPageNumber;
    private List<T> items;
    private Pager<T> pager;
    private int pageSize;
    private int totalItems;

    public ResourceCollection(Pager<T> pager, NodeWrapper response, Class<T> klass) {
        this.pager = pager;
        currentPageNumber = response.findInteger("current-page-number");
        pageSize = response.findInteger("page-size");
        totalItems = response.findInteger("total-items");

        items = new ArrayList<T>();
        for (NodeWrapper node : response.findAll(klass.getSimpleName().toLowerCase())) {
            items.add(Result.newInstanceFromNode(klass, node));
        }
    }
    
    /**
     * Returns the approximate total size of the collection.
     * 
     * @return Approximate size of collection
     */
    public int getApproximateSize() {
        return totalItems;
    }

    public Iterator<T> iterator() {
        return new PagedIterator<T>(this);
    }

    public T getFirst() {
        return items.get(0);
    }
    
    /**
     * Returns a list of the items for the current page.
     * 
     * @return List of objects being paged, e.g. {@link Transaction} or
     *         {@link Customer}.
     */
    private List<T> getItems() {
        return items;
    }

    /**
     * Returns the next page of results.
     * 
     * @return {@link ResourceCollection} or null if the current page is the last
     *         page
     */
    private ResourceCollection<T> getNextPage() {
        if (isLastPage()) {
            return null;
        }
        return pager.getPage(currentPageNumber + 1);
    }

    /**
     * Returns the total number of pages.
     */
    private int getTotalPages() {
        if (totalItems == 0) {
            return 1;
        }
        
        int totalPages = totalItems / pageSize;
        if (totalItems % pageSize != 0) {
            totalPages += 1;
        }
        return totalPages;
    }

    /**
     * Returns whether or not this is the last page.
     */
    private boolean isLastPage() {
        return currentPageNumber == getTotalPages();
    }
}
