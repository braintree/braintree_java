package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PayPalPaymentResourceRequest extends Request {
    private BigDecimal amount;
    AmountBreakdownRequest amountBreakdown;
    private String currencyIsoCode;
    private String customField;
    private String description;
    private List<TransactionLineItemRequest> lineItems;
    private String orderId;
    private String payeeEmail;
    private String paymentMethodNonce;
    private PayPalPaymentResourceShippingRequest shippingRequest;
    private List<ShippingOptionRequest> shippingOptions;

    public PayPalPaymentResourceRequest() {
        this.lineItems = new ArrayList<TransactionLineItemRequest>();
        this.shippingOptions = new ArrayList<ShippingOptionRequest>();
    }

    public PayPalPaymentResourceRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AmountBreakdownRequest amountBreakdown() {
        amountBreakdown = new AmountBreakdownRequest(this);
        return amountBreakdown;
    }

    public PayPalPaymentResourceRequest currencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
        return this;
    }

    public PayPalPaymentResourceRequest customField(String customField) {
        this.customField = customField;
        return this;
    }

    public PayPalPaymentResourceRequest description(String description) {
        this.description = description;
        return this;
    }

    public PayPalPaymentResourceRequest addLineItem(TransactionLineItemRequest lineItemRequest) {
        this.lineItems.add(lineItemRequest);
        return this;
    }

    public PayPalPaymentResourceRequest orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public PayPalPaymentResourceRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    public PayPalPaymentResourceRequest paymentMethodNonce(String paymentMethodNonce) {
        this.paymentMethodNonce = paymentMethodNonce;
        return this;
    }

    public PayPalPaymentResourceShippingRequest shipping() {
        shippingRequest = new PayPalPaymentResourceShippingRequest(this);
        return shippingRequest;
    }

    public ShippingOptionRequest shippingOption() {
        ShippingOptionRequest shippingOptionRequest = new ShippingOptionRequest(this);
        shippingOptions.add(shippingOptionRequest);
        return shippingOptionRequest;
    }
    
    @Override
    public String toXML() {
        return buildRequest("paypalPaymentResource").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("amount", amount)
            .addElement("amountBreakdown", amountBreakdown)
            .addElement("currencyIsoCode", currencyIsoCode)
            .addElement("customField", customField)
            .addElement("description", description)
            .addElement("orderId", orderId)
            .addElement("payeeEmail", payeeEmail)
            .addElement("paymentMethodNonce", paymentMethodNonce)
            .addElement("shipping", shippingRequest);

        if (!lineItems.isEmpty()) {
            builder.addElement("lineItems", lineItems);
        }

        if (!shippingOptions.isEmpty()) {
            builder.addElement("shippingOptions", shippingOptions);
        }

        return builder;
    }
}