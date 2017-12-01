package com.braintreegateway;

import java.math.BigDecimal;

public class TransactionLineItemRequest extends Request {

    private TransactionRequest parent;
    private BigDecimal quantity;
    private String description;
    private TransactionLineItem.Kind kind;
    private BigDecimal unitAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private String unitOfMeasure;
    private String productCode;
    private String commodityCode;

    public TransactionLineItemRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionLineItemRequest quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public TransactionLineItemRequest description(String description) {
        this.description = description;
        return this;
    }

    public TransactionLineItemRequest kind(TransactionLineItem.Kind kind) {
        this.kind = kind;
        return this;
    }

    public TransactionLineItemRequest unitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
        return this;
    }

    public TransactionLineItemRequest totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public TransactionLineItemRequest discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public TransactionLineItemRequest unitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public TransactionLineItemRequest productCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public TransactionLineItemRequest commodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
        return this;
    }

    public TransactionRequest done() {
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
            addElement("quantity", quantity).
            addElement("description", description).
            addElement("kind", kind).
            addElement("unitAmount", unitAmount).
            addElement("totalAmount", totalAmount).
            addElement("discountAmount", discountAmount).
            addElement("unitOfMeasure", unitOfMeasure).
            addElement("productCode", productCode).
            addElement("commodityCode", commodityCode);
    }
}
