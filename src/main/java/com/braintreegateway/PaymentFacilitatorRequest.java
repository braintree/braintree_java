package com.braintreegateway;

public class PaymentFacilitatorRequest extends Request {
    private String paymentFacilitatorId;
    private TransactionRequest parent;
    private TransactionSubMerchant subMerchant;

    public PaymentFacilitatorRequest() {
    }

    public PaymentFacilitatorRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionSubMerchant subMerchant() {
        subMerchant = new TransactionSubMerchant(this);
        return this.subMerchant; 
    }

    public PaymentFacilitatorRequest paymentFacilitatorId(String paymentFacilitatorId) {
        this.paymentFacilitatorId = paymentFacilitatorId;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("paymentFacilitator").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("paymentFacilitator");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        if (paymentFacilitatorId != null) {
            builder.addElement("paymentFacilitatorId", paymentFacilitatorId);
        }
        if (subMerchant != null) {
            builder.addElement("subMerchant", subMerchant);
        }
        return builder;
    }
}
