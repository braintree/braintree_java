package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class CustomerGateway {
    private Http http;
    private String baseMerchantURL;

    public CustomerGateway(Http http, Configuration configuration) {
        this.baseMerchantURL = configuration.baseMerchantURL;
        this.http = http;
    }

    public Result<Customer> confirmTransparentRedirect(String queryString) {
        TransparentRedirectRequest trRequest = new TransparentRedirectRequest(queryString);
        NodeWrapper node = http.post("/customers/all/confirm_transparent_redirect_request", trRequest);
        return new Result<Customer>(node, Customer.class);
    }

    public Result<Customer> create(CustomerRequest request) {
        NodeWrapper node = http.post("/customers", request);
        return new Result<Customer>(node, Customer.class);
    }

    public Result<Customer> delete(String id) {
        http.delete("/customers/" + id);
        return new Result<Customer>();
    }

    public Result<Customer> update(String id, CustomerRequest request) {
        NodeWrapper node = http.put("/customers/" + id, request);
        return new Result<Customer>(node, Customer.class);
    }

    public Customer find(String id) {
        return new Customer(http.get("/customers/" + id));
    }

    public String transparentRedirectURLForCreate() {
        return baseMerchantURL + "/customers/all/create_via_transparent_redirect_request";
    }

    public String transparentRedirectURLForUpdate() {
        return baseMerchantURL + "/customers/all/update_via_transparent_redirect_request";
    }
}
