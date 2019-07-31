package com.braintreegateway;

import com.braintreegateway.Transaction.GatewayRejectionReason;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public class CreditCardVerification {

    public enum Status {
        FAILED, GATEWAY_REJECTED, PROCESSOR_DECLINED, UNRECOGNIZED, VERIFIED
    }

    private BigDecimal amount;
    private String avsErrorResponseCode;
    private String avsPostalCodeResponseCode;
    private String avsStreetAddressResponseCode;
    private String currencyIsoCode;
    private String cvvResponseCode;
    private GatewayRejectionReason gatewayRejectionReason;
    private String processorResponseCode;
    private String processorResponseText;
    private ProcessorResponseType processorResponseType;
    private String networkResponseCode;
    private String networkResponseText;
    private String merchantAccountId;
    private Status status;
    private String id;
    private CreditCard creditCard;
    private Address billingAddress;
    private Calendar createdAt;
    private RiskData riskData;
    private ThreeDSecureInfo threeDSecureInfo;

    public CreditCardVerification(NodeWrapper node) {
        this.amount = node.findBigDecimal("amount");
        this.avsErrorResponseCode = node.findString("avs-error-response-code");
        this.avsPostalCodeResponseCode = node.findString("avs-postal-code-response-code");
        this.avsStreetAddressResponseCode = node.findString("avs-street-address-response-code");
        this.currencyIsoCode = node.findString("currency-iso-code");
        this.cvvResponseCode = node.findString("cvv-response-code");
        this.gatewayRejectionReason = EnumUtils.findByName(GatewayRejectionReason.class, node.findString("gateway-rejection-reason"), GatewayRejectionReason.UNRECOGNIZED);
        this.processorResponseCode = node.findString("processor-response-code");
        this.processorResponseText = node.findString("processor-response-text");
        this.processorResponseType = EnumUtils.findByName(ProcessorResponseType.class, node.findString("processor-response-type"), ProcessorResponseType.UNRECOGNIZED);
        this.networkResponseCode = node.findString("network-response-code");
        this.networkResponseText = node.findString("network-response-text");
        this.merchantAccountId = node.findString("merchant-account-id");
        this.status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);
        this.id = node.findString("id");

        NodeWrapper riskDataNode = node.findFirst("risk-data");
        if (riskDataNode != null) {
            this.riskData = new RiskData(riskDataNode);
        }

        NodeWrapper threeDSecureInfoNode = node.findFirst("three-d-secure-info");
        if (threeDSecureInfoNode != null && !threeDSecureInfoNode.isBlank()) {
            threeDSecureInfo = new ThreeDSecureInfo(threeDSecureInfoNode);
        }


        NodeWrapper creditCardNode = node.findFirst("credit-card");
        if(creditCardNode != null) {
            this.creditCard = new CreditCard(creditCardNode);
        }

        NodeWrapper billingAddressNode = node.findFirst("billing");
        if(billingAddressNode != null) {
            this.billingAddress = new Address(billingAddressNode);
        }

        this.createdAt = node.findDateTime("created-at");
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

    public String getCvvResponseCode() {
        return cvvResponseCode;
    }

    public String getId() {
        return id;
    }

    public RiskData getRiskData(){
        return riskData;

    }

    public ThreeDSecureInfo getThreeDSecureInfo() {
        return threeDSecureInfo;
    }

    public GatewayRejectionReason getGatewayRejectionReason() {
        return gatewayRejectionReason;
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

    public String getNetworkResponseCode() {
        return networkResponseCode;
    }

    public String getNetworkResponseText() {
        return networkResponseText;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public Status getStatus() {
        return status;
    }
}
