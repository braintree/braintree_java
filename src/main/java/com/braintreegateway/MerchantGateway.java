package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class MerchantGateway {

    private Http http;

    public MerchantGateway(Http http) {
        this.http = http;
    }

    public Result<Merchant> create(MerchantRequest request) {
        NodeWrapper response = http.post("/merchants/create_via_api", request);
        return new Result<Merchant>(response, Merchant.class);
    }
}
