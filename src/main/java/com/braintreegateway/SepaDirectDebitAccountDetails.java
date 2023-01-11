package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.EnumUtils;

public class SepaDirectDebitAccountDetails {
    public enum MandateType {
        ONE_OFF("ONE_OFF"),
        RECURRENT("RECURRENT");

        private final String name;

        MandateType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum SettlementType {
        INSTANT("INSTANT"),
        DELAYED("DELAYED");

        private final String name;

        SettlementType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private MandateType mandateType;
    private SettlementType settlementType;
    private String bankReferenceToken;
    private String captureId;
    private String merchantOrPartnerCustomerId;
    private String debugId;
    private String last4;
    private String paypalV2OrderId;
    private String refundFromTransactionFeeAmount;
    private String refundFromTransactionFeeCurrencyIsoCode;
    private String refundId;
    private String token;
    private String transactionFeeAmount;
    private String transactionFeeCurrencyIsoCode;

    public SepaDirectDebitAccountDetails(NodeWrapper node) {
        bankReferenceToken = node.findString("bank-reference-token");
        captureId = node.findString("capture-id");
        debugId = node.findString("debug-id");
        last4 = node.findString("last-4");
        mandateType = EnumUtils.findByName(MandateType.class, node.findString("mandate-type"), MandateType.ONE_OFF);
        merchantOrPartnerCustomerId = node.findString("merchant-or-partner-customer-id");
        paypalV2OrderId = node.findString("paypal-v2-order-id");
        refundFromTransactionFeeAmount = node.findString("refund-from-transaction-fee-amount");
        refundFromTransactionFeeCurrencyIsoCode = node.findString("refund-from-transaction-fee-currency-iso-code");
        refundId = node.findString("refund-id");
        settlementType = EnumUtils.findByName(SettlementType.class, node.findString("settlement-type"), SettlementType.INSTANT);
        token = node.findString("token");
        transactionFeeAmount = node.findString("transaction-fee-amount");
        transactionFeeCurrencyIsoCode = node.findString("transaction-fee-currency-iso-code");
    }

    public String getPayPalV2OrderId() {
        return paypalV2OrderId;
    }

    public MandateType getMandateType() {
        return mandateType;
    }

    public String getBankReferenceToken() {
        return bankReferenceToken;
    }

    public String getMerchantOrPartnerCustomerId() {
        return merchantOrPartnerCustomerId;
    }

    public String getLast4() {
       return last4;
    }

    public String getDebugId() {
        return debugId;
    }

    public String getRefundId() {
        return refundId;
    }

    public String getCaptureId() {
        return captureId;
    }

    public String getTransactionFeeAmount() {
        return transactionFeeAmount;
    }

    public String getTransactionFeeCurrencyIsoCode() {
        return transactionFeeCurrencyIsoCode;
    }

    public String getRefundFromTransactionFeeAmount() {
        return refundFromTransactionFeeAmount;
    }

    public String getRefundFromTransactionFeeCurrencyIsoCode() {
        return refundFromTransactionFeeCurrencyIsoCode;
    }

    public SettlementType getSettlementType() {
        return settlementType;
    }

    public String getToken() {
        return token;
    }
}
