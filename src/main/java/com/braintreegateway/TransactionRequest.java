package com.braintreegateway;

import com.braintreegateway.Transaction.ScaExemption;
import com.braintreegateway.Transaction.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a fluent interface to build up requests around {@link Transaction Transactions}.
 */
// NEXT_MAJOR_VERSION remove venmoSdkPaymentMethodCode
// The old venmo SDK integration has been deprecated
public class TransactionRequest extends Request {
    private BigDecimal amount;
    private TransactionAndroidPayCardRequest androidPayCardRequest;
    private TransactionApplePayCardRequest applePayCardRequest;
    private String billingAddressId;
    private TransactionAddressRequest billingAddressRequest;
    private String channel;
    private TransactionCreditCardRequest creditCardRequest;
    private String currencyIsoCode;
    private String customerId;
    private CustomerRequest customerRequest;
    private Map<String, String> customFields;
    private TransactionDescriptorRequest descriptorRequest;
    private String deviceData;
    private String deviceSessionId;
    private BigDecimal discountAmount;
    private String exchangeRateQuoteId;
    private ExternalVaultRequest externalVaultRequest;
    private Boolean finalCapture;
    private Boolean foreignRetailer;
    private String fraudMerchantId;
    private TransactionIndustryRequest industryRequest;
    private InstallmentRequest installments;
    private String merchantAccountId;
    private String orderId;
    private String paymentMethodNonce;
    private String paymentMethodToken;
    private TransactionPayPalRequest paypalRequest;
    private String productSku;
    private String purchaseOrderNumber;
    private Boolean recurring;
    private RiskDataTransactionRequest riskDataTransactionRequest;
    private ScaExemption scaExemption;
    private BigDecimal serviceFeeAmount;
    private String sharedBillingAddressId;
    private String sharedCustomerId;
    private String sharedPaymentMethodNonce;
    private String sharedPaymentMethodToken;
    private String sharedShippingAddressId;
    private String shippingAddressId;
    private TransactionAddressRequest shippingAddressRequest;
    private BigDecimal shippingAmount;
    private String shipsFromPostalCode;
    private String source;
    private BigDecimal taxAmount;
    private Boolean taxExempt;
    private String threeDSecureAuthenticationId;
    private TransactionThreeDSecurePassThruRequest threeDSecurePassThruRequest;
    private String threeDSecureToken;
    private Boolean threeDSecureTransaction;
    private List<TransactionLineItemRequest> transactionLineItemRequests;
    private TransactionOptionsRequest transactionOptionsRequest;
    private Type type;
    private String venmoSdkPaymentMethodCode;

    public TransactionRequest() {
        this.customFields = new HashMap<String, String>();
        this.threeDSecureTransaction = false;
        this.transactionLineItemRequests = new ArrayList<TransactionLineItemRequest>();
    }

    public TransactionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionAndroidPayCardRequest androidPayCardRequest() {
        androidPayCardRequest = new TransactionAndroidPayCardRequest(this);
        return androidPayCardRequest;
    }

    public TransactionApplePayCardRequest applePayCardRequest() {
        TransactionApplePayCardRequest applePayCardRequest = new TransactionApplePayCardRequest(this);
        this.applePayCardRequest = applePayCardRequest;
        return applePayCardRequest;
    }

    public TransactionAddressRequest billingAddress() {
        billingAddressRequest = new TransactionAddressRequest(this, "billing");
        return billingAddressRequest;
    }

    public TransactionRequest billingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
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

    public TransactionRequest currencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
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

    public TransactionDescriptorRequest descriptor() {
        descriptorRequest = new TransactionDescriptorRequest(this);
        return descriptorRequest;
    }

    public TransactionRequest deviceData(String deviceData) {
        this.deviceData = deviceData;
        return this;
    }

