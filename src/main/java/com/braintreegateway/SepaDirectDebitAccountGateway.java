package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.StringUtils;

public class SepaDirectDebitAccountGateway {
    private Http http;
    private Configuration configuration;

    public SepaDirectDebitAccountGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public SepaDirectDebitAccount find(String token) {
        if (StringUtils.isInvalidPathSegment(token)) {
            throw new NotFoundException();
        }

        return new SepaDirectDebitAccount(http.get(configuration.getMerchantPath() + "/payment_methods/sepa_debit_account/" + token));
    }

    public Result<SepaDirectDebitAccount> delete(String token) {
        if (StringUtils.isInvalidPathSegment(token)) {
            throw new NotFoundException();
        }

        http.delete(configuration.getMerchantPath() + "/payment_methods/sepa_debit_account/" + token);
        return new Result<SepaDirectDebitAccount>();
    }
}
