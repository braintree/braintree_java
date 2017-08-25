package com.braintreegateway.unittest;

import java.net.URL;

import com.braintreegateway.DocumentUpload;
import com.braintreegateway.DocumentUploadRequest;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentUploadRequestTest {
  @Test
  public void documentUploadRequestThrowsExceptionWhenKindIsNull() {
      URL fileToUpload = getClass().getClassLoader().getResource("fixtures/bt_logo.png");

      try {
          DocumentUploadRequest uploadRequest = new DocumentUploadRequest(null,
                  new File(fileToUpload.getFile()));
      } catch (IllegalArgumentException e) {
          assertEquals("DocumentUpload.Kind must not be null", e.getMessage());
      }
  }

  @Test
  public void documentUploadRequestThrowsExceptionWhenFileIsNull() {
      try {
          DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT, null);
      } catch (IllegalArgumentException e) {
          assertEquals("File must not be null", e.getMessage());
      }
  }
}
