package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

import java.util.ArrayList;
import java.util.List;

public class AddOnGateway {
    private Configuration configuration;
    private Http http;

    public AddOnGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }

    public List<AddOn> all() {
        NodeWrapper node = http.get("/add_ons");

        List<AddOn> addOns = new ArrayList<AddOn>();

        for (NodeWrapper addOnResponse : node.findAll("modification")) {
            addOns.add(new AddOn(addOnResponse));
        }

        return addOns;
    }
}
