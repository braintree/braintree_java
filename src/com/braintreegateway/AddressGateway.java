package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class AddressGateway {

    private Http http;

    public AddressGateway(Http http) {
        this.http = http;
    }

    public Result<Address> create(String customerId, AddressRequest request) {
        NodeWrapper node = http.post("/customers/" + customerId + "/addresses", request);
        return new Result<Address>(node, Address.class);
    }

    public Result<Address> delete(String customerId, String id) {
        http.delete("/customers/" + customerId + "/addresses/" + id);
        return new Result<Address>();
    }

    public Address find(String customerId, String id) {
        return new Address(http.get("/customers/" + customerId + "/addresses/" + id));
    }

    public Result<Address> update(String customerId, String id, AddressRequest request) {
        NodeWrapper node = http.put("/customers/" + customerId + "/addresses/" + id, request);
        return new Result<Address>(node, Address.class);
    }
}
