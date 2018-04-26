package com.braintreegateway;

import com.braintreegateway.Transaction.GatewayRejectionReason;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public class UsBankAccountVerification {

    public enum Status {
        FAILED("failed"),
        GATEWAY_REJECTED("gateway_rejected"),
        PROCESSOR_DECLINED("processor_declined"),
        UNRECOGNIZED("unrecognized"),
        VERIFIED("verified"),
        PENDING("pending");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum VerificationMethod {
        TOKENIZED_CHECK("tokenized_check"),
        NETWORK_CHECK("network_check"),
        INDEPENDENT_CHECK("independent_check"),
        UNRECOGNIZED("unrecognized"),
        MICRO_TRANSFERS("micro_transfers");

        private final String name;

        VerificationMethod(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private String id;
    private Status status;
    private VerificationMethod verificationMethod;
    private String processorResponseCode;
    private String processorResponseText;
    private Calendar verificationDeterminedAt;
    private Calendar createdAt;
    private GatewayRejectionReason gatewayRejectionReason;
    private UsBankAccount usBankAccount;

    public UsBankAccountVerification(NodeWrapper node) {
        this.id = node.findString("id");
        this.status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);
        this.verificationMethod = EnumUtils.findByName(
            VerificationMethod.class,
            node.findString("verification-method"),
            VerificationMethod.UNRECOGNIZED
        );
        this.processorResponseCode = node.findString("processor-response-code");
        this.processorResponseText = node.findString("processor-response-text");
        this.createdAt = node.findDateTime("created-at");
        this.verificationDeterminedAt = node.findDateTime("verification-determined-at");
        this.gatewayRejectionReason = EnumUtils.findByName(GatewayRejectionReason.class, node.findString("gateway-rejection-reason"), GatewayRejectionReason.UNRECOGNIZED);
        NodeWrapper usBankAccountNode = node.findFirst("us-bank-account");
        if (usBankAccountNode != null) {
            this.usBankAccount = new UsBankAccount(usBankAccountNode);
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public GatewayRejectionReason getGatewayRejectionReason() {
        return gatewayRejectionReason;
    }

    public VerificationMethod getVerificationMethod() {
        return verificationMethod;
    }

    public Calendar getVerificationDeterminedAt() {
        return verificationDeterminedAt;
    }

    public String getProcessorResponseCode() {
        return processorResponseCode;
    }

    public String getProcessorResponseText() {
        return processorResponseText;
    }

    public Status getStatus() {
        return status;
    }

    public UsBankAccount getUsBankAccount() {
        return usBankAccount;
    }
}
