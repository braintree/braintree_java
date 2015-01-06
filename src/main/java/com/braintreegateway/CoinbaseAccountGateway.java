package com.braintreegateway;

import com.braintreegateway.util.Http;

public class CoinbaseAccountGateway {
    private Http http;

    public CoinbaseAccountGateway(Http http) {
        this.http = http;
    }

    public CoinbaseAccount find(String token) {
        return new CoinbaseAccount(http.get("/payment_methods/coinbase_account/" + token));
    }

    public Result<CoinbaseAccount> delete(String token) {
        http.delete("/payment_methods/coinbase_account/" + token);
        return new Result<CoinbaseAccount>();
    }
}
