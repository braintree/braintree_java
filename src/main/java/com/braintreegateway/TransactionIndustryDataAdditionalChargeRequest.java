package com.braintreegateway;

import java.math.BigDecimal;

public class TransactionIndustryDataAdditionalChargeRequest extends Request {

    public enum Kind {
        MINI_BAR("mini_bar"),
        RESTAURANT("restaurant"),
        GIFT_SHOP("gift_shop"),
        TELEPHONE("telephone"),
        LAUNDRY("laundry"),
        OTHER("other");

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private TransactionIndustryDataRequest parent;
    private BigDecimal amount;
    private TransactionIndustryDataAdditionalChargeRequest.Kind kind;

    public TransactionIndustryDataAdditionalChargeRequest(TransactionIndustryDataRequest parent) {
        this.parent = parent;
    }

    public TransactionIndustryDataAdditionalChargeRequest kind(Kind kind) {
        this.kind = kind;
        return this;
    }

    public TransactionIndustryDataAdditionalChargeRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionIndustryDataRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("item").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("item");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("kind", kind).
            addElement("amount", amount);
    }
}
