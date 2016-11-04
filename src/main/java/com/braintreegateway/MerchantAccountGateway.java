package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.exceptions.NotFoundException;

public final class MerchantAccountGateway {

    public static final String CREATE_URL = "/merchant_accounts/create_via_api";
    public static final String CREATE_FOR_CURRENCY_URL = "/merchant_accounts/create_for_currency";

    private final Http http;
    private Configuration configuration;

    public MerchantAccountGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<MerchantAccount> create(MerchantAccountRequest request) {
        final NodeWrapper response = http.post(configuration.getMerchantPath() + CREATE_URL, request);
        return new Result<MerchantAccount>(response, MerchantAccount.class);
    }

    public Result<MerchantAccount> createForCurrency(MerchantAccountCreateForCurrencyRequest request) {
        final NodeWrapper response = http.post(configuration.getMerchantPath() + CREATE_FOR_CURRENCY_URL, request);
        return new Result<MerchantAccount>(response, MerchantAccount.class);
    }

    public MerchantAccount find(String id) {
        if(id == null || id.trim().equals(""))
            throw new NotFoundException();
        return new MerchantAccount(http.get(configuration.getMerchantPath() + "/merchant_accounts/" + id));
    }

    public Result<MerchantAccount> update(String id, MerchantAccountRequest request) {
        final NodeWrapper response = http.put(configuration.getMerchantPath() + "/merchant_accounts/" + id + "/update_via_api", request);
        return new Result<MerchantAccount>(response, MerchantAccount.class);
    }
}
