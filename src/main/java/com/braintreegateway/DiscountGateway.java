package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

import java.util.ArrayList;
import java.util.List;

public class DiscountGateway {
    private Http http;
    private Configuration configuration;

    public DiscountGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public List<Discount> all() {
        NodeWrapper node = http.get(configuration.getMerchantPath() + "/discounts");

        List<Discount> discounts = new ArrayList<Discount>();

        for (NodeWrapper discountResponse : node.findAll("discount")) {
            discounts.add(new Discount(discountResponse));
        }

        return discounts;
    }
}
