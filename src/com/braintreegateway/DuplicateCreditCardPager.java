package com.braintreegateway;

import java.util.List;

public class DuplicateCreditCardPager implements Pager<CreditCard> {

    private CreditCardGateway gateway;
    private String token;

    public DuplicateCreditCardPager(CreditCardGateway gateway, String token) {
        this.gateway = gateway;
        this.token = token;
    }

    public List<CreditCard> getPage(List<String> ids) {
        return gateway.fetchDuplicateCreditCards(ids, this.token);
    }
}

