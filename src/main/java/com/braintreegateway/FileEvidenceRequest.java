package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around FileEvidence.
 */
public class FileEvidenceRequest extends Request {
    private String documentId;
    private String category;

    public FileEvidenceRequest documentId(String id) {
        this.documentId = id;
        return this;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public FileEvidenceRequest category(String category) {
        this.category = category;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("evidence").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("document_upload_id", documentId).
            addElement("category", category);
    }
}
