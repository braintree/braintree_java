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
        APPLICATION_INCOMPLETE("application_incomplete"),
        AVS("avs"),
        AVS_AND_CVV("avs_and_cvv"),
        CVV("cvv"),
        DUPLICATE("duplicate"),
        FRAUD("fraud"),
        RISK_THRESHOLD("risk_threshold"),
        THREE_D_SECURE("three_d_secure"),
        TOKEN_ISSUANCE("token_issuance"),
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

    public enum IndustryType {
        LODGING("lodging"),
        TRAVEL_CRUISE("travel_cruise"),
        TRAVEL_FLIGHT("travel_flight");

        private final String name;

        IndustryType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
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
    private String graphqlId;
    private String id;
    private String merchantAccountId;
    private String orderId;
    private PayPalDetails paypalDetails;
    private PayPalHereDetails paypalHereDetails;
    private ApplePayDetails applePayDetails;
    private AndroidPayDetails androidPayDetails;
    private AmexExpressCheckoutDetails amexExpressCheckoutDetails;
    private VenmoAccountDetails venmoAccountDetails;
    private UsBankAccountDetails usBankAccountDetails;
    private VisaCheckoutCardDetails visaCheckoutCardDetails;
    private MasterpassCardDetails masterpassCardDetails;
    private LocalPaymentDetails localPaymentDetails;
    private CustomActionsPaymentMethodDetails customActionsPaymentMethodDetails;
    private String planId;
    private boolean processedWithNetworkToken;
    private String processorAuthorizationCode;
    private String processorResponseCode;
    private String processorResponseText;
    private ProcessorResponseType processorResponseType;
    private String processorSettlementResponseCode;
    private String processorSettlementResponseText;
    private String additionalProcessorResponse;
    private String networkResponseCode;
    private String networkResponseText;
    private String voiceReferralNumber;
    private String purchaseOrderNumber;
    private Boolean recurring;
    private String refundedTransactionId;
    private String refundId;
    private List<String> refundIds;
    private String retrievalReferenceNumber;
    private SamsungPayCardDetails samsungPayCardDetails;
    private String settlementBatchId;
    private Address shippingAddress;
    private Status status;
    private List<StatusEvent> statusHistory;
    private String subscriptionId;
    private Subscription subscription;
    private SubscriptionDetails subscriptionDetails;
    private BigDecimal taxAmount;
    private Boolean taxExempt;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private String shipsFromPostalCode;
    private Type type;
    private Calendar updatedAt;
    private BigDecimal serviceFeeAmount;
    private String paymentInstrumentType;
    private RiskData riskData;
    private ThreeDSecureInfo threeDSecureInfo;
    private String authorizedTransactionId;
    private List<String> partialSettlementTransactionIds;
    private List<AuthorizationAdjustment> authorizationAdjustments;
    private FacilitatedDetails facilitatedDetails;
    private FacilitatorDetails facilitatorDetails;
    private String networkTransactionId;
    private Calendar authorizationExpiresAt;

    public Transaction(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        avsErrorResponseCode = node.findString("avs-error-response-code");
        avsPostalCodeResponseCode = node.findString("avs-postal-code-response-code");
        avsStreetAddressResponseCode = node.findString("avs-street-address-response-code");
        channel = node.findString("channel");
        createdAt = node.findDateTime("created-at");
        currencyIsoCode = node.findString("currency-iso-code");
        customFields = node.findMap("custom-fields/*");
        cvvResponseCode = node.findString("cvv-response-code");
        escrowStatus = EnumUtils.findByName(EscrowStatus.class, node.findString("escrow-status"), EscrowStatus.UNRECOGNIZED);
        gatewayRejectionReason = EnumUtils.findByName(GatewayRejectionReason.class, node.findString("gateway-rejection-reason"), GatewayRejectionReason.UNRECOGNIZED);
        graphqlId = node.findString("global-id");
        id = node.findString("id");
        merchantAccountId = node.findString("merchant-account-id");
        orderId = node.findString("order-id");
        NodeWrapper billingAddressNode = node.findFirst("billing");
        if (billingAddressNode != null) {
            billingAddress = new Address(billingAddressNode);
        }
        NodeWrapper creditCardNode = node.findFirst("credit-card");
        if (creditCardNode != null) {
            creditCard = new CreditCard(creditCardNode);
        }
        NodeWrapper customerNode = node.findFirst("customer");
        if (customerNode != null) {
            customer = new Customer(customerNode);
        }
        NodeWrapper disbursementDetailsNode = node.findFirst("disbursement-details");
        if (disbursementDetailsNode != null) {
            disbursementDetails = new DisbursementDetails(disbursementDetailsNode);
        }
        NodeWrapper descriptorNode = node.findFirst("descriptor");
        if (descriptorNode != null) {
            descriptor = new Descriptor(descriptorNode);
        }
        NodeWrapper paypalNode = node.findFirst("paypal");
        if (paypalNode != null) {
            paypalDetails = new PayPalDetails(paypalNode);
        }
        NodeWrapper paypalHereNode = node.findFirst("paypal-here");
        if (paypalHereNode != null) {
            paypalHereDetails = new PayPalHereDetails(paypalHereNode);
        }
        NodeWrapper applePayNode = node.findFirst("apple-pay");
        if (applePayNode != null) {
            applePayDetails = new ApplePayDetails(applePayNode);
        }
        NodeWrapper androidPayCardNode = node.findFirst("android-pay-card");
        if (androidPayCardNode != null) {
            androidPayDetails = new AndroidPayDetails(androidPayCardNode);
        }
        NodeWrapper amexExpressCheckoutCardNode = node.findFirst("amex-express-checkout-card");
        if (amexExpressCheckoutCardNode != null) {
            amexExpressCheckoutDetails = new AmexExpressCheckoutDetails(amexExpressCheckoutCardNode);
        }
        NodeWrapper venmoAccountNode = node.findFirst("venmo-account");
        if (venmoAccountNode != null) {
            venmoAccountDetails = new VenmoAccountDetails(venmoAccountNode);
        }
        NodeWrapper usBankAccountNode = node.findFirst("us-bank-account");
        if (usBankAccountNode != null) {
            usBankAccountDetails = new UsBankAccountDetails(usBankAccountNode);
        }
        NodeWrapper localPaymentNode = node.findFirst("local-payment");
        if (localPaymentNode != null) {
            localPaymentDetails = new LocalPaymentDetails(localPaymentNode);
        }
        NodeWrapper visaCheckoutCardNode = node.findFirst("visa-checkout-card");
        if (visaCheckoutCardNode != null) {
            visaCheckoutCardDetails = new VisaCheckoutCardDetails(visaCheckoutCardNode);
        }
        NodeWrapper masterpassCardNode = node.findFirst("masterpass-card");
        if (masterpassCardNode != null) {
            masterpassCardDetails = new MasterpassCardDetails(masterpassCardNode);
        }
        NodeWrapper samsungPayCardNode = node.findFirst("samsung-pay-card");
        if (samsungPayCardNode != null) {
            samsungPayCardDetails = new SamsungPayCardDetails(samsungPayCardNode);
        }
        NodeWrapper customActionsPaymentMethodNode = node.findFirst("custom-actions-payment-method");
        if (customActionsPaymentMethodNode != null) {
            customActionsPaymentMethodDetails = new CustomActionsPaymentMethodDetails(customActionsPaymentMethodNode);
        }
        planId = node.findString("plan-id");
        processedWithNetworkToken = node.findBoolean("processed-with-network-token");
        processorAuthorizationCode = node.findString("processor-authorization-code");
        processorResponseCode = node.findString("processor-response-code");
        processorResponseText = node.findString("processor-response-text");
        processorResponseType = EnumUtils.findByName(ProcessorResponseType.class, node.findString("processor-response-type"), ProcessorResponseType.UNRECOGNIZED);
        processorSettlementResponseCode = node.findString("processor-settlement-response-code");
        processorSettlementResponseText = node.findString("processor-settlement-response-text");
        additionalProcessorResponse = node.findString("additional-processor-response");
        networkResponseCode = node.findString("network-response-code");
        networkResponseText = node.findString("network-response-text");
        voiceReferralNumber = node.findString("voice-referral-number");
        purchaseOrderNumber = node.findString("purchase-order-number");
        recurring = node.findBoolean("recurring");
        refundedTransactionId = node.findString("refunded-transaction-id");
        refundId = node.findString("refund-id");

        NodeWrapper riskDataNode = node.findFirst("risk-data");
        if (riskDataNode != null) {
            riskData = new RiskData(riskDataNode);
        }

        NodeWrapper threeDSecureInfoNode = node.findFirst("three-d-secure-info");
        if (threeDSecureInfoNode != null && !threeDSecureInfoNode.isBlank()) {
            threeDSecureInfo = new ThreeDSecureInfo(threeDSecureInfoNode);
        }

        serviceFeeAmount = node.findBigDecimal("service-fee-amount");
        settlementBatchId = node.findString("settlement-batch-id");

        NodeWrapper shippingAddressNode = node.findFirst("shipping");
        if (shippingAddressNode != null) {
            shippingAddress = new Address(shippingAddressNode);
        }

        status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);

        NodeWrapper subscriptionNode = node.findFirst("subscription");
        if (subscriptionNode != null) {
            subscriptionDetails = new SubscriptionDetails(subscriptionNode);
            subscription = new Subscription(subscriptionNode);
        }
        subscriptionId = node.findString("subscription-id");
        taxAmount = node.findBigDecimal("tax-amount");
        taxExempt = node.findBoolean("tax-exempt");
        shippingAmount = node.findBigDecimal("shipping-amount");
        discountAmount = node.findBigDecimal("discount-amount");
        shipsFromPostalCode = node.findString("ships-from-postal-code");
        type = EnumUtils.findByName(Type.class, node.findString("type"), Type.UNRECOGNIZED);
        updatedAt = node.findDateTime("updated-at");

        refundIds = new ArrayList<String>();
        for (NodeWrapper refundIdNode : node.findAll("refund-ids/item")) {
            refundIds.add(refundIdNode.findString("."));
        }

        retrievalReferenceNumber = node.findString("retrieval-reference-number");

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

        authorizedTransactionId = node.findString("authorized-transaction-id");

        partialSettlementTransactionIds = new ArrayList<String>();
        for (NodeWrapper partialSettlementTransactionIdNode : node.findAll("partial-settlement-transaction-ids/*")) {
            partialSettlementTransactionIds.add(partialSettlementTransactionIdNode.findString("."));
        }

        authorizationAdjustments = new ArrayList<AuthorizationAdjustment>();
        for (NodeWrapper authorizationAdjustmentNode : node.findAll("authorization-adjustments/authorization-adjustment")) {
            authorizationAdjustments.add(new AuthorizationAdjustment(authorizationAdjustmentNode));
        }

        NodeWrapper facilitatedDetailsNode = node.findFirst("facilitated-details");
        if (facilitatedDetailsNode != null) {
            facilitatedDetails = new FacilitatedDetails(facilitatedDetailsNode);
        }

        NodeWrapper facilitatorDetailsNode = node.findFirst("facilitator-details");
        if (facilitatorDetailsNode != null) {
            facilitatorDetails = new FacilitatorDetails(facilitatorDetailsNode);
        }

        networkTransactionId = node.findString("network-transaction-id");

        authorizationExpiresAt = node.findDateTime("authorization-expires-at");
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

    public String getGraphQLId() {
        return graphqlId;
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

    public PayPalHereDetails getPayPalHereDetails() {
        return paypalHereDetails;
    }

    public ApplePayDetails getApplePayDetails() {
        return applePayDetails;
    }

    public AndroidPayDetails getAndroidPayDetails() {
        return androidPayDetails;
    }

    public AmexExpressCheckoutDetails getAmexExpressCheckoutDetails() {
        return amexExpressCheckoutDetails;
    }

    public VenmoAccountDetails getVenmoAccountDetails() {
        return venmoAccountDetails;
    }

    public UsBankAccountDetails getUsBankAccountDetails() {
        return usBankAccountDetails;
    }

    public LocalPaymentDetails getLocalPaymentDetails() {
        return localPaymentDetails;
    }

    public VisaCheckoutCardDetails getVisaCheckoutCardDetails() {
        return visaCheckoutCardDetails;
    }

    public MasterpassCardDetails getMasterpassCardDetails() {
        return masterpassCardDetails;
    }

    public SamsungPayCardDetails getSamsungPayCardDetails() {
        return samsungPayCardDetails;
    }

    public CustomActionsPaymentMethodDetails getCustomActionsPaymentMethodDetails() {
        return customActionsPaymentMethodDetails;
    }

    public String getPlanId() {
        return planId;
    }

    public boolean isProcessedWithNetworkToken() {
        return processedWithNetworkToken;
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

    public ProcessorResponseType getProcessorResponseType() {
        return processorResponseType;
    }

    public String getProcessorSettlementResponseCode() {
        return processorSettlementResponseCode;
    }

    public String getProcessorSettlementResponseText() {
        return processorSettlementResponseText;
    }

    public String getAdditionalProcessorResponse() {
        return additionalProcessorResponse;
    }

    public String getNetworkResponseCode() {
        return networkResponseCode;
    }

    public String getNetworkResponseText() {
        return networkResponseText;
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

    public List<String> getRefundIds() {
        return refundIds;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public RiskData getRiskData() {
        return riskData;
    }

    public ThreeDSecureInfo getThreeDSecureInfo() {
        return threeDSecureInfo;
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

    public SubscriptionDetails getSubscriptionDetails() {
        return subscriptionDetails;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public String getShipsFromPostalCode() {
        return shipsFromPostalCode;
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

    public String getAuthorizedTransactionId() {
        return authorizedTransactionId;
    }

    public List<String> getPartialSettlementTransactionIds() {
        return partialSettlementTransactionIds;
    }

    public List<AuthorizationAdjustment> getAuthorizationAdjustments() {
        return authorizationAdjustments;
    }

    public FacilitatedDetails getFacilitatedDetails() {
        return facilitatedDetails;
    }

    public FacilitatorDetails getFacilitatorDetails() {
        return facilitatorDetails;
    }

    public List<TransactionLineItem> getLineItems(BraintreeGateway gateway) {
        return gateway.transactionLineItem().findAll(id);
    }

    public String getNetworkTransactionId() {
        return networkTransactionId;
    }

    public Calendar getAuthorizationExpiresAt() {
        return authorizationExpiresAt;
    }
}
