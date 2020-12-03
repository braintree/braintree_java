package com.braintreegateway;

import java.math.BigDecimal;
import java.util.Calendar;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class Adjustment {
  private Calendar projectedDisbursementDate;
  private Calendar actualDisbursementDate;
  private BigDecimal amount;
  private KIND kind;

  public enum KIND {
    REFUND,
    DISPUTE,
    UNRECOGNIZED;
  }
  
  public Adjustment(NodeWrapper node) {
    amount = node.findBigDecimal("amount");
    projectedDisbursementDate = node.findDateTime("projected_disbursement_date");
    actualDisbursementDate = node.findDateTime("actual_disbursement_date");
    kind = EnumUtils.findByName(Adjustment.KIND.class, node.findString("kind"), KIND.UNRECOGNIZED);
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

  public KIND getKind() {
    return kind;
  }
}
