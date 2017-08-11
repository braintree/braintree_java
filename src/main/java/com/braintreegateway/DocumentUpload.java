package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class DocumentUpload {

  public enum Kind {
    EVIDENCE_DOCUMENT("evidence_document"),
    IDENTITY_DOCUMENT("identity_document");

    private final String kind;

    Kind(String kind) { this.kind = kind; }

    @Override
    public String toString() { return kind; }
  }

  private final String id;
  private final Kind kind;
  private final String contentType;
  private final String name;
  private final int size;

  public DocumentUpload(NodeWrapper node) {
    kind = Kind.valueOf(node.findString("kind").toUpperCase());
    id = node.findString("id");
    contentType = node.findString("content-type");
    name = node.findString("name");
    size = node.findInteger("size");
  }

  public String getId() {
    return id;
  }

  public Kind getKind() {
    return kind;
  }

  public String getContentType() {
    return contentType;
  }

  public String getName() {
    return name;
  }

  public int getSize() {
    return size;
  }
}
