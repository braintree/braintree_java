package com.braintreegateway.integrationtest;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.Http;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Dispute;
import com.braintreegateway.DisputeEvidence;
import com.braintreegateway.DisputeSearchRequest;
import com.braintreegateway.DisputeStatusHistory;
import com.braintreegateway.DocumentUpload;
import com.braintreegateway.DocumentUploadRequest;
import com.braintreegateway.PaginatedCollection;
import com.braintreegateway.Result;
import com.braintreegateway.TextEvidenceRequest;
import com.braintreegateway.FileEvidenceRequest;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Calendar;
import java.util.List;

import static com.braintreegateway.SandboxValues.Dispute.CHARGEBACK;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DisputeIT extends IntegrationTest {

    private Http http;

    @BeforeEach
    public void createHttp() {
        http = new Http(this.gateway.getConfiguration());
    }

    @Test
    public void acceptChangesDisputeStatusToAccepted() {
        Dispute dispute = createSampleDispute();

        Result result = gateway.dispute()
            .accept(dispute.getId());

        assertTrue(result.isSuccess());

        Dispute updatedDispute = gateway.dispute()
            .find(dispute.getId());

        assertEquals(Dispute.Status.ACCEPTED, updatedDispute.getStatus());

        Dispute disputeFromTransaction = gateway.transaction()
            .find(dispute.getTransaction().getId())
            .getDisputes()
            .get(0);

        assertEquals(Dispute.Status.ACCEPTED, disputeFromTransaction.getStatus());
    }

    @Test
    public void acceptWhenDisputeNotOpenErrors() {
        Result<Dispute> result = gateway.dispute()
            .accept("wells_dispute");

        assertFalse(result.isSuccess());

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_ACCEPT_OPEN_DISPUTE, error.getCode());
        assertEquals("Disputes can only be accepted when they are in an Open state", error.getMessage());
    }

    @Test
    public void acceptThrowsNotFoundExceptionWhenDisputeIsNotFound() {
        try {
            gateway.dispute().accept("invalid-id");
            fail("DisputeGateway#accept allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-id\" not found", exception.getMessage());
        }
    }

    @Test
    public void addFileEvidenceAddsEvidence() {
        String disputeId = createSampleDispute().getId();
        String documentId = createSampleDocument().getId();

        Result<DisputeEvidence> result = gateway.dispute().addFileEvidence(disputeId, documentId);
        String evidenceId = result.getTarget().getId();

        assertTrue(result.isSuccess());

        String updatedEvidenceId = gateway.dispute()
            .find(disputeId)
            .getEvidence()
            .get(0)
            .getId();

        assertEquals(evidenceId, updatedEvidenceId);
    }

    @Test
    public void addFileEvidenceAddsEvidenceWithCategory() {
        String disputeId = createSampleDispute().getId();
        String documentId = createSampleDocument().getId();
        FileEvidenceRequest request = new FileEvidenceRequest()
            .category("GENERAL")
            .documentId(documentId);

        Result<DisputeEvidence> result = gateway.dispute().addFileEvidence(disputeId, request);
        assertTrue(result.isSuccess());

        DisputeEvidence evidence = result.getTarget();

        assertEquals(evidence.getCategory(), "GENERAL");
    }

    @Test
    public void addFileEvidenceWithBadCategoryFails() {
        String disputeId = createSampleDispute().getId();
        String documentId = createSampleDocument().getId();
        FileEvidenceRequest request = new FileEvidenceRequest()
            .category("NOT A REAL CATEGORY")
            .documentId(documentId);

        Result<DisputeEvidence> result = gateway.dispute().addFileEvidence(disputeId, request);
        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.DISPUTE_CAN_ONLY_CREATE_EVIDENCE_WITH_VALID_CATEGORY,
                result.getErrors().forObject("dispute").onField("evidence").get(0).getCode()
                );
    }

    @Test
    public void addFileEvidenceThrowsNotFoundExceptionWhenDisputeOrDocumentIdIsNotFound() {
        try {
            gateway.dispute().addFileEvidence("invalid-dispute-id", "invalid-document-id");
            fail("DisputeGateway#addFileEvidence allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-dispute-id\" not found", exception.getMessage());
        }
    }

    @Test
    public void addFileEvidenceWhenDisputeNotOpenErrors() {
        String disputeId = createSampleDispute().getId();
        String documentId = createSampleDocument().getId();

        gateway.dispute()
            .accept(disputeId);

        Result<DisputeEvidence> result = gateway.dispute()
            .addFileEvidence(disputeId, documentId);

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_ADD_EVIDENCE_TO_OPEN_DISPUTE, error.getCode());
        assertEquals("Evidence can only be attached to disputes that are in an Open state", error.getMessage());
    }

    @Test
    public void addTextEvidenceAddsTextEvidence() {
        Dispute dispute = createSampleDispute();

        Result<DisputeEvidence> result = gateway.dispute()
            .addTextEvidence(dispute.getId(), "text evidence");
        DisputeEvidence evidence = result.getTarget();

        assertTrue(result.isSuccess());
        assertEquals(evidence.getComment(), "text evidence");
        assertNotNull(evidence.getCreatedAt());
        assertTrue(Pattern.matches("^\\w{16,}$", evidence.getId()));
        assertNull(evidence.getSentToProcessorAt());
        assertNull(evidence.getUrl());
        assertNull(evidence.getCategory());
        assertNull(evidence.getSequenceNumber());
    }

    @Test
    public void addTextEvidenceThrowsNotFoundExceptionWhenDisputeNotFound() {
        try {
            gateway.dispute().addTextEvidence("invalid-id", "evidence!");
            fail("DisputeGateway#addTextEvidence allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("Dispute with ID \"invalid-id\" not found", exception.getMessage());
        }
    }

    @Test
    public void addTextEvidenceWorksForProofOfFulfillment() {
        Dispute dispute = createSampleDispute();

        TextEvidenceRequest textEvidenceRequest = new TextEvidenceRequest().
            content("PROOF_OF_FULFILLMENT").
            category("EVIDENCE_TYPE");
        Result<DisputeEvidence> disputeEvidenceResult = gateway.dispute().addTextEvidence(dispute.getId(), textEvidenceRequest);

        textEvidenceRequest = new TextEvidenceRequest().
            content("UPS").
            category("CARRIER_NAME").
            sequenceNumber("0");
        Result<DisputeEvidence> carrierEvidenceZeroResult = gateway.dispute().addTextEvidence(dispute.getId(), textEvidenceRequest);

        textEvidenceRequest = new TextEvidenceRequest().
            content("3").
            category("TRACKING_NUMBER").
            sequenceNumber("0");
        Result<DisputeEvidence> trackingEvidenceZeroResult = gateway.dispute().addTextEvidence(dispute.getId(), textEvidenceRequest);

        textEvidenceRequest = new TextEvidenceRequest().
            content("https://example.com/tracking-number/abc12345").
            category("TRACKING_URL").
            sequenceNumber("1");
        Result<DisputeEvidence> trackingEvidenceOneResult = gateway.dispute().addTextEvidence(dispute.getId(), textEvidenceRequest);

        assertTrue(disputeEvidenceResult.isSuccess());
        DisputeEvidence disputeEvidence = disputeEvidenceResult.getTarget();
        assertEquals("PROOF_OF_FULFILLMENT", disputeEvidence.getComment());
        assertEquals("EVIDENCE_TYPE", disputeEvidence.getCategory());

        assertTrue(carrierEvidenceZeroResult.isSuccess());
        DisputeEvidence carrierEvidenceZero = carrierEvidenceZeroResult.getTarget();
        assertEquals("UPS", carrierEvidenceZero.getComment());
        assertEquals("CARRIER_NAME", carrierEvidenceZero.getCategory());
        assertEquals("0", carrierEvidenceZero.getSequenceNumber());

        assertTrue(trackingEvidenceZeroResult.isSuccess());
        DisputeEvidence trackingEvidenceZero = trackingEvidenceZeroResult.getTarget();
        assertEquals("3", trackingEvidenceZero.getComment());
        assertEquals("TRACKING_NUMBER", trackingEvidenceZero.getCategory());
        assertEquals("0", trackingEvidenceZero.getSequenceNumber());

        assertTrue(trackingEvidenceOneResult.isSuccess());
        DisputeEvidence trackingEvidenceOne = trackingEvidenceOneResult.getTarget();
        assertEquals("https://example.com/tracking-number/abc12345", trackingEvidenceOne.getComment());
        assertEquals("TRACKING_URL", trackingEvidenceOne.getCategory());
        assertEquals("1", trackingEvidenceOne.getSequenceNumber());
    }

    @Test
    public void addTextEvidenceWorksForProofOfRefund() {
        Dispute dispute = createSampleDispute();

        TextEvidenceRequest textEvidenceRequest = new TextEvidenceRequest().
            content("PROOF_OF_REFUND").
            category("EVIDENCE_TYPE");
        Result<DisputeEvidence> disputeEvidenceResult = gateway.dispute().addTextEvidence(dispute.getId(), textEvidenceRequest);

        textEvidenceRequest = new TextEvidenceRequest().
            content("1023").
            category("REFUND_ID");
        Result<DisputeEvidence> refundEvidenceResult = gateway.dispute().addTextEvidence(dispute.getId(), textEvidenceRequest);

        assertTrue(disputeEvidenceResult.isSuccess());
        DisputeEvidence disputeEvidence = disputeEvidenceResult.getTarget();
        assertEquals("PROOF_OF_REFUND", disputeEvidence.getComment());
        assertEquals("EVIDENCE_TYPE", disputeEvidence.getCategory());

        assertTrue(refundEvidenceResult.isSuccess());
        DisputeEvidence refundEvidence = refundEvidenceResult.getTarget();
        assertEquals("1023", refundEvidence.getComment());
        assertEquals("REFUND_ID", refundEvidence.getCategory());
        assertNull(refundEvidence.getSequenceNumber());
    }

    @Test
    public void addTextEvidenceWhenDisputeNotOpenErrors() {
        Dispute dispute = createSampleDispute();

        gateway.dispute().accept(dispute.getId());
        Result<DisputeEvidence> result = gateway.dispute().addTextEvidence(dispute.getId(), "text evidence");

        assertFalse(result.isSuccess());

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_ADD_EVIDENCE_TO_OPEN_DISPUTE, error.getCode());
        assertEquals("Evidence can only be attached to disputes that are in an Open state", error.getMessage());
    }


    @Test
    public void addTextEvidenceWithUnsupportedCategoryFails() {
        Dispute dispute = createSampleDispute();

        gateway.dispute().accept(dispute.getId());

        TextEvidenceRequest textEvidenceRequest = new TextEvidenceRequest()
            .category("NOTACATEGORY")
            .content("this was paid for by the customer");

        Result<DisputeEvidence> result = gateway.dispute()
            .addTextEvidence(dispute.getId(), textEvidenceRequest);

        assertFalse(result.isSuccess());

        assertEquals(
                ValidationErrorCode.DISPUTE_CAN_ONLY_CREATE_EVIDENCE_WITH_VALID_CATEGORY,
                result.getErrors().forObject("dispute").onField("evidence").get(0).getCode()
                );
    }

    @Test
    public void addTextEvidenceShowsNewRecordInFind() {
        Dispute dispute = createSampleDispute();

        DisputeEvidence evidence = gateway.dispute()
            .addTextEvidence(dispute.getId(), "text evidence")
            .getTarget();

        DisputeEvidence refreshedEvidence = gateway.dispute()
            .find(dispute.getId())
            .getEvidence()
            .get(0);

        assertEquals(evidence.getId(), refreshedEvidence.getId());
        assertEquals(evidence.getComment(), refreshedEvidence.getComment());
    }

    @Test
    public void finalizeChangesDisputeStatusToDisputed() {
        Dispute dispute = createSampleDispute();

        Result result = gateway.dispute()
            .finalize(dispute.getId());

        assertTrue(result.isSuccess());

        Dispute updatedDispute = gateway.dispute()
            .find(dispute.getId());

        assertEquals(Dispute.Status.DISPUTED, updatedDispute.getStatus());
    }

    @Test
    public void finalizeWhenDisputeNotOpenErrors() {
        Result<Dispute> result = gateway.dispute()
            .finalize("wells_dispute");

        assertFalse(result.isSuccess());

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_FINALIZE_OPEN_DISPUTE, error.getCode());
        assertEquals("Disputes can only be finalized when they are in an Open state", error.getMessage());
    }

    @Test
    public void finalizeWhenDisputeHasValidationErrors() {
        Dispute dispute = createSampleDispute();
        TextEvidenceRequest textEvidenceRequest = new TextEvidenceRequest().
            content("content").
            category("DEVICE_ID");

        gateway.dispute().addTextEvidence(dispute.getId(), textEvidenceRequest);

        Result<Dispute> result = gateway.dispute().finalize(dispute.getId());

        assertFalse(result.isSuccess());

        List<ValidationError> errors = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors();

        Set<ValidationErrorCode> codes = new HashSet<ValidationErrorCode>();
        for (ValidationError e : errors) {
            codes.add(e.getCode());
        }

        Set<ValidationErrorCode> expectedCodes = new HashSet<ValidationErrorCode>();
        expectedCodes.add(ValidationErrorCode.DISPUTE_DIGITAL_GOODS_MISSING_EVIDENCE);
        expectedCodes.add(ValidationErrorCode.DISPUTE_DIGITAL_GOODS_MISSING_DOWNLOAD_DATE);

        assertEquals(expectedCodes, codes);
    }

    @Test
    public void finalizeWhenDisputeNotFoundErrors() {
        try {
            gateway.dispute().finalize("invalid-id");
            fail("DisputeGateway#finalize allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-id\" not found", exception.getMessage());
        }
    }

    @Test
    public void findReturnsDisputeWithGivenId() {
        Dispute dispute = gateway.dispute().find("open_dispute");

        assertEquals(new BigDecimal("31.00"), dispute.getDisputedAmount());
        assertEquals(new BigDecimal("0.00"), dispute.getWonAmount());
        assertEquals("open_dispute", dispute.getId());
        assertEquals(Dispute.Status.OPEN, dispute.getStatus());
        assertEquals("open_disputed_transaction", dispute.getTransaction().getId());
        assertNotNull(dispute.getGraphQLId());
    }

    @Test
    public void findThrowsNotFoundExceptionWhenDisputeNotFound() {
        try {
            gateway.dispute().find("invalid-id");
            fail("DisputeGateway#find allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-id\" not found", exception.getMessage());
        }
    }

    @Test
    public void removeEvidenceRemovesEvidenceFromTheDispute() {
        String disputeId = createSampleDispute().getId();
        String evidenceId = gateway.dispute()
            .addTextEvidence(disputeId, "text evidence")
            .getTarget()
            .getId();

        Result<Dispute> result = gateway.dispute()
            .removeEvidence(disputeId, evidenceId);

        assertTrue(result.isSuccess());
    }

    @Test
    public void removeEvidenceWhenDisputeOrEvidenceNotFoundThrowsNotFoundException() {
        try {
            gateway.dispute().removeEvidence("invalid-dispute-id", "invalid-evidence-id");
            fail("DisputeGateway#removeEvidence allowed an invalid id and/or invalid evidence id");
        } catch (NotFoundException exception) {
            assertEquals("evidence with id \"invalid-evidence-id\" for dispute with id \"invalid-dispute-id\" not found", exception.getMessage());
        }
    }

    @Test
    public void removeEvidenceErrorsWhenDisputeNotOpen() {
        String disputeId = createSampleDispute().getId();
        String evidenceId = gateway.dispute()
            .addTextEvidence(disputeId, "text evidence")
            .getTarget()
            .getId();

        gateway.dispute().accept(disputeId);

        Result<Dispute> result = gateway.dispute()
            .removeEvidence(disputeId, evidenceId);

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_REMOVE_EVIDENCE_FROM_OPEN_DISPUTE, error.getCode());
        assertEquals("Evidence can only be removed from disputes that are in an Open state", error.getMessage());
    }

    @Test
    public void searchWithEmptyResult() {
        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
            .id().is("non_existent_dispute");

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertEquals(0, disputes.size());
    }

    @Test
    public void searchByIdReturnsSingleDispute() {
        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
            .id().is("open_dispute");

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertEquals(1, disputes.size());
	}

	@Test
	public void searchByCustomerIdReturnsSingleDispute() {
        String customerId = gateway.customer().create(new CustomerRequest()).getTarget().getId();

        TransactionRequest transactionRequest = new TransactionRequest()
            .amount(new BigDecimal("10.00"))
            .customerId(customerId)
            .creditCard()
                .number(CHARGEBACK)
                .expirationDate("12/2020")
            .done();

        gateway.transaction().sale(transactionRequest);

        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
            .customerId().is(customerId);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertEquals(1, disputes.size());
	}

	@Test
	public void searchWithMultipleReasonsReturnsMultipleDisputes() {
        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
            .reason()
            .in(Dispute.Reason.PRODUCT_UNSATISFACTORY, Dispute.Reason.RETRIEVAL);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertTrue(disputes.size() >= 2);
	}

    @Test
    public void searchWithChargebackProtectionLevelReturnsDispute() {
        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
                .chargebackProtectionLevel()
                .in(Dispute.ChargebackProtectionLevel.EFFORTLESS);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
                .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertTrue(disputes.size() > 0);

        for (Dispute dispute: disputes) {
            // NEXT_MAJOR_VERSION Remove this assertion when chargebackProtectionLevel is removed from the SDK
            assertEquals(dispute.getChargebackProtectionLevel(), Dispute.ChargebackProtectionLevel.EFFORTLESS);
            assertEquals(dispute.getProtectionLevel(), Dispute.ProtectionLevel.EFFORTLESS_CBP);
        }
    }

    @Test
    public void searchWithPreDisputeProgramReturnsDispute() {
        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
                .preDisputeProgram()
                .in(Dispute.PreDisputeProgram.VISA_RDR);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
                .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertEquals(1, disputes.size());
        assertEquals(disputes.get(0).getPreDisputeProgram(), Dispute.PreDisputeProgram.VISA_RDR);
    }

    @Test
    public void searchForNonPreDisputesReturnsDisputes() {
        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
                .preDisputeProgram()
                .is(Dispute.PreDisputeProgram.NONE);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
                .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }
        assertTrue(disputes.size() > 1);

        Set<Dispute.PreDisputeProgram> preDisputePrograms = disputes.stream().map(Dispute::getPreDisputeProgram).collect(Collectors.toSet());
        assertEquals(1, preDisputePrograms.size());
        assertTrue(preDisputePrograms.contains(Dispute.PreDisputeProgram.NONE));
    }

	@Test
    public void searchReceivedDateRangeReturnsDispute() throws ParseException{
        List<Dispute> disputes = new ArrayList<Dispute>();
        Calendar before = CalendarTestUtils.date("2014-03-03");
        Calendar after = CalendarTestUtils.date("2014-03-05");
        DisputeSearchRequest request = new DisputeSearchRequest()
            .receivedDate()
            .between(before, after);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertTrue(disputes.size() >= 1);
        assertEquals(disputes.get(0).getReceivedDate().get(Calendar.YEAR), 2014);
        assertEquals(disputes.get(0).getReceivedDate().get(Calendar.MONTH)+1, 3);
        assertEquals(disputes.get(0).getReceivedDate().get(Calendar.DAY_OF_MONTH), 4);
    }

	@Test
    public void searchDisbursementDateRangeReturnsDispute() throws ParseException{
        List<Dispute> disputes = new ArrayList<Dispute>();
        Calendar before = CalendarTestUtils.date("2014-03-03");
        Calendar after = CalendarTestUtils.date("2014-03-05");
        DisputeSearchRequest request = new DisputeSearchRequest()
            .disbursementDate()
            .between(before, after);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertTrue(disputes.size() >= 1);
        DisputeStatusHistory history = disputes.get(0).getStatusHistory().get(0);
        assertEquals(history.getDisbursementDate().get(Calendar.YEAR), 2014);
        assertEquals(history.getDisbursementDate().get(Calendar.MONTH)+1, 3);
        assertEquals(history.getDisbursementDate().get(Calendar.DAY_OF_MONTH), 5);
    }

	@Test
    public void searchEffectiveDateRangeReturnsDispute() throws ParseException{
        List<Dispute> disputes = new ArrayList<Dispute>();
        Calendar before = CalendarTestUtils.date("2014-03-03");
        Calendar after = CalendarTestUtils.date("2014-03-05");
        DisputeSearchRequest request = new DisputeSearchRequest()
            .effectiveDate()
            .between(before, after);

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for (Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertTrue(disputes.size() >= 1);
        DisputeStatusHistory history = disputes.get(0).getStatusHistory().get(0);
        assertEquals(history.getEffectiveDate().get(Calendar.YEAR), 2014);
        assertEquals(history.getEffectiveDate().get(Calendar.MONTH)+1, 3);
        assertEquals(history.getEffectiveDate().get(Calendar.DAY_OF_MONTH), 4);
    }

    public Dispute createSampleDispute() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CHARGEBACK).
                expirationDate("12/2019").
                done();

        Result<Transaction> result = gateway.transaction()
            .sale(request);

        if (!result.isSuccess()) {
            String reason = result.getTransaction().getProcessorResponseText();
            throw new RuntimeException("Could not create a transaction: " + reason);
        }

        return result.getTarget()
            .getDisputes()
            .get(0);
    }

    public DocumentUpload createSampleDocument() {
        return createSampleDocument(DocumentUpload.Kind.EVIDENCE_DOCUMENT);
    }

    public DocumentUpload createSampleDocument(DocumentUpload.Kind kind) {
        URL fileToUpload = getClass().getClassLoader().getResource("fixtures/bt_logo.png");
        DocumentUploadRequest uploadRequest = new DocumentUploadRequest(
                kind,
                new File(fileToUpload.getFile())
        );

        Result<DocumentUpload> uploadResult = gateway.documentUpload().create(uploadRequest);
        DocumentUpload documentUpload = uploadResult.getTarget();

        return documentUpload;
    }
}
