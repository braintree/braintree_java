package com.braintreegateway;

import com.braintreegateway.util.Http;

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
