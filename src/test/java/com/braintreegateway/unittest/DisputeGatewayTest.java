package com.braintreegateway.unittest;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.DisputeGateway;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DisputeGatewayTest {

    @Test
    public void acceptNullRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).accept(null);
            fail("DisputeGateway#accept allowed null");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \"null\" not found");
        }
    }

    @Test
    public void acceptEmptyStringRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).accept(" ");
            fail("DisputeGateway#accept allowed empty string");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \" \" not found");
        }
    }

    @Test
    public void addTextEvidenceNullIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).addTextEvidence(null, "content");
            fail("DisputeGateway#addTextEvidence allowed null ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \"null\" not found");
        }
    }

    @Test
    public void addTextEvidenceEmptyStringIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).addTextEvidence(" ", "content");
            fail("DisputeGateway#addTextEvidence allowed empty string ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \" \" not found");
        }
    }

    @Test
    public void addTextEvidenceNullContentRaisesIllegalArgumentException() {
        try {
            new DisputeGateway(null, null).addTextEvidence("id", null);
            fail("DisputeGateway#addTextEvidence allowed null content");
        } catch (IllegalArgumentException exception) {
            assertEquals(exception.getMessage(), "content cannot be empty");
        }
    }

    @Test
    public void addTextEvidenceEmptyStringContentRaisesIllegalArgumentException() {
        try {
            new DisputeGateway(null, null).addTextEvidence("id", " ");
            fail("DisputeGateway#addTextEvidence allowed empty string content");
        } catch (IllegalArgumentException exception) {
            assertEquals(exception.getMessage(), "content cannot be empty");
        }
    }

	@Test
    public void addFileEvidenceNullDisputeIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).addFileEvidence(null, "documentId");
            fail("DisputeGateway#addFileEvidence allowed null despute ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \"null\" not found");
        }
    }

    @Test
    public void addFileEvidenceEmptyStringDisputeIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).addFileEvidence(" ", "documentId");
            fail("DisputeGateway#addFileEvidence allowed empty string dispute ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \" \" not found");
        }
    }

	@Test
    public void addFileEvidenceNullDocumentIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).addFileEvidence("disputeId", null);
            fail("DisputeGateway#addFileEvidence allowed null document ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "document with id \"null\" not found");
        }
    }

    @Test
    public void addFileEvidenceEmptyStringDocumentIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).addFileEvidence("disputeId", " ");
            fail("DisputeGateway#addFileEvidence allowed empty string ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "document with id \" \" not found");
        }
    }

    @Test
    public void finalizeNullRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).finalize(null);
            fail("DisputeGateway#finalize allowed null");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \"null\" not found");
        }
    }

    @Test
    public void finalizeEmptyStringRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).finalize(" ");
            fail("DisputeGateway#finalize allowed empty string");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \" \" not found");
        }
    }

    @Test
    public void findNullRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).find(null);
            fail("DisputeGateway#find allowed null");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \"null\" not found");
        }
    }

    @Test
    public void findEmptyStringRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).find(" ");
            fail("DisputeGateway#find allowed empty string");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "dispute with id \" \" not found");
        }
    }

    @Test
    public void removeEvidenceNullDisputeIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).removeEvidence(null, "evidenceId");
            fail("DisputeGateway#removeEvidence allowed null despute ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "evidence with id \"evidenceId\" for dispute with id \"null\" not found");
        }
    }

    @Test
    public void removeEvidenceEmptyStringDisputeIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).removeEvidence(" ", "evidenceId");
            fail("DisputeGateway#removeEvidence allowed empty string dispute ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "evidence with id \"evidenceId\" for dispute with id \" \" not found");
        }
    }

	@Test
    public void removeEvidenceNullEvidenceIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).removeEvidence("disputeId", null);
            fail("DisputeGateway#removeEvidence allowed null document ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "evidence with id \"null\" for dispute with id \"disputeId\" not found");
        }
    }

    @Test
    public void removeEvidenceEmptyStringEvidenceIdRaisesNotFoundException() {
        try {
            new DisputeGateway(null, null).removeEvidence("disputeId", " ");
            fail("DisputeGateway#removeEvidence allowed empty string ID");
        } catch (NotFoundException exception) {
            assertEquals(exception.getMessage(), "evidence with id \" \" for dispute with id \"disputeId\" not found");
        }
    }
}
