package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.EnumUtils;
import java.util.Map;

import com.braintreegateway.SepaDirectDebitAccountDetails.MandateType;

public class PaymentMethodNonceDetailsSepaDirectDebit {
    private MandateType mandateType;
    private String bankReferenceToken;
    private String correlationId;
    private String ibanLastChars;
    private String merchantOrPartnerCustomerId;

    public PaymentMethodNonceDetailsSepaDirectDebit(NodeWrapper node) {
        bankReferenceToken = node.findString("bank-reference-token");
        correlationId = node.findString("correlation-id");
        ibanLastChars = node.findString("iban-last-chars");
        mandateType = EnumUtils.findByName(MandateType.class, node.findString("mandate-type"), MandateType.ONE_OFF);
        merchantOrPartnerCustomerId = node.findString("merchant-or-partner-customer-id");
    }

    public PaymentMethodNonceDetailsSepaDirectDebit(Map<String, Object> map) {
        bankReferenceToken = (String) map.get("bank-reference-token");
        correlationId = (String) map.get("correlation-id");
        ibanLastChars = (String) map.get("iban-last-chars");
        mandateType = EnumUtils.findByName(MandateType.class, (String) map.get("mandate-type"), MandateType.ONE_OFF);
        merchantOrPartnerCustomerId = (String) map.get("merchant-or-partner-customer-id");
    }

    public String getBankReferenceToken() {
        return bankReferenceToken;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getIbanLastChars() {
        return ibanLastChars;
    }

    public String getMandateType() {
        return mandateType.name();
    }

    public String getMerchantOrPartnerCustomerId() {
        return merchantOrPartnerCustomerId;
    }
}
