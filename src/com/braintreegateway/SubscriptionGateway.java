package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

/**
 * Provides methods to interact with {@link Subscription Subscriptions}.
 * Including create, find, update, cancel, etc.
 * This class does not need to be instantiated directly.  
 * Instead, use {@link BraintreeGateway#subscription()} to get an instance of this class:
 * 
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.subscription().create(...)
 * </pre>
 */
public class SubscriptionGateway {

    private Http http;

    public SubscriptionGateway(Http http) {
        this.http = http;
    }

    /**
     * Cancels the {@link Subscription} with the given id.
     * @param id of the {@link Subscription} to cancel.
     * @return a {@link Result}.
     */
    public Result<Subscription> cancel(String id) {
        NodeWrapper node = http.put("/subscriptions/" + id + "/cancel");
        return new Result<Subscription>(node, Subscription.class);
    }

    /**
     * Creates a {@link Subscription}.
     * @param request the request.
     * @return a {@link Result}.
     */
    public Result<Subscription> create(SubscriptionRequest request) {
        NodeWrapper node = http.post("/subscriptions", request);
        return new Result<Subscription>(node, Subscription.class);
    }

    public Result<Subscription> delete(String customerId, String id) {
        http.delete("/subscriptions/" + id);
        return new Result<Subscription>();
    }

    /**
     * Finds a {@link Subscription} by id.
     * @param id the id of the {@link Subscription}.
     * @return the {@link Subscription} or raises a {@link com.braintreegateway.exceptions.NotFoundException}.
     */
    public Subscription find(String id) {
        return new Subscription(http.get("/subscriptions/" + id));
    }

    /**
     * Updates a {@link Subscription}.
     * @param id the id of the {@link Subscription}.
     * @param request the request.
     * @return a {@link Result}.
     */
    public Result<Subscription> update(String id, SubscriptionRequest request) {
        NodeWrapper node = http.put("/subscriptions/" + id, request);
        return new Result<Subscription>(node, Subscription.class);
    }
    
    /**
     * Search for a {@link Subscription}.
     * @param request the {@link SubscriptionSearchRequest}.
     * @return a {@link Result}.
     */
    public PagedCollection<Subscription> search(SubscriptionSearchRequest search) {
        return this.search(search, 1);
    }

    public PagedCollection<Subscription> search(SubscriptionSearchRequest search, int pageNumber) {
        NodeWrapper node = http.post("/subscriptions/advanced_search?page=" + pageNumber, search);
        return new PagedCollection<Subscription>(new SubscriptionPager(this, search), node, Subscription.class);
    }
}
