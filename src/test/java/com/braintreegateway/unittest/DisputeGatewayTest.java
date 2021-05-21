package com.braintreegateway.unittest;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.DisputeGateway;
import com.braintreegateway.TextEvidenceRequest;
import com.braintreegateway.FileEvidenceRequest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DisputeGatewayTest {

    @Test
    public void acceptNullRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).accept(null);
        });

        assertEquals(e.getMessage(), "dispute with id \"null\" not found");
    }

    @Test
    public void acceptEmptyStringRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).accept(" ");
        });

        assertEquals(e.getMessage(), "dispute with id \" \" not found");
    }

    @Test
    public void addTextEvidenceNullIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addTextEvidence(null, "content");
        });

        assertEquals(e.getMessage(), "Dispute ID is required");
    }

    @Test
    public void addTextEvidenceEmptyStringIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addTextEvidence(" ", "content");
        });

        assertEquals(e.getMessage(), "Dispute ID is required");
    }

    @Test
    public void addTextEvidenceNullTextEvidenceRequestRaisesIllegalArgumentException() {
        TextEvidenceRequest textEvidenceRequest = null;

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            new DisputeGateway(null, null).addTextEvidence("id", textEvidenceRequest);
        });

        assertEquals(e.getMessage(), "TextEvidenceRequest cannot be null");
    }

    @Test
    public void addTextEvidenceNullContentRaisesIllegalArgumentException() {
        String content = null;

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            new DisputeGateway(null, null).addTextEvidence("id", content);
        });

        assertEquals(e.getMessage(), "Content cannot be empty");
    }

    @Test
    public void addTextEvidenceEmptyStringContentRaisesIllegalArgumentException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            new DisputeGateway(null, null).addTextEvidence("id", " ");
        });

        assertEquals(e.getMessage(), "Content cannot be empty");
    }

    @Test
    public void addTextEvidenceRequestEmptyStringContentRaisesIllegalArgumentException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            new DisputeGateway(null, null).addTextEvidence("id", new TextEvidenceRequest().content(" "));
        });

        assertEquals(e.getMessage(), "Content cannot be empty");
    }

	@Test
    public void addFileEvidenceNullDisputeIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addFileEvidence(null, "documentId");
        });

        assertEquals(e.getMessage(), "dispute with id \"null\" not found");
    }

    @Test
    public void addFileEvidenceEmptyStringDisputeIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addFileEvidence(" ", "documentId");
        });

        assertEquals(e.getMessage(), "dispute with id \" \" not found");
    }

	@Test
    public void addFileEvidenceNullDocumentIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addFileEvidence("disputeId", (String) null);
        });

        assertEquals(e.getMessage(), "document with id \"null\" not found");
    }

    @Test
    public void addFileEvidenceEmptyStringDocumentIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addFileEvidence("disputeId", " ");
        });

        assertEquals(e.getMessage(), "document with id \" \" not found");
    }

    @Test
    public void addFileEvidenceWithFileEvidenceRequestNullDocumentIdRaisesNotFoundException() {
        FileEvidenceRequest request = new FileEvidenceRequest().documentId(null);

        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addFileEvidence("disputeId", request);
        });

        assertEquals(e.getMessage(), "document with id \"null\" not found");
    }

    @Test
    public void addFileEvidenceWithFileEvidenceRequestEmptyDocumentIdRaisesNotFoundException() {
        FileEvidenceRequest request = new FileEvidenceRequest().documentId("");

        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).addFileEvidence("disputeId", request);
        });

        assertEquals(e.getMessage(), "document with id \"\" not found");
    }

    @Test
    public void finalizeNullRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).finalize(null);
        });

        assertEquals(e.getMessage(), "dispute with id \"null\" not found");
    }

    @Test
    public void finalizeEmptyStringRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).finalize(" ");
        });

        assertEquals(e.getMessage(), "dispute with id \" \" not found");
    }

    @Test
    public void findNullRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).find(null);
        });

        assertEquals(e.getMessage(), "dispute with id \"null\" not found");
    }

    @Test
    public void findEmptyStringRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).find(" ");
        });

        assertEquals(e.getMessage(), "dispute with id \" \" not found");
    }

    @Test
    public void removeEvidenceNullDisputeIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).removeEvidence(null, "evidenceId");
        });

        assertEquals(e.getMessage(), "evidence with id \"evidenceId\" for dispute with id \"null\" not found");
    }

    @Test
    public void removeEvidenceEmptyStringDisputeIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).removeEvidence(" ", "evidenceId");
        });

        assertEquals(e.getMessage(), "evidence with id \"evidenceId\" for dispute with id \" \" not found");
    }

	@Test
    public void removeEvidenceNullEvidenceIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).removeEvidence("disputeId", null);
        });

        assertEquals(e.getMessage(), "evidence with id \"null\" for dispute with id \"disputeId\" not found");
    }

    @Test
    public void removeEvidenceEmptyStringEvidenceIdRaisesNotFoundException() {
        Exception e = assertThrows(NotFoundException.class, () -> {
            new DisputeGateway(null, null).removeEvidence("disputeId", " ");
        });

        assertEquals(e.getMessage(), "evidence with id \" \" for dispute with id \"disputeId\" not found");
    }
}
