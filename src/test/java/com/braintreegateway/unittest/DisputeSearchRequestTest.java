package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Dispute;
import com.braintreegateway.DisputeSearchRequest;

@SuppressWarnings("deprecation")
public class DisputeSearchRequestTest {

    @Test
    public void buildsSearchCriteriaForAllFields() {
        Calendar now = Calendar.getInstance();
        DisputeSearchRequest search = new DisputeSearchRequest();

        search.amountDisputed().is("100.00");
        search.amountWon().is("95.00");
        search.caseNumber().is("CASE-1");
        search.id().is("dispute-id");
        search.customerId().is("customer-id");
        search.kind().is(Dispute.Kind.CHARGEBACK);
        search.chargebackProtectionLevel().is(Dispute.ChargebackProtectionLevel.EFFORTLESS);
        search.protectionLevel().is(Dispute.ProtectionLevel.EFFORTLESS_CBP);
        search.preDisputeProgram().is(Dispute.PreDisputeProgram.NONE);
        search.merchantAccountId().is("merchant-account-id");
        search.reason().is(Dispute.Reason.CANCELLED_RECURRING_TRANSACTION);
        search.reasonCode().is("reason-code");
        search.receivedDate().greaterThanOrEqualTo(now);
        search.disbursementDate().greaterThanOrEqualTo(now);
        search.effectiveDate().greaterThanOrEqualTo(now);
        search.referenceNumber().is("reference-number");
        search.replyByDate().greaterThanOrEqualTo(now);
        search.status().is(Dispute.Status.OPEN);
        search.transactionId().is("transaction-id");
        search.transactionSource().is("transaction-source");

        String xml = search.toXML();
        // Assert every criterion is serialized as its own element (multi-value nodes carry type="array").
        assertAll("dispute search criteria",
                () -> assertTrue(xml.contains("<amount_disputed>"), xml),
                () -> assertTrue(xml.contains("<amount_won>"), xml),
                () -> assertTrue(xml.contains("<case_number><is>CASE-1</is></case_number>"), xml),
                () -> assertTrue(xml.contains("<id><is>dispute-id</is></id>"), xml),
                () -> assertTrue(xml.contains("<customerId><is>customer-id</is></customerId>"), xml),
                () -> assertTrue(xml.contains("<kind type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<chargebackProtectionLevel type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<protectionLevel type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<pre_dispute_program type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<merchant_account_id type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<reason type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<reason_code type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<received_date>"), xml),
                () -> assertTrue(xml.contains("<disbursement_date>"), xml),
                () -> assertTrue(xml.contains("<effective_date>"), xml),
                () -> assertTrue(xml.contains("<reference_number><is>reference-number</is></reference_number>"), xml),
                () -> assertTrue(xml.contains("<reply_by_date>"), xml),
                () -> assertTrue(xml.contains("<status type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<transaction_id><is>transaction-id</is></transaction_id>"), xml),
                () -> assertTrue(xml.contains("<transaction_source type=\"array\">"), xml));
    }
}
