package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        private List<String> ids;
        private int pageSize;
        private int index;
        private int nextIndexToFetch;
        private List<E> items;

        public PagedIterator(ResourceCollection<E> resourceCollection) {
            this.resourceCollection = resourceCollection;
            this.ids = resourceCollection.ids;
            this.pageSize = resourceCollection.pageSize;
            this.index = 0;
            this.nextIndexToFetch = 0;
            this.items = new ArrayList<E>();
        }

        private List<String> nextBatchOfIds() {
            int lastIdIndex = nextIndexToFetch + pageSize;
            if (lastIdIndex > ids.size()) {
                lastIdIndex = ids.size();
            }

            List<String> nextIds = ids.subList(nextIndexToFetch, lastIdIndex);
            nextIndexToFetch = lastIdIndex;

            return nextIds;
        }

        public boolean hasNext() {
            if (nextIndexToFetch < ids.size() && index == items.size()) {
                this.items = resourceCollection.pager.getPage(nextBatchOfIds());
                this.index = 0;
            }

            if (index < items.size()) {
                return true;
            }
            return false;
        }

        public E next() {
            E item = items.get(index);
            index++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private List<String> ids;
    private Pager<T> pager;
    private int pageSize;

    public ResourceCollection(Pager<T> pager, NodeWrapper response) {
        this.pager = pager;
        pageSize = response.findInteger("page-size");
        ids = response.findAllStrings("ids/*");
    }

    /**
     * Returns the approximate total size of the collection.
     *
     * @return Approximate size of collection
     */
    public int getMaximumSize() {
        return ids.size();
    }

    public Iterator<T> iterator() {
        return new PagedIterator<T>(this);
    }

    public T getFirst() {
        return pager.getPage(ids.subList(0, 1)).get(0);
    }

    public List<String> getIds() {
        return ids;
    }
}
