package com.braintreegateway.integrationtest;

import java.io.File;
import java.net.URL;

import com.braintreegateway.DocumentUpload;
import com.braintreegateway.DocumentUploadRequest;
import com.braintreegateway.Result;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentUploadIT extends IntegrationTest {

  private String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\Z";

  @Test
  public void createReturnsSuccessfulWithValidRequest() {
    URL fileToUpload = getClass().getClassLoader().getResource("fixtures/bt_logo.png");
    DocumentUploadRequest uploadRequest = new DocumentUploadRequest(DocumentUpload.Kind.IDENTITY_DOCUMENT,
                                                                    new File(fileToUpload.getFile()));

    Result<DocumentUpload> uploadResult = gateway.documentUpload().create(uploadRequest);
    DocumentUpload documentUpload = uploadResult.getTarget();

    assertTrue(uploadResult.isSuccess());
    assertTrue(documentUpload.getId().matches(UUID_REGEX));
    assertEquals(DocumentUpload.Kind.IDENTITY_DOCUMENT, documentUpload.getKind());
    assertEquals("image/png", documentUpload.getContentType());
    assertEquals("bt_logo.png", documentUpload.getName());
    assertEquals(2443, documentUpload.getSize());
  }
}
