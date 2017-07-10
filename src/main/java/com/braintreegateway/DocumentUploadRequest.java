package com.braintreegateway;

import java.io.File;

public class DocumentUploadRequest extends Request {

  private DocumentUpload.Kind kind;
  private File file;

  public DocumentUploadRequest(DocumentUpload.Kind kind, File file) {
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
