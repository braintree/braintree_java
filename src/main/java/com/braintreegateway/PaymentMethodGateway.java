package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.PaymentMethodParser;
import com.braintreegateway.exceptions.NotFoundException;

public class PaymentMethodGateway {
    private Http http;
    private Configuration configuration;

    public PaymentMethodGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<? extends PaymentMethod> create(PaymentMethodRequest request) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods", request);
        return parseResponse(response);
    }

    public Result<? extends PaymentMethod> update(String token, PaymentMethodRequest request) {
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/payment_methods/any/" + token, request);
        return parseResponse(response);
    }

    public Result<? extends PaymentMethod> delete(String token) {
        http.delete(configuration.getMerchantPath() + "/payment_methods/any/" + token);
        return new Result<UnknownPaymentMethod>();
    }

    public Result<? extends PaymentMethod> delete(String token, PaymentMethodDeleteRequest request) {
        http.delete(configuration.getMerchantPath() + "/payment_methods/any/" + token + "?" + request.toQueryString());
        return new Result<UnknownPaymentMethod>();
    }

    public PaymentMethod find(String token) {
        if(token == null || token.trim().equals(""))
            throw new NotFoundException();

        NodeWrapper response = http.get(configuration.getMerchantPath() + "/payment_methods/any/" + token);

        return parseResponse(response).getTarget();
    }

    public Result<PaymentMethodNonce> grant(String token) {
        String request = new RequestBuilder("payment-method").addElement("shared-payment-method-token", token).toXML();
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods/grant", request);
        return new Result<PaymentMethodNonce>(response, PaymentMethodNonce.class);
    }

    public Result<PaymentMethodNonce> grant(String token, PaymentMethodGrantRequest grantRequest) {
        String request = grantRequest.sharedPaymentMethodToken(token).toXML();
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods/grant", request);
        return new Result<PaymentMethodNonce>(response, PaymentMethodNonce.class);
    }

    public Result<? extends PaymentMethod> revoke(String token) {
        PaymentMethodGrantRevokeRequest revokeRequest = new PaymentMethodGrantRevokeRequest();
        String request = revokeRequest.sharedPaymentMethodToken(token).toXML();
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods/revoke", request);
        return parseResponse(response);
    }

    public Result<? extends PaymentMethod> parseResponse(NodeWrapper response) {
        return PaymentMethodParser.parsePaymentMethod(response);
    }
}
