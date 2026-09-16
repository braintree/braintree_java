package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.StringUtils;
import com.braintreegateway.util.NodeWrapper;

public class PayPalAccountGateway {
    private Http http;
    private Configuration configuration;

    public PayPalAccountGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public PayPalAccount find(String token) {
        if (StringUtils.isInvalidPathSegment(token)) {
            throw new NotFoundException();
        }

        return new PayPalAccount(http.get(configuration.getMerchantPath() + "/payment_methods/paypal_account/" + token));
    }

    public Result<PayPalAccount> delete(String token) {
        if (StringUtils.isInvalidPathSegment(token)) {
            throw new NotFoundException();
        }

        http.delete(configuration.getMerchantPath() + "/payment_methods/paypal_account/" + token);
        return new Result<PayPalAccount>();
    }

    public Result<PayPalAccount> update(String token, PayPalAccountRequest request) {
        if (StringUtils.isInvalidPathSegment(token)) {
            throw new NotFoundException();
        }

        NodeWrapper response = http.put(configuration.getMerchantPath() + "/payment_methods/paypal_account/" + token, request);
        return new Result<PayPalAccount>(response, PayPalAccount.class);
    }
}
