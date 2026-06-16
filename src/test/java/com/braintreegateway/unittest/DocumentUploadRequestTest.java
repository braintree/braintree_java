package com.braintreegateway.unittest;

import java.net.URL;

import com.braintreegateway.DocumentUpload;
import com.braintreegateway.DocumentUploadRequest;

import java.io.File;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentUploadRequestTest {
  @Test
  public void documentUploadRequestThrowsExceptionWhenKindIsNull() {
      URL fileToUpload = getClass().getClassLoader().getResource("fixtures/bt_logo.png");

      Exception e = assertThrows(IllegalArgumentException.class, () -> {
          DocumentUploadRequest uploadRequest = new DocumentUploadRequest(null,
                  new File(fileToUpload.getFile()));
      });

      assertEquals("DocumentUpload.Kind must not be null", e.getMessage());
  }

  @Test
  public void documentUploadRequestThrowsExceptionWhenFileIsNull() {
      Exception e = assertThrows(IllegalArgumentException.class, () -> {
          DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT, null);
      });

      assertEquals("File must not be null", e.getMessage());
  }

  @Test
  public void getRequestAndGetFileReturnCorrectValues() throws Exception {
      URL fileUrl = getClass().getClassLoader().getResource("fixtures/bt_logo.png");
      File file = new File(fileUrl.getFile());
      DocumentUploadRequest request = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT, file);

      assertAll("document upload request fields",
              () -> assertEquals("{\"document_upload[kind]\": \"evidence_document\"}", request.getRequest()),
              () -> assertEquals(file, request.getFile()));
  }
}
