package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class PayPalAccountGateway {
    private Http http;

    public PayPalAccountGateway(Http http) {
        this.http = http;
    }

    public PayPalAccount find(String token) {
        return new PayPalAccount(http.get("/payment_methods/paypal_account/" + token));
    }

    public Result<PayPalAccount> delete(String token) {
        http.delete("/payment_methods/paypal_account/" + token);
        return new Result<PayPalAccount>();
    }

    public Result<PayPalAccount> update(String token, PayPalAccountRequest request) {
        NodeWrapper response = http.put("/payment_methods/paypal_account/" + token, request);
        return new Result<PayPalAccount>(response, PayPalAccount.class);
    }
}
