package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class PayPalAccountGateway {
    private Http http;

    public PayPalAccountGateway(Http http) {
        this.http = http;
    }

    public PayPalAccount find(String token) {
        return new PayPalAccount(http.get("/payment_methods/paypal_accounts/" + token));
    }

}
