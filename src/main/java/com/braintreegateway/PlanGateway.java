package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

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

  public Result<Plan> create(PlanRequest request) {
    NodeWrapper node = http.post(configuration.getMerchantPath() + "/plans", request);
    return new Result<Plan>(node, Plan.class);
  }

  public Plan find(String id) {
    if (id == null || id.trim().equals("")) {
      throw new NotFoundException();
    }
    return new Plan(http.get(configuration.getMerchantPath() + "/plans/" + id));
  }

  public Result<Plan> update(String id, PlanRequest request) {
    NodeWrapper node = http.put(configuration.getMerchantPath() + "/plans/" + id, request);
    return new Result<Plan>(node, Plan.class);
  }
}
