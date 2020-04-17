package com.braintreegateway.unittest;

import java.util.List;

import com.braintreegateway.DocumentUpload;
import com.braintreegateway.Result;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DocumentUploadTest {

  @Test
  public void documentUploadCanMapFileTypeIsInvalidResponse() {
    NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(FILE_TYPE_IS_INVALID_RESPONSE);

    Result<DocumentUpload> documentUploadResult = new Result<DocumentUpload>(nodeWrapper, DocumentUpload.class);

    assertFalse(documentUploadResult.isSuccess());
    List<ValidationError> validationErrorList = documentUploadResult.getErrors().getAllDeepValidationErrors();
    ValidationError expectedValidationError = new ValidationError(
      "file",
      ValidationErrorCode.DOCUMENT_UPLOAD_FILE_TYPE_IS_INVALID,
      "Only PNG, JPG, JPEG, and PDF files are accepted."
    );
    assertTrue("received validation error for invalid file type", validationErrorList.contains(expectedValidationError));
  }

  @Test
  public void documentUploadCanMapFileIsMalformedResponse() {
    NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(FILE_IS_MALFORMED_RESPONSE);

    Result<DocumentUpload> documentUploadResult = new Result<DocumentUpload>(nodeWrapper, DocumentUpload.class);

    assertFalse(documentUploadResult.isSuccess());
    List<ValidationError> validationErrorList = documentUploadResult.getErrors().getAllDeepValidationErrors();
    ValidationError expectedValidationError = new ValidationError(
      "file",
      ValidationErrorCode.DOCUMENT_UPLOAD_FILE_IS_MALFORMED_OR_ENCRYPTED,
      "Malformed or encrypted files are not accepted"
    );
    assertTrue("received validation error for file that is malformed", validationErrorList.contains(expectedValidationError));
  }

  @Test
  public void documentUploadCanMapFileIsTooLargeResponse() {
    NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(FILE_IS_TOO_LARGE_RESPONSE);

    Result<DocumentUpload> documentUploadResult = new Result<DocumentUpload>(nodeWrapper, DocumentUpload.class);

    assertFalse(documentUploadResult.isSuccess());
    List<ValidationError> validationErrorList = documentUploadResult.getErrors().getAllDeepValidationErrors();
    ValidationError expectedValidationError = new ValidationError(
      "file",
      ValidationErrorCode.DOCUMENT_UPLOAD_FILE_IS_TOO_LARGE,
      "File size is limited to 4 MB."
    );
    assertTrue("received validation error for file that is too large", validationErrorList.contains(expectedValidationError));
  }

  @Test
  public void documentUploadCanMapFileIsEmptyResponse() {
    NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(FILE_IS_EMPTY_RESPONSE);

    Result<DocumentUpload> documentUploadResult = new Result<DocumentUpload>(nodeWrapper, DocumentUpload.class);

    assertFalse(documentUploadResult.isSuccess());
    List<ValidationError> validationErrorList = documentUploadResult.getErrors().getAllDeepValidationErrors();
    ValidationError expectedValidationError = new ValidationError(
      "file",
      ValidationErrorCode.DOCUMENT_UPLOAD_FILE_IS_EMPTY,
      "File cannot be empty."
    );
    assertTrue("received validation error for file that is empty", validationErrorList.contains(expectedValidationError));
  }

  @Test
  public void documentUploadCanMapFileIsTooLongResponse() {
    NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(FILE_IS_TOO_LONG_RESPONSE);

    Result<DocumentUpload> documentUploadResult = new Result<DocumentUpload>(nodeWrapper, DocumentUpload.class);

    assertFalse(documentUploadResult.isSuccess());
    List<ValidationError> validationErrorList = documentUploadResult.getErrors().getAllDeepValidationErrors();
    ValidationError expectedValidationError = new ValidationError(
      "file",
      ValidationErrorCode.DOCUMENT_UPLOAD_FILE_IS_TOO_LONG,
      "PDF page length is limited to 50 pages"
    );
    assertTrue("received validation error for file that is too long", validationErrorList.contains(expectedValidationError));
  }

  private String FILE_TYPE_IS_INVALID_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                                 + "<api-error-response>\n"
                                                 + "  <errors>\n"
                                                 + "    <errors type=\"array\"/>\n"
                                                 + "    <document-upload>\n"
                                                 + "      <errors type=\"array\">\n"
                                                 + "        <error>\n"
                                                 + "          <code>84903</code>\n"
                                                 + "          <attribute type=\"symbol\">file</attribute>\n"
                                                 + "          <message>Only PNG, JPG, JPEG, and PDF files are accepted.</message>\n"
                                                 + "        </error>\n"
                                                 + "      </errors>\n"
                                                 + "    </document-upload>\n"
                                                 + "  </errors>\n"
                                                 + "  <params>\n"
                                                 + "    <document-upload>\n"
                                                 + "      <kind>identity_document</kind>\n"
                                                 + "    </document-upload>\n"
                                                 + "    <controller>document_uploads</controller>\n"
                                                 + "    <action>create</action>\n"
                                                 + "    <merchant-id>integration_merchant_id</merchant-id>\n"
                                                 + "  </params>\n"
                                                 + "  <message>Only PNG, JPG, JPEG, and PDF files are accepted.</message>\n"
                                                 + "</api-error-response>";

  private String FILE_IS_MALFORMED_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                              + "<api-error-response>\n"
                                              + "  <errors>\n"
                                              + "    <errors type=\"array\"/>\n"
                                              + "    <document-upload>\n"
                                              + "      <errors type=\"array\">\n"
                                              + "        <error>\n"
                                              + "          <code>84904</code>\n"
                                              + "          <attribute type=\"symbol\">file</attribute>\n"
                                              + "          <message>Malformed or encrypted files are not accepted</message>\n"
                                              + "        </error>\n"
                                              + "      </errors>\n"
                                              + "    </document-upload>\n"
                                              + "  </errors>\n"
                                              + "  <params>\n"
                                              + "    <document-upload>\n"
                                              + "      <kind>identity_document</kind>\n"
                                              + "    </document-upload>\n"
                                              + "    <controller>document_uploads</controller>\n"
                                              + "    <action>create</action>\n"
                                              + "    <merchant-id>integration_merchant_id</merchant-id>\n"
                                              + "  </params>\n"
                                              + "  <message>Malformed or encrypted files are not accepted</message>\n"
                                              + "</api-error-response>";

  private String FILE_IS_TOO_LARGE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                              + "<api-error-response>\n"
                                              + "  <errors>\n"
                                              + "    <errors type=\"array\"/>\n"
                                              + "    <document-upload>\n"
                                              + "      <errors type=\"array\">\n"
                                              + "        <error>\n"
                                              + "          <code>84902</code>\n"
                                              + "          <attribute type=\"symbol\">file</attribute>\n"
                                              + "          <message>File size is limited to 4 MB.</message>\n"
                                              + "        </error>\n"
                                              + "      </errors>\n"
                                              + "    </document-upload>\n"
                                              + "  </errors>\n"
                                              + "  <params>\n"
                                              + "    <document-upload>\n"
                                              + "      <kind>identity_document</kind>\n"
                                              + "    </document-upload>\n"
                                              + "    <controller>document_uploads</controller>\n"
                                              + "    <action>create</action>\n"
                                              + "    <merchant-id>integration_merchant_id</merchant-id>\n"
                                              + "  </params>\n"
                                              + "  <message>File size is limited to 4 MB.</message>\n"
                                              + "</api-error-response>";

  private String FILE_IS_EMPTY_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                              + "<api-error-response>\n"
                                              + "  <errors>\n"
                                              + "    <errors type=\"array\"/>\n"
                                              + "    <document-upload>\n"
                                              + "      <errors type=\"array\">\n"
                                              + "        <error>\n"
                                              + "          <code>84906</code>\n"
                                              + "          <attribute type=\"symbol\">file</attribute>\n"
                                              + "          <message>File cannot be empty.</message>\n"
                                              + "        </error>\n"
                                              + "      </errors>\n"
                                              + "    </document-upload>\n"
                                              + "  </errors>\n"
                                              + "  <params>\n"
                                              + "    <document-upload>\n"
                                              + "      <kind>identity_document</kind>\n"
                                              + "    </document-upload>\n"
                                              + "    <controller>document_uploads</controller>\n"
                                              + "    <action>create</action>\n"
                                              + "    <merchant-id>integration_merchant_id</merchant-id>\n"
                                              + "  </params>\n"
                                              + "  <message>File cannot be empty.</message>\n"
                                              + "</api-error-response>";

  private String FILE_IS_TOO_LONG_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                              + "<api-error-response>\n"
                                              + "  <errors>\n"
                                              + "    <errors type=\"array\"/>\n"
                                              + "    <document-upload>\n"
                                              + "      <errors type=\"array\">\n"
                                              + "        <error>\n"
                                              + "          <code>84905</code>\n"
                                              + "          <attribute type=\"symbol\">file</attribute>\n"
                                              + "          <message>PDF page length is limited to 50 pages</message>\n"
                                              + "        </error>\n"
                                              + "      </errors>\n"
                                              + "    </document-upload>\n"
                                              + "  </errors>\n"
                                              + "  <params>\n"
                                              + "    <document-upload>\n"
                                              + "      <kind>identity_document</kind>\n"
                                              + "    </document-upload>\n"
                                              + "    <controller>document_uploads</controller>\n"
                                              + "    <action>create</action>\n"
                                              + "    <merchant-id>integration_merchant_id</merchant-id>\n"
                                              + "  </params>\n"
                                              + "  <message>PDF page length is limited to 50 pages</message>\n"
                                              + "</api-error-response>";
}
