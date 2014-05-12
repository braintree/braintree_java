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
        return parseResponse(response);
    }

    public Result<? extends PaymentMethod> parseResponse(NodeWrapper response) {
        if (response.getElementName() == "paypal-account") {
            return new Result<PayPalAccount>(response, PayPalAccount.class);
        } else if (response.getElementName() == "credit-card") {
            return new Result<CreditCard>(response, CreditCard.class);
        } else {
            return new Result<UnknownPaymentMethod>(response, UnknownPaymentMethod.class);
        }
    }
}
