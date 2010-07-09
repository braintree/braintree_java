package com.braintreegateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.Transaction.Type;

/**
 * Provides a fluent interface to build up requests around {@link Transaction Transactions}.
 */
public class TransactionRequest extends Request {
    private BigDecimal amount;
    private TransactionAddressRequest billingAddressRequest;
    private TransactionCreditCardRequest creditCardRequest;
    private String customerId;
    private CustomerRequest customerRequest;
    private Map<String, String> customFields;
    private String merchantAccountId;
    private String orderId;
    private String paymentMethodToken;
    private String shippingAddressId;
    private TransactionAddressRequest shippingAddressRequest;
    private TransactionOptionsRequest transactionOptionsRequest;
    private Type type;

    public TransactionRequest() {
        this.customFields = new HashMap<String, String>();
    }

    public TransactionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionAddressRequest billingAddress() {
        billingAddressRequest = new TransactionAddressRequest(this, "billing");
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
    
    @Override
    public String getKind() {
        return TransparentRedirectGateway.CREATE_TRANSACTION;
    }

    public TransactionRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
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

    public TransactionAddressRequest shippingAddress() {
        shippingAddressRequest = new TransactionAddressRequest(this, "shipping");
        return shippingAddressRequest;
    }

    public TransactionRequest shippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
        return this;
    }
    
    public TransactionRequest type(Type type) {
        this.type = type;
        return this;
    }

    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    public String toQueryString() {
        return toQueryString("transaction");
    }

    @Override
    public String toXML() {
        return buildRequest("transaction").toXML();
    }
    
    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).     
            addElement("amount", amount).
            addElement("customerId", customerId).
            addElement("merchantAccountId", merchantAccountId).
            addElement("orderId", orderId).
            addElement("paymentMethodToken", paymentMethodToken).
            addElement("shippingAddressId", shippingAddressId).
            addElement("creditCard", creditCardRequest).
            addElement("customer", customerRequest).
            addElement("billing", billingAddressRequest).
            addElement("shipping", shippingAddressRequest).
            addElement("options", transactionOptionsRequest);
        
        if (!customFields.isEmpty()) {
            builder.addElement("customFields", customFields);
        }
        if (type != null) {
            builder.addElement("type", type.toString().toLowerCase());
        }
        
        return builder;
    }
}
