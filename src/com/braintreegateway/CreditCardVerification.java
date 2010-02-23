package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class CreditCardVerification {

    private String avsErrorResponseCode;
    private String avsPostalCodeResponseCode;
    private String avsStreetAddressResponseCode;
    private String cvvResponseCode;
    private String processorResponseCode;
    private String processorResponseText;
    private String status;

    public CreditCardVerification(NodeWrapper node) {
        node = node.findFirst("verification");

        if (node != null) {
            this.avsErrorResponseCode = node.findString("avs-error-response-code");
            this.avsPostalCodeResponseCode = node.findString("avs-postal-code-response-code");
            this.avsStreetAddressResponseCode = node.findString("avs-street-address-response-code");
            this.cvvResponseCode = node.findString("cvv-response-code");
            this.processorResponseCode = node.findString("processor-response-code");
            this.processorResponseText = node.findString("processor-response-text");
            this.status = node.findString("status");
        }
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

    public String getProcessorResponseCode() {
        return processorResponseCode;
    }

    public String getProcessorResponseText() {
        return processorResponseText;
    }

    public String getStatus() {
        return status;
    }
}
