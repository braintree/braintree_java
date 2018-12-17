package com.braintreegateway.integrationtest;

import java.io.File;
import java.net.URL;

import com.braintreegateway.DocumentUpload;
import com.braintreegateway.DocumentUploadRequest;
import com.braintreegateway.Result;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DocumentUploadIT extends IntegrationTest {

  private String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\Z";

  @Test
  public void createReturnsSuccessfulWithValidRequest() {
    URL fileToUpload = getClass().getClassLoader().getResource("fixtures/bt_logo.png");
    DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT,
                                                                    new File(fileToUpload.getFile()));

    Result<DocumentUpload> uploadResult = gateway.documentUpload().create(uploadRequest);
    DocumentUpload documentUpload = uploadResult.getTarget();

    assertTrue(uploadResult.isSuccess());
    assertTrue(documentUpload.getId().matches(UUID_REGEX));
    assertEquals(DocumentUpload.Kind.EVIDENCE_DOCUMENT, documentUpload.getKind());
    assertEquals("image/png", documentUpload.getContentType());
    assertEquals("bt_logo.png", documentUpload.getName());
    assertEquals(2443, documentUpload.getSize());
  }

  @Test
  public void createReturnsErrorWithUnsupportedFileType() {
    URL fileToUpload = getClass().getClassLoader().getResource("fixtures/gif_extension_bt_logo.gif");
    DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT,
        new File(fileToUpload.getFile()));

    Result<DocumentUpload> uploadResult = gateway.documentUpload().create(uploadRequest);
    ValidationError error = uploadResult.getErrors()
      .forObject("documentUpload")
      .getAllValidationErrors()
      .get(0);

    assertEquals(ValidationErrorCode.DOCUMENT_UPLOAD_FILE_TYPE_IS_INVALID, error.getCode());
  }

  @Test
  public void createReturnsErrorWithMalformedFile() {
    URL fileToUpload = getClass().getClassLoader().getResource("fixtures/malformed_pdf.pdf");
    DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT,
        new File(fileToUpload.getFile()));

    Result<DocumentUpload> uploadResult = gateway.documentUpload().create(uploadRequest);
    ValidationError error = uploadResult.getErrors()
      .forObject("documentUpload")
      .getAllValidationErrors()
      .get(0);

    assertEquals(ValidationErrorCode.DOCUMENT_UPLOAD_FILE_IS_MALFORMED_OR_ENCRYPTED, error.getCode());
  }

  @Test
  public void createReturnsErrorWhenFileIsOver4Mb() {
    try {
      PrintWriter writer = new PrintWriter("large_file.png");
      for (int i = 0; i <= 1048577; i++) {
        writer.print("aaaa");
      }
      writer.close();
    } catch (FileNotFoundException e) {
      fail("Cannot open large_file.png to write");
    }

    DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT,
        new File("large_file.png"));

    Result<DocumentUpload> uploadResult = gateway.documentUpload().create(uploadRequest);
    ValidationError error = uploadResult.getErrors()
      .forObject("documentUpload")
      .getAllValidationErrors()
      .get(0);

    assertEquals(ValidationErrorCode.DOCUMENT_UPLOAD_FILE_IS_TOO_LARGE, error.getCode());
  }

  @Test
  public void createReturnsErrorWhenPDFFileIsOver50Pages() {
    URL fileToUpload = getClass().getClassLoader().getResource("fixtures/too_long.pdf");
    DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.EVIDENCE_DOCUMENT,
        new File(fileToUpload.getFile()));

    Result<DocumentUpload> uploadResult = gateway.documentUpload().create(uploadRequest);
    ValidationError error = uploadResult.getErrors()
      .forObject("documentUpload")
      .getAllValidationErrors()
      .get(0);

    assertEquals(ValidationErrorCode.DOCUMENT_UPLOAD_FILE_IS_TOO_LONG, error.getCode());
  }
}
