package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;

public class TransactionLineItem {

    public enum Kind {
        DEBIT("debit"),
        CREDIT("credit"),
        UNRECOGNIZED("unrecognized");

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private BigDecimal quantity;
    private String description;
    private TransactionLineItem.Kind kind;
    private BigDecimal unitAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private String unitOfMeasure;
    private String productCode;
    private String commodityCode;

    public TransactionLineItem(NodeWrapper node) {
        quantity = node.findBigDecimal("quantity");
        description = node.findString("description");
        kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        unitAmount = node.findBigDecimal("unit-amount");
        totalAmount = node.findBigDecimal("total-amount");
        discountAmount = node.findBigDecimal("discount-amount");
        unitOfMeasure = node.findString("unit-of-measure");
        productCode = node.findString("product-code");
        commodityCode = node.findString("commodity-code");
    }

    public BigDecimal getQuantity() {
            return quantity;
    }

    public String getDescription() {
            return description;
    }

    public Kind getKind() {
            return kind;
    }

    public BigDecimal getUnitAmount() {
            return unitAmount;
    }

    public BigDecimal getTotalAmount() {
            return totalAmount;
    }

    public BigDecimal getDiscountAmount() {
            return discountAmount;
    }

    public String getUnitOfMeasure() {
            return unitOfMeasure;
    }

    public String getProductCode() {
            return productCode;
    }

    public String getCommodityCode() {
            return commodityCode;
    }

}
