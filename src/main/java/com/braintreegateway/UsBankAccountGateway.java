package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.StringUtils;

public class UsBankAccountGateway {
    private BraintreeGateway gateway;
    private Http http;
    private Configuration configuration;

    public UsBankAccountGateway(BraintreeGateway gateway, Http http, Configuration configuration) {
        this.gateway = gateway;
        this.http = http;
        this.configuration = configuration;
    }

    public UsBankAccount find(String token) {
        if (StringUtils.isInvalidPathSegment(token)) {
            throw new NotFoundException();
        }

        return new UsBankAccount(http.get(configuration.getMerchantPath() + "/payment_methods/us_bank_account/" + token));
    }

    public Result<Transaction> sale(String token, TransactionRequest transactionRequest) {
        transactionRequest
            .paymentMethodToken(token)
            .options()
                .submitForSettlement(true);
        return gateway.transaction().sale(transactionRequest);
    }
}
