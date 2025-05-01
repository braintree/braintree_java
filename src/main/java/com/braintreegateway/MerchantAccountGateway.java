package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class MerchantAccountGateway {

    public static final String CREATE_URL = "/merchant_accounts/create_via_api";
    public static final String CREATE_FOR_CURRENCY_URL = "/merchant_accounts/create_for_currency";

    private final Http http;
    private Configuration configuration;

    public MerchantAccountGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<MerchantAccount> createForCurrency(MerchantAccountCreateForCurrencyRequest request) {
        final NodeWrapper response = http.post(configuration.getMerchantPath() + CREATE_FOR_CURRENCY_URL, request);
        return new Result<MerchantAccount>(response, MerchantAccount.class);
    }

    public MerchantAccount find(String id) {
        if (id == null || id.trim().equals("")) {
            throw new NotFoundException();
        }
        return new MerchantAccount(http.get(configuration.getMerchantPath() + "/merchant_accounts/" + id));
    }

    public PaginatedCollection<MerchantAccount> all() {
        return new PaginatedCollection<MerchantAccount>(new MerchantAccountPager(this));
    }

    public PaginatedResult<MerchantAccount> fetchMerchantAccounts(int page) {
        final NodeWrapper response = http.get(configuration.getMerchantPath() + "/merchant_accounts?page=" + page);

        List<MerchantAccount> merchantAccounts = new ArrayList<MerchantAccount>();
        for (NodeWrapper node : response.findAll("merchant-account")) {
            merchantAccounts.add(new MerchantAccount(node));
        }

        return new PaginatedResult<MerchantAccount>(response.findInteger("total-items"), response.findInteger("page-size"), merchantAccounts);
    }
}
