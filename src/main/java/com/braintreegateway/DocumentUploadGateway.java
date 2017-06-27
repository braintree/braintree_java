package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class DocumentUploadGateway {
  private final Http http;
  private final Configuration configuration;

  private final String BASE_URL = "/document_uploads";

  public DocumentUploadGateway(Http http, Configuration configuration) {
    this.http = http;
    this.configuration = configuration;
  }

  public Result<DocumentUpload> create(DocumentUploadRequest request) {
    final NodeWrapper response = http.postMultipart(configuration.getMerchantPath() + BASE_URL, request.getRequest(), request.getFile());
    return new Result<DocumentUpload>(response, DocumentUpload.class);
  }
}
