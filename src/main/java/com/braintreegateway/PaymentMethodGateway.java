package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.exceptions.NotFoundException;

public class PaymentMethodGateway {
    private Http http;

    public PaymentMethodGateway(Http http) {
        this.http = http;
    }

    public Result<? extends PaymentMethod> create(PaymentMethodRequest request) {
        NodeWrapper response = http.post("/payment_methods", request);
        return parseResponse(response);
    }

    public Result<? extends PaymentMethod> update(String token, PaymentMethodRequest request) {
        NodeWrapper response = http.put("/payment_methods/any/" + token, request);
        return parseResponse(response);
    }

    public Result<? extends PaymentMethod> delete(String token) {
        http.delete("/payment_methods/any/" + token);
        return new Result<UnknownPaymentMethod>();
    }

    public PaymentMethod find(String token) {
        if(token.trim().equals("") || token == null)
            throw new NotFoundException();

        NodeWrapper response = http.get("/payment_methods/any/" + token);

        if (response.getElementName() == "paypal-account") {
            return new PayPalAccount(response);
        } else if (response.getElementName() == "credit-card") {
            return new CreditCard(response);
        } else if (response.getElementName() == "sepa-bank-account") {
            return new SEPABankAccount(response);
        } else {
            return new UnknownPaymentMethod(response);
        }
    }

    public Result<? extends PaymentMethod> parseResponse(NodeWrapper response) {
        if (response.getElementName() == "paypal-account") {
            return new Result<PayPalAccount>(response, PayPalAccount.class);
        } else if (response.getElementName() == "credit-card") {
            return new Result<CreditCard>(response, CreditCard.class);
        } else if (response.getElementName() == "sepa-bank-account") {
            return new Result<SEPABankAccount>(response, SEPABankAccount.class);
        } else {
            return new Result<UnknownPaymentMethod>(response, UnknownPaymentMethod.class);
        }
    }
}
