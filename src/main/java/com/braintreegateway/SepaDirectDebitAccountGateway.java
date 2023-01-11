package com.braintreegateway;

import com.braintreegateway.util.Http;

public class SepaDirectDebitAccountGateway {
    private Http http;
    private Configuration configuration;

    public SepaDirectDebitAccountGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public SepaDirectDebitAccount find(String token) {
        return new SepaDirectDebitAccount(http.get(configuration.getMerchantPath() + "/payment_methods/sepa_debit_account/" + token));
    }

    public Result<SepaDirectDebitAccount> delete(String token) {
        http.delete(configuration.getMerchantPath() + "/payment_methods/sepa_debit_account/" + token);
        return new Result<SepaDirectDebitAccount>();
    }
}
