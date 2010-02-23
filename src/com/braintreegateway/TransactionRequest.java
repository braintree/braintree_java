package com.braintreegateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Transaction.Type;
import com.braintreegateway.util.QueryString;

public class TransactionRequest extends Request {
    private BigDecimal amount;
    private NestedAddressRequest<TransactionRequest> billingAddressRequest;
    private TransactionCreditCardRequest creditCardRequest;
    private String customerId;
    private CustomerRequest customerRequest;
    private Map<String, String> customFields;
    private String orderId;
    private String paymentMethodToken;
    private String shippingAddressId;
    private NestedAddressRequest<TransactionRequest> shippingAddressRequest;
    private TransactionOptionsRequest transactionOptionsRequest;
    private Type type;

    public TransactionRequest() {
        this.customFields = new HashMap<String, String>();
    }

    public TransactionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public NestedAddressRequest<TransactionRequest> billingAddress() {
        billingAddressRequest = new NestedAddressRequest<TransactionRequest>(this, "billing");
        return billingAddressRequest;
    }

    public TransactionCreditCardRequest creditCard() {
        creditCardRequest = new TransactionCreditCardRequest(this);
        return creditCardRequest;
    }

    public CustomerRequest customer() {
        customerRequest = new CustomerRequest(this);
        return customerRequest;
    }

    public TransactionRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public TransactionRequest customField(String apiName, String value) {
        customFields.put(apiName, value);
        return this;
    }

    public TransactionOptionsRequest options() {
        transactionOptionsRequest = new TransactionOptionsRequest(this);
        return transactionOptionsRequest;
    }

    public TransactionRequest orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public TransactionRequest paymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
        return this;
    }

    public NestedAddressRequest<TransactionRequest> shippingAddress() {
        shippingAddressRequest = new NestedAddressRequest<TransactionRequest>(this, "shipping");
        return shippingAddressRequest;
    }

    public TransactionRequest shippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
        return this;
    }

    public String toQueryString(String root) {
        QueryString queryString = new QueryString().
            append(parentBracketChildString(root, "credit_card"), creditCardRequest).
            append(parentBracketChildString(root, "customer"), customerRequest).
            append(parentBracketChildString(root, "billing"), billingAddressRequest).
            append(parentBracketChildString(root, "shipping"), shippingAddressRequest).
            append(parentBracketChildString(root, "options"), transactionOptionsRequest).
            append(parentBracketChildString(root, "amount"), amount).
            append(parentBracketChildString(root, "customer_id"), customerId).
            append(parentBracketChildString(root, "order_id"), orderId).
            append(parentBracketChildString(root, "payment_method_token"), paymentMethodToken).
            append(parentBracketChildString(root, "shipping_address_id"), shippingAddressId);

        if (type != null) {
            queryString.append(parentBracketChildString(root, "type"), type.toString().toLowerCase());
        }

        if (!customFields.isEmpty()) {
            queryString.append(parentBracketChildString(root, "custom_fields"), customFields);
        }

        return queryString.toString();
    }

    public String toQueryString() {
        return toQueryString("transaction");
    }

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<transaction>");
        builder.append(buildXMLElement("amount", amount));
        builder.append(buildXMLElement("customerId", customerId));
        builder.append(buildXMLElement("customFields", customFields));
        builder.append(buildXMLElement("orderId", orderId));
        builder.append(buildXMLElement("paymentMethodToken", paymentMethodToken));
        builder.append(buildXMLElement("shippingAddressId", shippingAddressId));
        if (type != null) {
            builder.append(buildXMLElement("type", type.toString().toLowerCase()));
        }
        builder.append(buildXMLElement(creditCardRequest));
        builder.append(buildXMLElement(customerRequest));
        builder.append(buildXMLElement(billingAddressRequest));
        builder.append(buildXMLElement(shippingAddressRequest));
        builder.append(buildXMLElement(transactionOptionsRequest));
        builder.append("</transaction>");
        return builder.toString();
    }

    public TransactionRequest type(Type type) {
        this.type = type;
        return this;
    }
}
