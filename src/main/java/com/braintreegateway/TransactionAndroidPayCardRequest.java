package com.braintreegateway;

// NEXT_MAJOR_VERSION - rename to TransactionGooglePayCardRequest
public class TransactionAndroidPayCardRequest extends Request {

  private String cryptogram;
  private String eciIndicator;
  private String expirationMonth;
  private String expirationYear;
  private String googleTransactionId;
  private String number;
  private String sourceCardLastFour;
  private String sourceCardType;
  private TransactionRequest parent;

  public TransactionAndroidPayCardRequest(TransactionRequest parent) {
    this.parent = parent;
  }

  public TransactionAndroidPayCardRequest cryptogram(String cryptogram) {
    this.cryptogram = cryptogram;
    return this;
  }

  public TransactionAndroidPayCardRequest eciIndicator(String eciIndicator) {
    this.eciIndicator = eciIndicator;
    return this;
  }

  public TransactionAndroidPayCardRequest expirationMonth(String expirationMonth) {
    this.expirationMonth = expirationMonth;
    return this;
  }

  public TransactionAndroidPayCardRequest expirationYear(String expirationYear) {
    this.expirationYear = expirationYear;
    return this;
  }

  public TransactionAndroidPayCardRequest googleTransactionId(String googleTransactionId) {
    this.googleTransactionId = googleTransactionId;
    return this;
  }

  public TransactionAndroidPayCardRequest number(String number) {
    this.number = number;
    return this;
  }

  public TransactionAndroidPayCardRequest sourceCardLastFour(String sourceCardLastFour) {
    this.sourceCardLastFour = sourceCardLastFour;
    return this;
  }

  public TransactionAndroidPayCardRequest sourceCardType(String sourceCardType) {
    this.sourceCardType = sourceCardType;
    return this;
  }

  public TransactionRequest done() {
    return parent;
  }

  @Override
  public String toXML() {
    return buildRequest("androidPayCard").toXML();
  }

  @Override
  public String toQueryString() {
    return toQueryString("androidPayCard");
  }

  @Override
  public String toQueryString(String root) {
    return buildRequest(root).toQueryString();
  }

  protected RequestBuilder buildRequest(String root) {
    return new RequestBuilder(root)
      .addElement("cryptogram", cryptogram)
      .addElement("eciIndicator", eciIndicator)
      .addElement("expirationMonth", expirationMonth)
      .addElement("expirationYear", expirationYear)
      .addElement("number", number)
      .addElement("googleTransactionId", googleTransactionId)
      .addElement("sourceCardLastFour", sourceCardLastFour)
      .addElement("sourceCardType", sourceCardType);
  }
}
