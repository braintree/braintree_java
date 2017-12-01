package com.braintreegateway;

import com.braintreegateway.Transaction.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a fluent interface to build up requests around {@link Transaction Transactions}.
 */
public class TransactionRequest extends Request {
    private BigDecimal amount;
    private TransactionAddressRequest billingAddressRequest;
    private String deviceData;
    private TransactionCreditCardRequest creditCardRequest;
    private TransactionPayPalRequest paypalRequest;
    private String channel;
    private String customerId;
    private String deviceSessionId;
    private String fraudMerchantId;
    private CustomerRequest customerRequest;
    private Map<String, String> customFields;
    private String merchantAccountId;
    private String orderId;
    private String paymentMethodToken;
    private String purchaseOrderNumber;
    private Boolean recurring;
    private String source;
    private String shippingAddressId;
    private String billingAddressId;
    private TransactionDescriptorRequest descriptorRequest;
    private TransactionIndustryRequest industryRequest;
    private TransactionAddressRequest shippingAddressRequest;
    private TransactionOptionsRequest transactionOptionsRequest;
    private TransactionThreeDSecurePassThruRequest threeDSecurePassThruRequest;
    private BigDecimal taxAmount;
    private Boolean taxExempt;
    private Type type;
    private String venmoSdkPaymentMethodCode;
    private String paymentMethodNonce;
    private BigDecimal serviceFeeAmount;

    private String threeDSecureToken;
    private Boolean threeDSecureTransaction;

    private String sharedPaymentMethodToken;
    private String sharedPaymentMethodNonce;
    private String sharedCustomerId;
    private String sharedShippingAddressId;
    private String sharedBillingAddressId;

    private RiskDataTransactionRequest riskDataTransactionRequest;

    private List<TransactionLineItemRequest> transactionLineItemRequests;

    public TransactionRequest() {
        this.customFields = new HashMap<String, String>();
        this.threeDSecureTransaction = false;
        this.transactionLineItemRequests = new ArrayList<TransactionLineItemRequest>();
    }

    public TransactionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionAddressRequest billingAddress() {
        billingAddressRequest = new TransactionAddressRequest(this, "billing");
        return billingAddressRequest;
    }

    public TransactionRequest deviceData(String deviceData) {
        this.deviceData = deviceData;
        return this;
    }

    public TransactionRequest channel(String channel) {
        this.channel = channel;
        return this;
    }

    public TransactionCreditCardRequest creditCard() {
        creditCardRequest = new TransactionCreditCardRequest(this);
        return creditCardRequest;
    }

    public TransactionPayPalRequest paypalAccount() {
        paypalRequest = new TransactionPayPalRequest(this);
        return paypalRequest;
    }

    public TransactionRequest serviceFeeAmount(BigDecimal fee) {
        serviceFeeAmount = fee;
        return this;
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

    public TransactionRequest deviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
        return this;
    }

    public TransactionRequest fraudMerchantId(String fraudMerchantId) {
        this.fraudMerchantId = fraudMerchantId;
        return this;
    }

    public TransactionDescriptorRequest descriptor() {
        descriptorRequest = new TransactionDescriptorRequest(this);
        return descriptorRequest;
    }

    public TransactionIndustryRequest industry() {
        industryRequest = new TransactionIndustryRequest(this);
        return industryRequest;
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

    public TransactionThreeDSecurePassThruRequest threeDSecurePassThru() {
        threeDSecurePassThruRequest = new TransactionThreeDSecurePassThruRequest(this);
        return threeDSecurePassThruRequest;
    }

    public TransactionRequest orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public TransactionRequest paymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
        return this;
    }

