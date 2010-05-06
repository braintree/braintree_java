package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
 * 
 * For more detailed information on {@link Subscription Subscriptions}, see <a href="http://www.braintreepaymentsolutions.com/gateway/subscription-api" target="_blank">http://www.braintreepaymentsolutions.com/gateway/subscription-api</a>
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
    public ResourceCollection<Subscription> search(SubscriptionSearchRequest search) {
        NodeWrapper node = http.post("/subscriptions/advanced_search_ids", search);
        return new ResourceCollection<Subscription>(new SubscriptionPager(this, search), node);
    }

    List<Subscription> fetchSubscriptions(SubscriptionSearchRequest search, List<String> ids) {
        search.ids().in(ids);
        NodeWrapper response = http.post("/subscriptions/advanced_search", search);

        List<Subscription> items = new ArrayList<Subscription>();
        for (NodeWrapper node : response.findAll("subscription")) {
            items.add(new Subscription(node));
        }
        
        return items;
    }
        
    private Result<Transaction> retryCharge(SubscriptionTransactionRequest txnRequest) {
        NodeWrapper response = http.post("/transactions", txnRequest);
        return new Result<Transaction>(response, Transaction.class);
    }

    public Result<Transaction> retryCharge(String subscriptionId) {
       return retryCharge(new SubscriptionTransactionRequest().
         subscriptionId(subscriptionId));
    }

    public Result<Transaction> retryCharge(String subscriptionId, BigDecimal amount) {
        return retryCharge(new SubscriptionTransactionRequest().
          subscriptionId(subscriptionId).
          amount(amount));
     }
}
