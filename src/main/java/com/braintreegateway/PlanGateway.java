package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class PlanGateway {
    private Http http;
    private Configuration configuration;

    public PlanGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public List<Plan> all() {
        NodeWrapper node = http.get(configuration.getMerchantPath() + "/plans");

        List<Plan> plans = new ArrayList<Plan>();

        for (NodeWrapper planResponse : node.findAll("plan")) {
            plans.add(new Plan(planResponse));
        }

        return plans;
    }
}
