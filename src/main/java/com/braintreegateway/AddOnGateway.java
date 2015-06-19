package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

import java.util.ArrayList;
import java.util.List;

public class AddOnGateway {
    private Http http;
    private Configuration configuration;

    public AddOnGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public List<AddOn> all() {
        NodeWrapper node = http.get(configuration.getMerchantPath() + "/add_ons");

        List<AddOn> addOns = new ArrayList<AddOn>();

        for (NodeWrapper addOnResponse : node.findAll("add-on")) {
            addOns.add(new AddOn(addOnResponse));
        }

        return addOns;
    }
}
