package com.braintreegateway;

import java.math.BigDecimal;

public class TransactionLineItemRequest extends Request {

    private TransactionRequest parent;
    private TransactionLineItem.Kind kind;
    private BigDecimal discountAmount;
    private BigDecimal quantity;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal unitAmount;
    private BigDecimal unitTaxAmount;
    private String commodityCode;
    private String description;
    private String imageUrl;
    private String name;
    private String productCode;
    private String unitOfMeasure;
    private String upcCode;
    private String upcType;
    private String url;

    public TransactionLineItemRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionLineItemRequest() {
    }

    public TransactionLineItemRequest quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public TransactionLineItemRequest name(String name) {
        this.name = name;
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

    public TransactionLineItemRequest unitTaxAmount(BigDecimal unitTaxAmount) {
        this.unitTaxAmount = unitTaxAmount;
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

    public TransactionLineItemRequest url(String url) {
        this.url = url;
        return this;
    }

    public TransactionLineItemRequest taxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    public TransactionLineItemRequest imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public TransactionLineItemRequest upcCode(String upcCode) {
        this.upcCode = upcCode;
        return this;
    }

    public TransactionLineItemRequest upcType(String upcType) {
        this.upcType = upcType;
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
        return new RequestBuilder(root)
            .addElement("commodityCode", commodityCode)
            .addElement("description", description)
            .addElement("discountAmount", discountAmount)
            .addElement("imageUrl", imageUrl)
            .addElement("kind", kind)
            .addElement("name", name)
            .addElement("productCode", productCode)
            .addElement("quantity", quantity)
            .addElement("taxAmount", taxAmount)
            .addElement("totalAmount", totalAmount)
            .addElement("unitAmount", unitAmount)
            .addElement("unitOfMeasure", unitOfMeasure)
            .addElement("unitTaxAmount", unitTaxAmount)
            .addElement("upcCode", upcCode)
            .addElement("upcType", upcType)
            .addElement("url", url)
            ;
    }
}