    public TransactionRequest purchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
        return this;
    }

    public TransactionRequest recurring(Boolean recurring) {
        this.recurring = recurring;
        return this;
    }

    public TransactionRequest transactionSource(String source) {
        this.source = source;
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

    public TransactionRequest billingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
        return this;
    }

    public TransactionRequest taxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    public TransactionRequest taxExempt(Boolean taxExempt) {
        this.taxExempt = taxExempt;
        return this;
    }

    public TransactionRequest venmoSdkPaymentMethodCode(String venmoSdkPaymentMethodCode) {
      this.venmoSdkPaymentMethodCode = venmoSdkPaymentMethodCode;
      return this;
    }

    public TransactionRequest paymentMethodNonce(String paymentMethodNonce) {
      this.paymentMethodNonce = paymentMethodNonce;
      return this;
    }

    public TransactionRequest threeDSecureToken(String threeDSecureToken) {
      this.threeDSecureTransaction = true;
      this.threeDSecureToken = threeDSecureToken;
      return this;
    }

    public TransactionRequest sharedPaymentMethodToken(String sharedPaymentMethodToken) {
        this.sharedPaymentMethodToken = sharedPaymentMethodToken;
        return this;
    }

    public TransactionRequest sharedPaymentMethodNonce(String sharedPaymentMethodNonce) {
        this.sharedPaymentMethodNonce = sharedPaymentMethodNonce;
        return this;
    }

    public TransactionRequest sharedCustomerId(String sharedCustomerId) {
        this.sharedCustomerId = sharedCustomerId;
        return this;
    }

    public TransactionRequest sharedShippingAddressId(String sharedShippingAddressId) {
        this.sharedShippingAddressId = sharedShippingAddressId;
        return this;
    }

    public TransactionRequest sharedBillingAddressId(String sharedBillingAddressId) {
        this.sharedBillingAddressId = sharedBillingAddressId;
        return this;
    }

    public RiskDataTransactionRequest riskData() {
        riskDataTransactionRequest = new RiskDataTransactionRequest(this);
        return riskDataTransactionRequest;
    }

    public TransactionLineItemRequest lineItem() {
        TransactionLineItemRequest transactionLineItemRequest = new TransactionLineItemRequest(this);
        transactionLineItemRequests.add(transactionLineItemRequest);
        return transactionLineItemRequest;
    }

    @Override
    public String toQueryString() {
        return toQueryString("transaction");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("transaction").toXML();
    }

    public TransactionRequest type(Type type) {
        this.type = type;
        return this;
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("amount", amount).
            addElement("deviceData", deviceData).
            addElement("channel", channel).
            addElement("customerId", customerId).
            addElement("merchantAccountId", merchantAccountId).
            addElement("orderId", orderId).
            addElement("paymentMethodToken", paymentMethodToken).
            addElement("paymentMethodNonce", paymentMethodNonce).
            addElement("purchaseOrderNumber", purchaseOrderNumber).
            addElement("taxAmount", taxAmount).
            addElement("taxExempt", taxExempt).
            addElement("shippingAddressId", shippingAddressId).
            addElement("billingAddressId", billingAddressId).
            addElement("creditCard", creditCardRequest).
            addElement("paypalAccount", paypalRequest).
            addElement("customer", customerRequest).
            addElement("descriptor", descriptorRequest).
            addElement("industry", industryRequest).
            addElement("billing", billingAddressRequest).
            addElement("shipping", shippingAddressRequest).
            addElement("options", transactionOptionsRequest).
            addElement("threeDSecurePassThru", threeDSecurePassThruRequest).
            addElement("recurring", recurring).
            addElement("transactionSource", source).
            addElement("deviceSessionId", deviceSessionId).
            addElement("fraudMerchantId", fraudMerchantId).
            addElement("venmoSdkPaymentMethodCode", venmoSdkPaymentMethodCode).
            addElement("sharedPaymentMethodToken", sharedPaymentMethodToken).
            addElement("sharedPaymentMethodNonce", sharedPaymentMethodNonce).
            addElement("sharedCustomerId", sharedCustomerId).
            addElement("sharedShippingAddressId", sharedShippingAddressId).
            addElement("sharedBillingAddressId", sharedBillingAddressId).
            addElement("serviceFeeAmount", serviceFeeAmount).
            addElement("riskData", riskDataTransactionRequest);

        if (!customFields.isEmpty()) {
            builder.addElement("customFields", customFields);
        }
        if (type != null) {
            builder.addElement("type", type.toString().toLowerCase());
        }

        if (threeDSecureTransaction) {
            String token = threeDSecureToken != null ? threeDSecureToken : "";
            builder.addElement("threeDSecureToken", token);
        }

        if (!transactionLineItemRequests.isEmpty()) {
            builder.addElement("lineItems", transactionLineItemRequests);
        }

        return builder;
    }
}
