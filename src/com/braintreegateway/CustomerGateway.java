package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.QueryString;

/**
 * Provides methods to create, delete, find, and update {@link Customer}
 * objects. This class does not need to be instantiated directly. Instead, use
 * {@link BraintreeGateway#customer()} to get an instance of this class:
 * 
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.customer().create(...)
 * </pre>
 */
public class CustomerGateway {
    private Configuration configuration;
    private Http http;

    public CustomerGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }

    /**
     * Finds all Customers and returns a {@link ResourceCollection} for paging
     * through them starting at the first page.
     * 
     * @return a {@link ResourceCollection}.
     */
    public ResourceCollection<Customer> all() {
        return all(1);
    }

    /**
     * Finds all Customers and returns a {@link ResourceCollection} for paging
     * through them starting at the given page.
     * 
     * @param pageNumber
     *            the starting page.
     * @return a {@link ResourceCollection}
     */
    public ResourceCollection<Customer> all(int pageNumber) {
        String queryString = new QueryString().append("page", pageNumber).toString();
        NodeWrapper response = http.get("/customers?" + queryString);
        return new ResourceCollection<Customer>(new CustomerPager(this), response, Customer.class);
    }

    /**
     * Confirms the transparent redirect request and creates a {@link Customer}
     * based on the parameters submitted with the transparent redirect.
     * 
     * @param queryString
     *            the queryString of the transparent redirect.
     * @return a {@link Result}.
     */
    public Result<Customer> confirmTransparentRedirect(String queryString) {
        TransparentRedirectRequest trRequest = new TransparentRedirectRequest(configuration, queryString);
        NodeWrapper node = http.post("/customers/all/confirm_transparent_redirect_request", trRequest);
        return new Result<Customer>(node, Customer.class);
    }

    /**
     * Creates a {@link Customer}.
     * 
     * @param request
     *            the request.
     * @return a {@link Result}.
     */
    public Result<Customer> create(CustomerRequest request) {
        NodeWrapper node = http.post("/customers", request);
        return new Result<Customer>(node, Customer.class);
    }

    /**
     * Deletes a {@link Customer} by id.
     * 
     * @param id
     *            the id of the {@link Customer}.
     * @return a {@link Result}.
     */
    public Result<Customer> delete(String id) {
        http.delete("/customers/" + id);
        return new Result<Customer>();
    }

    /**
     * Finds a {@link Customer} by id.
     * 
     * @param id
     *            the id of the {@link Customer}.
     * @return the {@link Customer} or raises a
     *         {@link com.braintreegateway.exceptions.NotFoundException}.
     */
    public Customer find(String id) {
        return new Customer(http.get("/customers/" + id));
    }

    /**
     * Returns the transparent redirect URL for creating a {@link Customer}.
     * 
     * @return a URL as a String.
     */
    public String transparentRedirectURLForCreate() {
        return configuration.baseMerchantURL + "/customers/all/create_via_transparent_redirect_request";
    }

    /**
     * Returns the transparent redirect URL for updating a {@link Customer}.
     * 
     * @return a URL as a String.
     */
    public String transparentRedirectURLForUpdate() {
        return configuration.baseMerchantURL + "/customers/all/update_via_transparent_redirect_request";
    }

    /**
     * Updates a {@link Customer}.
     * 
     * @param id
     *            the id of the {@link Customer}.
     * @param request
     *            the request.
     * @return a {@link Result}.
     */
    public Result<Customer> update(String id, CustomerRequest request) {
        NodeWrapper node = http.put("/customers/" + id, request);
        return new Result<Customer>(node, Customer.class);
    }

}
