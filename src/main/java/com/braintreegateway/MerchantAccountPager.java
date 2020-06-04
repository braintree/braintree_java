package com.braintreegateway;

public class MerchantAccountPager implements SimplePager<MerchantAccount> {
    private MerchantAccountGateway gateway;

    public MerchantAccountPager(MerchantAccountGateway gateway) {
        this.gateway = gateway;
    }

    public PaginatedResult<MerchantAccount> getPage(int page) {
        return gateway.fetchMerchantAccounts(page);
    }
}
