package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.util.NodeWrapper;

/**
 * A collection used to page through query or search results.
 * 
 * @param <T>
 *            type of object being paged, e.g. {@link Transaction} or
 *            {@link Customer}.
 */
public class PagedCollection<T> {

    private int currentPageNumber;
    private List<T> items;
    private Pager<T> pager;
    private int pageSize;
    private int totalItems;

    public PagedCollection(Pager<T> pager, NodeWrapper response, Class<T> klass) {
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
     * Returns the page the collection is currently on.
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    /**
     * Returns the number of objects on the current page.
     * 
     * @see #getPageSize()
     */
    public int getCurrentPageSize() {
        return items.size();
    }

    /**
     * Returns a list of the items for the current page.
     * 
     * @return List of objects being paged, e.g. {@link Transaction} or
     *         {@link Customer}.
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Returns the next page of results.
     * 
     * @return {@link PagedCollection} or null if the current page is the last
     *         page
     */
    public PagedCollection<T> getNextPage() {
        if (isLastPage()) {
            return null;
        }
        return pager.getPage(currentPageNumber + 1);
    }

    /**
     * Returns the number of objects on each page. This value is always fixed,
     * and does not change for pages with less items.
     * 
     * @see #getCurrentPageSize()
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Returns the total number of items on all pages.
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * Returns the total number of pages.
     */
    public int getTotalPages() {
        int totalPages = totalItems / pageSize;
        if (totalItems % pageSize != 0) {
            totalPages += 1;
        }
        return totalPages;
    }

    /**
     * Returns whether or not this is the last page.
     */
    public boolean isLastPage() {
        return currentPageNumber == getTotalPages();
    }
}
