package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.braintreegateway.util.NodeWrapper;

public class Installment {

  private String id;
  private Calendar projectedDisbursementDate;
  private Calendar actualDisbursementDate;
  private BigDecimal amount;
  private List<Adjustment> adjustments;

  public Installment(NodeWrapper node) {
    id = node.findString("id");
    amount = node.findBigDecimal("amount");
    projectedDisbursementDate = node.findDateTime("projected_disbursement_date");
    actualDisbursementDate = node.findDateTime("actual_disbursement_date");
    adjustments = new ArrayList<Adjustment>();
    for (NodeWrapper adjustmentNode : node.findAll("adjustments/adjustment")) {
      adjustments.add(new Adjustment(adjustmentNode));
    }
  }

  public String getId() {
    return id;
  }

  public Calendar getProjectedDisbursementDate() {
    return projectedDisbursementDate;
  }

  public Calendar getActualDisbursementDate() {
    return actualDisbursementDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public List<Adjustment> getAdjustments() {
    return adjustments;
  }
}
