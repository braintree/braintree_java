package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Disbursement {

    public enum DisbursementType {
        UNKNOWN,
        CREDIT,
        DEBIT;
    }

    private final String id;
    private final String exceptionMessage;
    private final Calendar disbursementDate;
    private final DisbursementType disbursementType;
    private final String followUpAction;
    private final List<String> transactionIds;
    private final Boolean success;
    private final Boolean retry;
    private final BigDecimal amount;
    private MerchantAccount merchantAccount;

    public Disbursement(NodeWrapper node) {
        id = node.findString("id");
        exceptionMessage = node.findString("exception-message");
        disbursementDate = node.findDate("disbursement-date");
        followUpAction = node.findString("follow-up-action");
        success = node.findBoolean("success");
        retry = node.findBoolean("retry");
        amount = node.findBigDecimal("amount");
        merchantAccount = new MerchantAccount(node.findFirst("merchant-account"));

        transactionIds = new ArrayList<String>();
        for (NodeWrapper transactionIdNode : node.findAll("transaction-ids/item")) {
            transactionIds.add(transactionIdNode.findString("."));
        }

        String dType = node.findString("disbursement-type");
        if (dType == null) {
            disbursementType = DisbursementType.UNKNOWN;
        } else {
            disbursementType = EnumUtils.findByName(DisbursementType.class, dType, DisbursementType.UNKNOWN);
        }
    }

    public String getId() {
        return id;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public Calendar getDisbursementDate() {
        return disbursementDate;
    }

    public DisbursementType getDisbursementType() {
        return disbursementType;
    }

    public String getFollowUpAction() {
        return followUpAction;
    }

    public Boolean getRetry() {
        return retry;
    }

    public Boolean getSuccess() {
        return success;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public List<String> getTransactionIds() {
        return transactionIds;
    }

    public MerchantAccount getMerchantAccount() {
        return merchantAccount;
    }

    public ResourceCollection<Transaction> getTransactions(BraintreeGateway gateway) {
        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
          ids().in(transactionIds);
        return gateway.transaction().search(searchRequest);
    }
}
