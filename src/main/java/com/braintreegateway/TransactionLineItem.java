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

    public TransactionLineItem(NodeWrapper node) {
        commodityCode = node.findString("commodity-code");
        description = node.findString("description");
        discountAmount = node.findBigDecimal("discount-amount");
        imageUrl = node.findString("image-url");
        kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        name = node.findString("name");
        productCode = node.findString("product-code");
        quantity = node.findBigDecimal("quantity");
        taxAmount = node.findBigDecimal("tax-amount");
        totalAmount = node.findBigDecimal("total-amount");
        unitAmount = node.findBigDecimal("unit-amount");
        unitOfMeasure = node.findString("unit-of-measure");
        unitTaxAmount = node.findBigDecimal("unit-tax-amount");
        upcCode = node.findString("upc-code");
        upcType = node.findString("upc-type");
        url = node.findString("url");
    }

    public BigDecimal getQuantity() {
            return quantity;
    }

    public String getName() {
            return name;
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

    public BigDecimal getUnitTaxAmount() {
            return unitTaxAmount;
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

    public String getUrl() {
            return url;
    }

    public BigDecimal getTaxAmount() {
            return taxAmount;
    }

    public String getImageUrl() {
            return imageUrl;
    }

    public String getUpcCode() {
            return upcCode;
    }

    public String getUpcType() {
            return upcType;
    }
}
