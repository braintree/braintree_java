package com.braintreegateway;

import com.braintreegateway.Transaction.GatewayRejectionReason;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class CreditCardVerification {

    public enum Status {
        FAILED, GATEWAY_REJECTED, PROCESSOR_DECLINED, UNRECOGNIZED, VERIFIED
    }

    private String avsErrorResponseCode;
    private String avsPostalCodeResponseCode;
    private String avsStreetAddressResponseCode;
    private String cvvResponseCode;
    private GatewayRejectionReason gatewayRejectionReason;
    private String processorResponseCode;
    private String processorResponseText;
    private String merchantAccountId;
    private Status status;

    public CreditCardVerification(NodeWrapper node) {
        this.avsErrorResponseCode = node.findString("avs-error-response-code");
        this.avsPostalCodeResponseCode = node.findString("avs-postal-code-response-code");
        this.avsStreetAddressResponseCode = node.findString("avs-street-address-response-code");
        this.cvvResponseCode = node.findString("cvv-response-code");
        this.gatewayRejectionReason = EnumUtils.findByName(GatewayRejectionReason.class, node.findString("gateway-rejection-reason"));
        this.processorResponseCode = node.findString("processor-response-code");
        this.processorResponseText = node.findString("processor-response-text");
        this.merchantAccountId = node.findString("merchant-account-id");
        this.status = EnumUtils.findByName(Status.class, node.findString("status"));
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

    public String getCvvResponseCode() {
        return cvvResponseCode;
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

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public Status getStatus() {
        return status;
    }
}