    @Deprecated
    // Merchants should be using deviceData only
    public TransactionRequest deviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
        return this;
    }

    public TransactionRequest discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public TransactionRequest exchangeRateQuoteId(String exchangeRateQuoteId) {
        this.exchangeRateQuoteId = exchangeRateQuoteId;
        return this;
    }

    public ExternalVaultRequest externalVault() {
        ExternalVaultRequest externalVaultRequest = new ExternalVaultRequest(this);
        this.externalVaultRequest = externalVaultRequest;
        return externalVaultRequest;
    }

    public TransactionRequest finalCapture(Boolean finalCapture) {
        this.finalCapture = finalCapture;
        return this;
    }

    public TransactionRequest foreignRetailer(Boolean foreignRetailer) {
        this.foreignRetailer = foreignRetailer;
        return this;
    }

    @Deprecated
    // Merchants should be using deviceData only
    public TransactionRequest fraudMerchantId(String fraudMerchantId) {
        this.fraudMerchantId = fraudMerchantId;
        return this;
    }

    public TransactionIndustryRequest industry() {
        industryRequest = new TransactionIndustryRequest(this);
        return industryRequest;
    }

    public InstallmentRequest installments() {
        this.installments = new InstallmentRequest(this);
        return this.installments;
    }

    public TransactionLineItemRequest lineItem() {
        TransactionLineItemRequest transactionLineItemRequest = new TransactionLineItemRequest(this);
        transactionLineItemRequests.add(transactionLineItemRequest);
        return transactionLineItemRequest;
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

    public TransactionRequest paymentMethodNonce(String paymentMethodNonce) {
      this.paymentMethodNonce = paymentMethodNonce;
      return this;
    }

    public TransactionRequest paymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
        return this;
    }

    public TransactionPayPalRequest paypalAccount() {
        paypalRequest = new TransactionPayPalRequest(this);
        return paypalRequest;
    }

    public TransactionRequest productSku(String productSku) {
        this.productSku = productSku;
        return this;
    }

    public TransactionRequest purchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
        return this;
    }

    /* @deprecated
     * use transactionSource instead
     */
    @Deprecated
    public TransactionRequest recurring(Boolean recurring) {
        this.recurring = recurring;
        return this;
    }

    public RiskDataTransactionRequest riskData() {
        riskDataTransactionRequest = new RiskDataTransactionRequest(this);
        return riskDataTransactionRequest;
    }

    public TransactionRequest scaExemption(ScaExemption scaExemption) {
        this.scaExemption = scaExemption;
        return this;
    }

    public TransactionRequest serviceFeeAmount(BigDecimal fee) {
        serviceFeeAmount = fee;
        return this;
    }

    public TransactionRequest sharedBillingAddressId(String sharedBillingAddressId) {
        this.sharedBillingAddressId = sharedBillingAddressId;
        return this;
    }

    public TransactionRequest sharedCustomerId(String sharedCustomerId) {
        this.sharedCustomerId = sharedCustomerId;
        return this;
    }

    public TransactionRequest sharedPaymentMethodNonce(String sharedPaymentMethodNonce) {
        this.sharedPaymentMethodNonce = sharedPaymentMethodNonce;
        return this;
    }

    public TransactionRequest sharedPaymentMethodToken(String sharedPaymentMethodToken) {
        this.sharedPaymentMethodToken = sharedPaymentMethodToken;
        return this;
    }

    public TransactionRequest sharedShippingAddressId(String sharedShippingAddressId) {
        this.sharedShippingAddressId = sharedShippingAddressId;
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

    public TransactionRequest shippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
        return this;
    }

    public TransactionRequest shipsFromPostalCode(String shipsFromPostalCode) {
        this.shipsFromPostalCode = shipsFromPostalCode;
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

    public TransactionRequest threeDSecureAuthenticationId(String threeDSecureAuthenticationId) {
      this.threeDSecureTransaction = true;
      this.threeDSecureAuthenticationId = threeDSecureAuthenticationId;
      return this;
    }

    public TransactionThreeDSecurePassThruRequest threeDSecurePassThru() {
        threeDSecurePassThruRequest = new TransactionThreeDSecurePassThruRequest(this);
        return threeDSecurePassThruRequest;
    }

    // NEXT_MAJOR_VERSION remove this method
    // threeDSecureToken has been deprecated in favor of threeDSecureAuthenticationID
    /**
     * @deprecated use threeDSecureAuthenticationID instead
     */
    @Deprecated
    public TransactionRequest threeDSecureToken(String threeDSecureToken) {
      this.threeDSecureTransaction = true;
      this.threeDSecureToken = threeDSecureToken;
      return this;
    }

    public TransactionRequest transactionSource(String source) {
        this.source = source;
        return this;
    }

    public TransactionRequest type(Type type) {
        this.type = type;
        return this;
    }

    //NEXT_MAJOR_VERSION remove this method
    /**
     * @deprecated - The Venmo SDK integration is Unsupported. Please update your integration to use Pay with Venmo instead
    */
    @Deprecated
    public TransactionRequest venmoSdkPaymentMethodCode(String venmoSdkPaymentMethodCode) {
      this.venmoSdkPaymentMethodCode = venmoSdkPaymentMethodCode;
      return this;
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

    // NEXT_MAJOR_VERSION remove venmoSdkPaymentMethodCode
    // The old venmo SDK integration has been deprecated
    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("amount", amount)
            .addElement("androidPayCard", androidPayCardRequest)
            .addElement("applePayCard", applePayCardRequest)
            .addElement("billing", billingAddressRequest)
            .addElement("billingAddressId", billingAddressId)
            .addElement("channel", channel)
            .addElement("creditCard", creditCardRequest)
            .addElement("currencyIsoCode", currencyIsoCode)
            .addElement("customer", customerRequest)
            .addElement("customerId", customerId)
            .addElement("descriptor", descriptorRequest)
            .addElement("deviceData", deviceData)
            .addElement("deviceSessionId", deviceSessionId)
            .addElement("discountAmount", discountAmount)
            .addElement("exchangeRateQuoteId", exchangeRateQuoteId)
            .addElement("externalVault", externalVaultRequest)
            .addElement("finalCapture", finalCapture)
            .addElement("foreignRetailer", foreignRetailer)
            .addElement("fraudMerchantId", fraudMerchantId)
            .addElement("industry", industryRequest)
            .addElement("installments", installments)
            .addElement("merchantAccountId", merchantAccountId)
            .addElement("options", transactionOptionsRequest)
            .addElement("orderId", orderId)
            .addElement("paymentMethodNonce", paymentMethodNonce)
            .addElement("paymentMethodToken", paymentMethodToken)
            .addElement("paypalAccount", paypalRequest)
            .addElement("productSku", productSku)
            .addElement("purchaseOrderNumber", purchaseOrderNumber)
            .addElement("recurring", recurring)
            .addElement("riskData", riskDataTransactionRequest)
            .addElement("serviceFeeAmount", serviceFeeAmount)
            .addElement("sharedBillingAddressId", sharedBillingAddressId)
            .addElement("sharedCustomerId", sharedCustomerId)
            .addElement("sharedPaymentMethodNonce", sharedPaymentMethodNonce)
            .addElement("sharedPaymentMethodToken", sharedPaymentMethodToken)
            .addElement("sharedShippingAddressId", sharedShippingAddressId)
            .addElement("shipping", shippingAddressRequest)
            .addElement("shippingAddressId", shippingAddressId)
            .addElement("shippingAmount", shippingAmount)
            .addElement("shipsFromPostalCode", shipsFromPostalCode)
            .addElement("transactionSource", source)
            .addElement("taxAmount", taxAmount)
            .addElement("taxExempt", taxExempt)
            .addElement("threeDSecurePassThru", threeDSecurePassThruRequest)
            .addElement("venmoSdkPaymentMethodCode", venmoSdkPaymentMethodCode);

        if (!customFields.isEmpty()) {
            builder.addElement("customFields", customFields);
        }
        if (!transactionLineItemRequests.isEmpty()) {
            builder.addElement("lineItems", transactionLineItemRequests);
        }
        if (scaExemption != null) {
            builder.addElement("scaExemption", scaExemption.toString());
        }

        // NEXT_MAJOR_VERSION modify this check
        // threeDSecureToken has been deprecated in favor of threeDSecureAuthenticationID
        if (threeDSecureTransaction) {
            if (threeDSecureAuthenticationId != null) {
                builder.addElement("threeDSecureAuthenticationId", threeDSecureAuthenticationId);
            } else {
                String token = threeDSecureToken != null ? threeDSecureToken : "";
                builder.addElement("threeDSecureToken", token);
            }
        }

        if (type != null) {
            builder.addElement("type", type.toString().toLowerCase());
        }

        return builder;
    }
}
