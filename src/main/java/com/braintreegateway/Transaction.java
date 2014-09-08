package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Transaction {

    public boolean isDisbursed() {
        return getDisbursementDetails().isValid();
    }

    public enum CreatedUsing {
        FULL_INFORMATION("full_information"),
        TOKEN("token"),
        UNRECOGNIZED("unrecognized");

        private final String name;

        CreatedUsing(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum EscrowStatus {
        HELD,
        HOLD_PENDING,
        RELEASE_PENDING,
        RELEASED,
        REFUNDED,
        UNRECOGNIZED;
    }

    public enum GatewayRejectionReason {
        AVS("avs"),
        AVS_AND_CVV("avs_and_cvv"),
        CVV("cvv"),
        DUPLICATE("duplicate"),
        FRAUD("fraud"),
        UNRECOGNIZED("unrecognized");

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
        CONTROL_PANEL("control_panel"),
        RECURRING("recurring"),
        UNRECOGNIZED("unrecognized");

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
        AUTHORIZATION_EXPIRED, AUTHORIZED, AUTHORIZING, FAILED, GATEWAY_REJECTED, PROCESSOR_DECLINED, SETTLED, SETTLEMENT_CONFIRMED, SETTLEMENT_DECLINED, SETTLEMENT_PENDING, SETTLING, SUBMITTED_FOR_SETTLEMENT, UNRECOGNIZED, VOIDED;
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
    private String channel;
    private Calendar createdAt;
    private CreditCard creditCard;
    private String currencyIsoCode;
    private Customer customer;
    private Map<String, String> customFields;
    private String cvvResponseCode;
    private DisbursementDetails disbursementDetails;
    private List<Dispute> disputes;
    private Descriptor descriptor;
    private List<Discount> discounts;
    private EscrowStatus escrowStatus;
    private GatewayRejectionReason gatewayRejectionReason;
    private String id;
    private String merchantAccountId;
    private String orderId;
    private PayPalDetails paypalDetails;
    private String planId;
    private String processorAuthorizationCode;
    private String processorResponseCode;
    private String processorResponseText;
    private String processorSettlementResponseCode;
    private String processorSettlementResponseText;
    private String voiceReferralNumber;
    private String purchaseOrderNumber;
    private Boolean recurring;
    private String refundedTransactionId;
    private String refundId;
    private List<String> refundIds;
    private String settlementBatchId;
    private Address shippingAddress;
    private Status status;
    private List<StatusEvent> statusHistory;
    private String subscriptionId;
    private Subscription subscription;
    private BigDecimal taxAmount;
    private Boolean taxExempt;
    private Type type;
    private Calendar updatedAt;
    private BigDecimal serviceFeeAmount;
    private String paymentInstrumentType;

    public Transaction(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        avsErrorResponseCode = node.findString("avs-error-response-code");
        avsPostalCodeResponseCode = node.findString("avs-postal-code-response-code");
        avsStreetAddressResponseCode = node.findString("avs-street-address-response-code");
        billingAddress = new Address(node.findFirst("billing"));
        channel = node.findString("channel");
        createdAt = node.findDateTime("created-at");
        creditCard = new CreditCard(node.findFirst("credit-card"));
        currencyIsoCode = node.findString("currency-iso-code");
        customFields = node.findMap("custom-fields/*");
        customer = new Customer(node.findFirst("customer"));
        cvvResponseCode = node.findString("cvv-response-code");
        disbursementDetails = new DisbursementDetails(node.findFirst("disbursement-details"));
        descriptor = new Descriptor(node.findFirst("descriptor"));
        escrowStatus = EnumUtils.findByName(EscrowStatus.class, node.findString("escrow-status"));
        gatewayRejectionReason = EnumUtils.findByName(GatewayRejectionReason.class, node.findString("gateway-rejection-reason"));
        id = node.findString("id");
        merchantAccountId = node.findString("merchant-account-id");
        orderId = node.findString("order-id");
        NodeWrapper paypalNode = node.findFirst("paypal");
        if (paypalNode != null) {
            paypalDetails = new PayPalDetails(paypalNode);
        }
        planId = node.findString("plan-id");
        processorAuthorizationCode = node.findString("processor-authorization-code");
        processorResponseCode = node.findString("processor-response-code");
        processorResponseText = node.findString("processor-response-text");
        processorSettlementResponseCode = node.findString("processor-settlement-response-code");
        processorSettlementResponseText = node.findString("processor-settlement-response-text");
        voiceReferralNumber = node.findString("voice-referral-number");
        purchaseOrderNumber = node.findString("purchase-order-number");
        recurring = node.findBoolean("recurring");
        refundedTransactionId = node.findString("refunded-transaction-id");
        refundId = node.findString("refund-id");
        serviceFeeAmount = node.findBigDecimal("service-fee-amount");
        settlementBatchId = node.findString("settlement-batch-id");
        shippingAddress = new Address(node.findFirst("shipping"));
        status = EnumUtils.findByName(Status.class, node.findString("status"));
        subscription = new Subscription(node.findFirst("subscription"));
        subscriptionId = node.findString("subscription-id");
        taxAmount = node.findBigDecimal("tax-amount");
        taxExempt = node.findBoolean("tax-exempt");
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

        disputes = new ArrayList<Dispute>();
        for (NodeWrapper dispute : node.findAll("disputes/dispute")) {
            disputes.add(new Dispute(dispute));
        }

        paymentInstrumentType = node.findString("payment-instrument-type");
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

    public String getChannel() {
        return channel;
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

    public DisbursementDetails getDisbursementDetails() {
        return disbursementDetails;
    }

    public List<Dispute> getDisputes() {
        return disputes;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public EscrowStatus getEscrowStatus() {
        return escrowStatus;
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

    public PayPalDetails getPayPalDetails() {
        return paypalDetails;
    }

    public String getPlanId() {
        return planId;
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

    public String getProcessorSettlementResponseCode() {
        return processorSettlementResponseCode;
    }

    public String getProcessorSettlementResponseText() {
        return processorSettlementResponseText;
    }
    public String getVoiceReferralNumber() {
        return voiceReferralNumber;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public String getRefundedTransactionId() {
        return refundedTransactionId;
    }

    /**
     * Please use Transaction.getRefundIds() instead
     */
    @Deprecated
    public String getRefundId() {
        return refundId;
    }

    public List<String> getRefundIds() {
        return refundIds;
    }

    public BigDecimal getServiceFeeAmount() {
        return serviceFeeAmount;
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

    public Subscription getSubscription() {
        return subscription;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public Type getType() {
        return type;
    }

    public Boolean getRecurring() {
        return recurring;
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

    public Boolean isTaxExempt() {
        return taxExempt;
    }

    public String getPaymentInstrumentType() {
        return paymentInstrumentType;
    }
}
