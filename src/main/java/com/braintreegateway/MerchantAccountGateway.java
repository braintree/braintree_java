package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public final class MerchantAccountGateway {

    public static final String CREATE_URL = "/merchant_accounts/create_via_api";

    private final Http http;

    public MerchantAccountGateway(Http http) {
        this.http = http;
    }

    public Result<MerchantAccount> create(MerchantAccountRequest request) {
        final NodeWrapper response = http.post(CREATE_URL, request);
        return new Result<MerchantAccount>(response, MerchantAccount.class);
    }

    public Result<MerchantAccount> update(String id, MerchantAccountRequest request) {
        final NodeWrapper response = http.put("/merchant_accounts/" + id + "/update_via_api", request);
        return new Result<MerchantAccount>(response, MerchantAccount.class);
    }
}
