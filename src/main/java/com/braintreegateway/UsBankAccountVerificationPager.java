package com.braintreegateway;

import java.util.List;

public class UsBankAccountVerificationPager implements Pager<UsBankAccountVerification> {
    private UsBankAccountVerificationGateway gateway;
    private UsBankAccountVerificationSearchRequest query;

    public UsBankAccountVerificationPager(UsBankAccountVerificationGateway gateway, UsBankAccountVerificationSearchRequest query) {
        this.gateway = gateway;
        this.query = query;
    }

    public List<UsBankAccountVerification> getPage(List<String> ids) {
        return gateway.fetchUsBankAccountVerifications(query, ids);
    }
}
