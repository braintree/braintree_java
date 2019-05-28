package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceGateway {
    private Http http;
    private Configuration configuration;

    public PaymentMethodNonceGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<PaymentMethodNonce> create(String paymentMethodToken) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods/" + paymentMethodToken + "/nonces");
        return parseResponse(response);
    }

    public Result<PaymentMethodNonce> create(PaymentMethodNonceRequest request) {
      NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods/" + request.getPaymentMethodToken() + "/nonces", request);
      return parseResponse(response);
    }

    public PaymentMethodNonce find(String paymentMethodNonce) {
        NodeWrapper response = http.get(configuration.getMerchantPath() + "/payment_method_nonces/" + paymentMethodNonce);
        return new PaymentMethodNonce(response);
    }

    public Result<PaymentMethodNonce> parseResponse(NodeWrapper response) {
        return new Result<PaymentMethodNonce>(response, PaymentMethodNonce.class);
    }
}
