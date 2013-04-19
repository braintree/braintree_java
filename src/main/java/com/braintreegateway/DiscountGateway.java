package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class DiscountGateway {
    private Http http;

    public DiscountGateway(Http http) {
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
