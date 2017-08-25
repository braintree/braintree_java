package com.braintreegateway;

import java.io.File;

public class DocumentUploadRequest extends Request {

  private DocumentUpload.Kind kind;
  private File file;

  public DocumentUploadRequest(DocumentUpload.Kind kind, File file) {
    if (kind == null) {
      throw new IllegalArgumentException("DocumentUpload.Kind must not be null");
    }

    if (file == null) {
      throw new IllegalArgumentException("File must not be null");
    }

    this.kind = kind;
    this.file = file;
  }

  public String getRequest() {
    return "{\"document_upload[kind]\": \"" + kind.toString() + "\"}";
  }

  public File getFile() {
    return file;
  }
}
