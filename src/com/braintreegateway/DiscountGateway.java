package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

import java.util.ArrayList;
import java.util.List;

public class DiscountGateway {
    private Configuration configuration;
    private Http http;

    public DiscountGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }

    public List<Discount> all() {
        NodeWrapper node = http.get("/discounts");

        List<Discount> discounts = new ArrayList<Discount>();

        for (NodeWrapper discountResponse : node.findAll("discount")) {
            discounts.add(new Discount(discountResponse));
        }

        return discounts;
    }
}
