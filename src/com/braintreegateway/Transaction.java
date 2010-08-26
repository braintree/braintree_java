package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class Transaction {

    public enum CreatedUsing {
        FULL_INFORMATION("full_information"),
        TOKEN("token");

        private final String name;

        CreatedUsing(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum GatewayRejectionReason {
        AVS("avs"),
        AVS_AND_CVV("avs_and_cvv"),
        CVV("cvv"),
        DUPLICATE("duplicate");

        private final String name;

        GatewayRejectionReason(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Source {
        API("api"),
        CONTROL_PANEL("control_panel");

        private final String name;

        Source(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Status {
        AUTHORIZED, AUTHORIZING, FAILED, GATEWAY_REJECTED, PROCESSOR_DECLINED, SETTLED, SETTLEMENT_FAILED, SUBMITTED_FOR_SETTLEMENT, UNRECOGNIZED, VOIDED;
    }

    public enum Type {
        CREDIT("credit"),
        SALE("sale"),
        UNRECOGNIZED("unrecognized");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private List<AddOn> addOns;
    private BigDecimal amount;
    private String avsErrorResponseCode;
    private String avsPostalCodeResponseCode;
    private String avsStreetAddressResponseCode;
    private Address billingAddress;
    private Calendar createdAt;
    private CreditCard creditCard;
    private String currencyIsoCode;
    private Customer customer;
    private Map<String, String> customFields;
    private String cvvResponseCode;
    private List<Discount> discounts;
    private GatewayRejectionReason gatewayRejectionReason;
    private String id;
    private String merchantAccountId;
    private String orderId;
    private String processorAuthorizationCode;
    private String processorResponseCode;
    private String processorResponseText;
    private String refundedTransactionId;
    private String refundId;
    private List<String> refundIds;
    private String settlementBatchId;
    private Address shippingAddress;
    private Status status;
    private List<StatusEvent> statusHistory;
    private String subscriptionId;
    private Type type;
    private Calendar updatedAt;

    public Transaction(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        avsErrorResponseCode = node.findString("avs-error-response-code");
        avsPostalCodeResponseCode = node.findString("avs-postal-code-response-code");
        avsStreetAddressResponseCode = node.findString("avs-street-address-response-code");
        billingAddress = new Address(node.findFirst("billing"));
        createdAt = node.findDateTime("created-at");
        creditCard = new CreditCard(node.findFirst("credit-card"));
        currencyIsoCode = node.findString("currency-iso-code");
        customFields = node.findMap("custom-fields");
        customer = new Customer(node.findFirst("customer"));
        cvvResponseCode = node.findString("cvv-response-code");
        gatewayRejectionReason = EnumUtils.findByName(GatewayRejectionReason.class, node
            .findString("gateway-rejection-reason"));
        id = node.findString("id");
        merchantAccountId = node.findString("merchant-account-id");
        orderId = node.findString("order-id");
        processorAuthorizationCode = node.findString("processor-authorization-code");
        processorResponseCode = node.findString("processor-response-code");
        processorResponseText = node.findString("processor-response-text");
        refundedTransactionId = node.findString("refunded-transaction-id");
        refundId = node.findString("refund-id");
        settlementBatchId = node.findString("settlement-batch-id");
        shippingAddress = new Address(node.findFirst("shipping"));
        status = EnumUtils.findByName(Status.class, node.findString("status"));
        subscriptionId = node.findString("subscription-id");
        type = EnumUtils.findByName(Type.class, node.findString("type"));
        updatedAt = node.findDateTime("updated-at");

        refundIds = new ArrayList<String>();
        for (NodeWrapper refundIdNode : node.findAll("refund-ids/item")) {
            refundIds.add(refundIdNode.findString("."));
        }
        
        statusHistory = new ArrayList<StatusEvent>();
        for (NodeWrapper statusNode : node.findAll("status-history/status-event")) {
            statusHistory.add(new StatusEvent(statusNode));
        }

        addOns = new ArrayList<AddOn>();
        for (NodeWrapper addOnResponse : node.findAll("add-ons/add-on")) {
            addOns.add(new AddOn(addOnResponse));
        }

        discounts = new ArrayList<Discount>();
        for (NodeWrapper discountResponse : node.findAll("discounts/discount")) {
            discounts.add(new Discount(discountResponse));
        }
    }

    public List<AddOn> getAddOns() {
        return addOns;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAvsErrorResponseCode() {
        return avsErrorResponseCode;
    }

    public String getAvsPostalCodeResponseCode() {
        return avsPostalCodeResponseCode;
    }

    public String getAvsStreetAddressResponseCode() {
        return avsStreetAddressResponseCode;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public String getCvvResponseCode() {
        return cvvResponseCode;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public GatewayRejectionReason getGatewayRejectionReason() {
        return gatewayRejectionReason;
    }

    public String getId() {
        return id;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProcessorAuthorizationCode() {
        return processorAuthorizationCode;
    }

    public String getProcessorResponseCode() {
        return processorResponseCode;
    }

    public String getProcessorResponseText() {
        return processorResponseText;
    }

    public String getRefundedTransactionId() {
        return refundedTransactionId;
    }
    
    @Deprecated
    public String getRefundId() {
        return refundId;
    }
    
    public List<String> getRefundIds() {
        return refundIds;
    }

    public String getSettlementBatchId() {
        return settlementBatchId;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Status getStatus() {
        return status;
    }

    public List<StatusEvent> getStatusHistory() {
        return statusHistory;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public Type getType() {
        return type;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public Address getVaultBillingAddress(BraintreeGateway gateway) {
        if (billingAddress.getId() == null) {
            return null;
        }
        return gateway.address().find(customer.getId(), billingAddress.getId());
    }

    public CreditCard getVaultCreditCard(BraintreeGateway gateway) {
        if (creditCard.getToken() == null) {
            return null;
        }
        return gateway.creditCard().find(creditCard.getToken());
    }

    public Customer getVaultCustomer(BraintreeGateway gateway) {
        if (customer.getId() == null) {
            return null;
        }
        return gateway.customer().find(customer.getId());
    }

    public Address getVaultShippingAddress(BraintreeGateway gateway) {
        if (shippingAddress.getId() == null) {
            return null;
        }
        return gateway.address().find(customer.getId(), shippingAddress.getId());
    }
}
