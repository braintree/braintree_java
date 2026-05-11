package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

// NEXT_MAJOR_VERSION remove this class
/**
 * @deprecated MerchantGateway has been deprecated and will be removed in a future version.
 */
@Deprecated
public class MerchantGateway {

    private Http http;
    private Configuration configuration;

    public MerchantGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    /**
     * @deprecated {@link MerchantGateway#create(MerchantRequest)} is deprecated and will be removed in a future version.
     */
    // NEXT_MAJOR_VERSION remove this method
    // The merchant create endpoint has been disabled
    @Deprecated
    public Result<Merchant> create(MerchantRequest request) {
        NodeWrapper response = http.post("/merchants/create_via_api", request);
        return new Result<Merchant>(response, Merchant.class);
    }
}
