package com.braintreegateway;

import com.braintreegateway.PayPalPaymentResourceRequest;
import com.braintreegateway.PaymentMethodNonce;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class PayPalPaymentResourceGateway {
    private Http http;
    private Configuration configuration;

    public PayPalPaymentResourceGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<PaymentMethodNonce> update(PayPalPaymentResourceRequest request) {
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/paypal/payment_resource", request);
        return new Result<PaymentMethodNonce>(response, PaymentMethodNonce.class);
    }
}