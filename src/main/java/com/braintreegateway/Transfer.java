package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.util.Calendar;

public class Transfer {

    private final String id;
    private final String message;
    private final Calendar disbursementDate;
    private final String followUpAction;
    private final String merchantAccountId;

    public Transfer(NodeWrapper node) {
        id = node.findString("id");
        message = node.findString("message");
        disbursementDate = node.findDate("disbursement-date");
        followUpAction = node.findString("follow-up-action");
        merchantAccountId = node.findString("merchant-account-id");
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Calendar getDisbursementDate() {
        return disbursementDate;
    }

    public String getFollowUpAction() {
        return followUpAction;
    }

    public MerchantAccount getMerchantAccount(BraintreeGateway gateway) {
        return gateway.merchantAccount().find(merchantAccountId);
    }

    public ResourceCollection<Transaction> getTransactions(BraintreeGateway gateway) {
        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
          merchantAccountId().is(merchantAccountId).
          disbursementDate().between(disbursementDate, disbursementDate);
        return gateway.transaction().search(searchRequest);
    }
}
