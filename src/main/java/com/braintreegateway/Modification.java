package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.math.BigDecimal;

public class Modification {
    private BigDecimal amount;
    private Integer currentBillingCycle;
    private String description;
    private String id;
    private String kind;
    private Integer quantity;
    private String name;
    private Boolean neverExpires;
    private Integer numberOfBillingCycles;
    private String planId;

    public Modification(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        currentBillingCycle = node.findInteger("current-billing-cycle");
        description = node.findString("description");
        id = node.findString("id");
        kind = node.findString("kind");
        quantity = node.findInteger("quantity");
        name = node.findString("name");
        neverExpires = node.findBoolean("never-expires");
        numberOfBillingCycles = node.findInteger("number-of-billing-cycles");
        planId = node.findString("plan-id");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getCurrentBillingCycle() {
      return currentBillingCycle;
    }

    public String getId() {
        return id;
    }

    public Integer getNumberOfBillingCycles() {
        return numberOfBillingCycles;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean neverExpires() {
        return neverExpires;
    }

    public String getDescription() {
        return description;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getPlanId() {
        return planId;
    }
}
