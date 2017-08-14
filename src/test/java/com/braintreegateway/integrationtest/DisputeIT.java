package com.braintreegateway.integrationtest;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Dispute;
import com.braintreegateway.DisputeEvidence;
import com.braintreegateway.DisputeSearchRequest;
import com.braintreegateway.DocumentUpload;
import com.braintreegateway.DocumentUploadRequest;
import com.braintreegateway.PaginatedCollection;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.braintreegateway.SandboxValues.Dispute.CHARGEBACK;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DisputeIT extends IntegrationTest {

    private Http http;

    @Before
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
    public void addFileEvidenceWhenIncorrectDocumentKindErrors() {
        String disputeId = createSampleDispute().getId();
        String documentId = createSampleDocument(DocumentUpload.Kind.IDENTITY_DOCUMENT).getId();

        Result<DisputeEvidence> result = gateway.dispute()
            .addFileEvidence(disputeId, documentId);

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_ADD_EVIDENCE_DOCUMENT_TO_DISPUTE, error.getCode());
        assertEquals("A document with kind other than Braintree::DocumentUpload::Kind::EvidenceDocument cannot be added to the dispute", error.getMessage());
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
    }

    @Test
    public void addTextEvidenceThrowsNotFoundExceptionWhenDisputeNotFound() {
        try {
            gateway.dispute().addTextEvidence("invalid-id", "evidence!");
            fail("DisputeGateway#addTextEvidence allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-id\" not found", exception.getMessage());
        }
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

        for(Dispute dispute : disputeCollection) {
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

        for(Dispute dispute : disputeCollection) {
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

        for(Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertEquals(2, disputes.size());
	}

	@Test
    public void searchDateRangeReturnsDispute() {
        List<Dispute> disputes = new ArrayList<Dispute>();
        DisputeSearchRequest request = new DisputeSearchRequest()
            .receivedDate()
            .between("03/03/2014", "03/05/2014");

        PaginatedCollection<Dispute> disputeCollection = gateway.dispute()
            .search(request);

        for(Dispute dispute : disputeCollection) {
            disputes.add(dispute);
        }

        assertEquals(1, disputes.size());
        assertEquals(disputes.get(0).getReceivedDate().get(Calendar.YEAR), 2014);
        assertEquals(disputes.get(0).getReceivedDate().get(Calendar.MONTH)+1, 3);
        assertEquals(disputes.get(0).getReceivedDate().get(Calendar.DAY_OF_MONTH), 4);
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
