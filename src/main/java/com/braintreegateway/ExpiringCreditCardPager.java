package com.braintreegateway;

import java.util.List;

public class ExpiringCreditCardPager implements Pager<CreditCard> {

    private CreditCardGateway gateway;
    private String queryString;

    public ExpiringCreditCardPager(CreditCardGateway gateway, String queryString) {
        this.gateway = gateway;
        this.queryString = queryString;
    }

    public List<CreditCard> getPage(List<String> ids) {
        return gateway.fetchExpiringCreditCards(ids, queryString);
    }
}
