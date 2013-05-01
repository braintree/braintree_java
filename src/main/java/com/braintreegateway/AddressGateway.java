package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

/**
 * Provides methods to create, delete, find, and update {@link Address} objects.  
 * This class does not need to be instantiated directly.  
 * Instead, use {@link BraintreeGateway#address()} to get an instance of this class:
 * 
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.address().create(...)
 * </pre>
 */
public class AddressGateway {

    private Http http;

    public AddressGateway(Http http) {
        this.http = http;
    }

    /**
     * Creates an {@link Address} for a {@link Customer}.
     * @param customerId the id of the {@link Customer}.
     * @param request the request object.
     * @return a {@link Result} object.
     */
    public Result<Address> create(String customerId, AddressRequest request) {
        NodeWrapper node = http.post("/customers/" + customerId + "/addresses", request);
        return new Result<Address>(node, Address.class);
    }

    /**
     * Deletes a Customer's {@link Address}.
     * @param customerId the id of the {@link Customer}.
     * @param id the id of the {@link Address} to delete.
     * @return a {@link Result} object.
     */
    public Result<Address> delete(String customerId, String id) {
        http.delete("/customers/" + customerId + "/addresses/" + id);
        return new Result<Address>();
    }

    /**
     * Finds a Customer's {@link Address}.
     * @param customerId the id of the {@link Customer}.
     * @param id the id of the {@link Address}.
     * @return the {@link Address} or raises a {@link com.braintreegateway.exceptions.NotFoundException}.
     */
    public Address find(String customerId, String id) {
        if(customerId == null || customerId.trim().equals("") || id == null || id.trim().equals(""))
            throw new NotFoundException();

        return new Address(http.get("/customers/" + customerId + "/addresses/" + id));
    }

    
    /**
     * Updates a Customer's {@link Address}.
     * @param customerId the id of the {@link Customer}.
     * @param id the id of the {@link Address}.
     * @param request the request object containing the {@link AddressRequest} parameters.
     * @return the {@link Address} or raises a {@link com.braintreegateway.exceptions.NotFoundException}.
     */
    public Result<Address> update(String customerId, String id, AddressRequest request) {
        NodeWrapper node = http.put("/customers/" + customerId + "/addresses/" + id, request);
        return new Result<Address>(node, Address.class);
    }
}
