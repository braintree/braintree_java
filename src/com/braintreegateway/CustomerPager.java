package com.braintreegateway;

import java.util.List;

public class CustomerPager implements Pager<Customer> {

    private CustomerGateway gateway;

    public CustomerPager(CustomerGateway gateway) {
        this.gateway = gateway;
    }

    public List<Customer> getPage(List<String> ids) {
        return gateway.fetchCustomers(ids);
    }
}
