package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Dispute {
    public enum Status {
        OPEN,
        LOST,
        WON,
        UNRECOGNIZED,
        ACCEPTED,
        DISPUTED,
        EXPIRED
    }

    public enum Reason {
        // This should be CANCELED but the Braintree Gateway
        // returns this as CANCELLED.
        CANCELLED_RECURRING_TRANSACTION,
        CREDIT_NOT_PROCESSED,
        DUPLICATE,
        FRAUD,
        GENERAL,
        INVALID_ACCOUNT,
        NOT_RECOGNIZED,
        PRODUCT_NOT_RECEIVED,
        PRODUCT_UNSATISFACTORY,
        TRANSACTION_AMOUNT_DIFFERS,
        RETRIEVAL;
    }

    public enum Kind {
        CHARGEBACK,
        PRE_ARBITRATION,
        RETRIEVAL,
        UNRECOGNIZED;
    }

    // NEXT_MAJOR_VERSION Remove this enum
    /**
     * @deprecated use ProtectionLevel enum instead
     */
    @Deprecated
    public enum ChargebackProtectionLevel {
        EFFORTLESS,
        STANDARD,
        NOT_PROTECTED;
    }

    public enum ProtectionLevel {
        EFFORTLESS_CBP("Effortless Chargeback Protection tool"),
        STANDARD_CBP("Chargeback Protection tool"),
        NO_PROTECTION("No Protection");

        private final String value;

        ProtectionLevel(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum PreDisputeProgram {
        NONE,
        UNRECOGNIZED,
        VISA_RDR;
    }

    private final Calendar createdAt;
    private final Calendar receivedDate;
    private final Calendar replyByDate;
    private final Calendar openedDate;
    private final Calendar updatedAt;
    private final Calendar wonDate;
    private final List<DisputeEvidence> evidence;
    private final List<DisputePayPalMessage> paypalMessages;
    private final List<DisputeStatusHistory> statusHistory;
    private final String caseNumber;
    private final String currencyIsoCode;
    private final String graphqlId;
    private final String id;
    private final String processorComments;
    private final String merchantAccountId;
    private final String originalDisputeId;
    private final String reasonCode;
    private final String reasonDescription;
    private final String referenceNumber;
    private final Reason reason;
    private final Status status;
    private final Kind kind;
    @Deprecated
    private final ChargebackProtectionLevel chargebackProtectionLevel; // Deprecated
    private final ProtectionLevel protectionLevel;
    private final PreDisputeProgram preDisputeProgram;
    private final BigDecimal amount;
    private final BigDecimal disputedAmount;
    private final BigDecimal wonAmount;
    private final TransactionDetails transactionDetails;
    private final DisputeTransaction transaction;

    public Dispute(NodeWrapper node) {
        createdAt = node.findDateTime("created-at");
        receivedDate = node.findDate("received-date");
        replyByDate = node.findDate("reply-by-date");
        openedDate = node.findDate("date-opened");
        updatedAt = node.findDateTime("updated-at");
        wonDate = node.findDate("date-won");
        caseNumber = node.findString("case-number");
        currencyIsoCode = node.findString("currency-iso-code");
        processorComments = node.findString("processor-comments");
        merchantAccountId = node.findString("merchant-account-id");
        originalDisputeId = node.findString("original-dispute-id");
        reasonCode = node.findString("reason-code");
        reasonDescription = node.findString("reason-description");
        referenceNumber = node.findString("reference-number");
        reason = EnumUtils.findByName(Reason.class, node.findString("reason"), Reason.GENERAL);
        status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);
        kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        chargebackProtectionLevel = EnumUtils.findByName(ChargebackProtectionLevel.class, node.findString("chargeback-protection-level"), ChargebackProtectionLevel.NOT_PROTECTED);
        switch(String.valueOf(node.findString("chargeback-protection-level"))) {
            case "effortless":
                protectionLevel = ProtectionLevel.EFFORTLESS_CBP;
                break;
            case "standard":
                protectionLevel = ProtectionLevel.STANDARD_CBP;
                break;
            default:
                protectionLevel = ProtectionLevel.NO_PROTECTION;
        }
        preDisputeProgram = EnumUtils.findByName(PreDisputeProgram.class, node.findString("pre-dispute-program"), PreDisputeProgram.UNRECOGNIZED);

        amount = node.findBigDecimal("amount");
        disputedAmount = node.findBigDecimal("amount-disputed");
        wonAmount = node.findBigDecimal("amount-won");
        graphqlId = node.findString("global-id");
        id = node.findString("id");
        transaction = new DisputeTransaction(node.findFirst("transaction"));
        transactionDetails = new TransactionDetails(node.findFirst("transaction"));

        evidence = new ArrayList<DisputeEvidence>();
        for (NodeWrapper evidenceNode : node.findAll("evidence/evidence")) {
            evidence.add(new DisputeEvidence(evidenceNode));
        }

        paypalMessages = new ArrayList<DisputePayPalMessage>();
        for (NodeWrapper paypalMessageNode : node.findAll("paypal-messages/paypal-messages")) {
            paypalMessages.add(new DisputePayPalMessage(paypalMessageNode));
        }

        statusHistory = new ArrayList<DisputeStatusHistory>();
        for (NodeWrapper statusNode : node.findAll("status-history/status-history")) {
            statusHistory.add(new DisputeStatusHistory(statusNode));
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getReceivedDate() {
        return receivedDate;
    }

    public Calendar getReplyByDate() {
        return replyByDate;
    }

    public Calendar getOpenedDate() {
        return openedDate;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public Calendar getWonDate() {
        return wonDate;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public String getProcessorComments() {
        return processorComments;
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

    public String getOriginalDisputeId() {
        return originalDisputeId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public Reason getReason() {
        return reason;
    }

    public Status getStatus() {
        return status;
    }

    public Kind getKind() {
        return kind;
    }

    /**
     * @deprecated use getProtectionLevel() instead
     */
    @Deprecated
    public ChargebackProtectionLevel getChargebackProtectionLevel() {
        return chargebackProtectionLevel;
    }

    public ProtectionLevel getProtectionLevel() {
        return protectionLevel;
    }

    public PreDisputeProgram getPreDisputeProgram() {
        return preDisputeProgram;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getDisputedAmount() {
        return disputedAmount;
    }

    public BigDecimal getWonAmount() {
        return wonAmount;
    }

    public List<DisputeEvidence> getEvidence() {
        return evidence;
    }

    public List<DisputePayPalMessage> getPayPalMessages() {
        return paypalMessages;
    }

    public List<DisputeStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public DisputeTransaction getTransaction() {
        return transaction;
    }
}
