package com.braintreegateway;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class Transaction {

    public enum Status {
        AUTHORIZED, AUTHORIZING, FAILED, GATEWAY_REJECTED, PROCESSOR_DECLINED, SETTLED, SETTLEMENT_FAILED, SUBMITTED_FOR_SETTLEMENT, UNKNOWN, UNRECOGNIZED, VOIDED;
    }

    public enum Type {
        CREDIT("credit"),
        SALE("sale"),
        UNRECOGNIZED("unrecognized");
        
        private final String name;
        
        Type(String name) {
            this.name = name;
        }
        
        public String toString() {
            return name;
        }
    }
    
    public enum CreatedUsing {
        FULL_INFORMATION("full_information"),
        TOKEN("token");
        
        private final String name;
        
        CreatedUsing(String name) {
            this.name = name;
        }
        
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
        
        public String toString() {
            return name;
        }
    }

    private BigDecimal amount;
    private Address billingAddress;
    private Calendar createdAt;
    private CreditCard creditCard;
    private Customer customer;
    private Map<String, String> customFields;
    private String id;
    private String merchantAccountId;
    private String orderId;
    private String processorAuthorizationCode;
    private String processorResponseCode;
    private String processorResponseText;
    private Address shippingAddress;
    private Status status;
    private String subscriptionId;
    private Type type;
    private Calendar updatedAt;

    public Transaction(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        billingAddress = new Address(node.findFirst("billing"));
        createdAt = node.findDateTime("created-at");
        creditCard = new CreditCard(node.findFirst("credit-card"));
        customFields = node.findMap("custom-fields");
        customer = new Customer(node.findFirst("customer"));
        id = node.findString("id");
        merchantAccountId = node.findString("merchant-account-id");
        orderId = node.findString("order-id");
        processorAuthorizationCode = node.findString("processor-authorization-code");
        processorResponseCode = node.findString("processor-response-code");
        processorResponseText = node.findString("processor-response-text");
        shippingAddress = new Address(node.findFirst("shipping"));
        status = EnumUtils.findByName(Status.class, node.findString("status"));
        subscriptionId = node.findString("subscription-id");
        type = EnumUtils.findByName(Type.class, node.findString("type"));
        updatedAt = node.findDateTime("updated-at");
    }

    public BigDecimal getAmount() {
        return amount;
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

    public Customer getCustomer() {
        return customer;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
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

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Status getStatus() {
        return status;
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
