package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
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

    public NodeWrapper revoke(String token) {
        String request = new RequestBuilder("payment-method").addElement("shared-payment-method-token", token).toXML();
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods/revoke", request);
        return response;
    }

    public Result<? extends PaymentMethod> parseResponse(NodeWrapper response) {
        if (response.getElementName() == "paypal-account") {
            return new Result<PayPalAccount>(response, PayPalAccount.class);
        } else if (response.getElementName() == "credit-card") {
            return new Result<CreditCard>(response, CreditCard.class);
        } else if (response.getElementName() == "europe-bank-account") {
            return new Result<EuropeBankAccount>(response, EuropeBankAccount.class);
        } else if (response.getElementName() == "apple-pay-card") {
            return new Result<ApplePayCard>(response, ApplePayCard.class);
        } else if (response.getElementName() == "android-pay-card") {
            return new Result<AndroidPayCard>(response, AndroidPayCard.class);
        } else if (response.getElementName() == "amex-express-checkout-card") {
            return new Result<AmexExpressCheckoutCard>(response, AmexExpressCheckoutCard.class);
        } else if (response.getElementName() == "coinbase-account") {
            return new Result<CoinbaseAccount>(response, CoinbaseAccount.class);
        } else if (response.getElementName() == "venmo-account") {
            return new Result<VenmoAccount>(response, VenmoAccount.class);
        } else {
            return new Result<UnknownPaymentMethod>(response, UnknownPaymentMethod.class);
        }
    }
}
