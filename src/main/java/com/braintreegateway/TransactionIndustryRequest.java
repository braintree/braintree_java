package com.braintreegateway;

public class TransactionIndustryRequest extends IndustryRequest {
    private TransactionRequest parent;

    public TransactionIndustryRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionIndustryRequest industryType(Transaction.IndustryType industryType) {
        super.industryType(industryType);
        return this;
    }

    public TransactionIndustryDataRequest data(String data) {
        super.data = new TransactionIndustryDataRequest(this);
        return super.data;
    }

    public TransactionRequest done() {
        return parent;
    }
}
