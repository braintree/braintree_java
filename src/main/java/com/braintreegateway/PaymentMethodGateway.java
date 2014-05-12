package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodGateway {
    private Http http;

    public PaymentMethodGateway(Http http) {
        this.http = http;
    }

    public Result<? extends PaymentMethod> create(PaymentMethodRequest request) {
        NodeWrapper response = http.post("/payment_methods", request);
        return new Result<PayPalAccount>(response, PayPalAccount.class);
    }
}
