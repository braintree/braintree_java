package com.braintreegateway;

import java.util.List;

public class CreditCardVerificationPager implements Pager<CreditCardVerification> {
    private CreditCardVerificationGateway gateway;
    private CreditCardVerificationSearchRequest query;

    public CreditCardVerificationPager(CreditCardVerificationGateway gateway, CreditCardVerificationSearchRequest query) {
        this.gateway = gateway;
        this.query = query;
    }

    public List<CreditCardVerification> getPage(List<String> ids) {
        return gateway.fetchCreditCardVerifications(query, ids);
    }
}
