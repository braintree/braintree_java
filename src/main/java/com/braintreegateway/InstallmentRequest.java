package com.braintreegateway;

public class InstallmentRequest extends Request {
  private Integer count;
  private
  TransactionRequest parent;

  public InstallmentRequest(TransactionRequest txnRequest) {
    this.parent = txnRequest;
  }

  public TransactionRequest done() {
    return parent;
  }

  @Override
  public String toXML() {
    return buildRequest("installments").toXML();
  }

  @Override
  public String toQueryString() {
    return toQueryString("installments");
  }

  @Override
  public String toQueryString(String root) {
    return buildRequest(root)
      .addTopLevelElement("count", count.toString())
      .toQueryString();
  }

  public InstallmentRequest count(Integer count) {
    this.count = count;
    return this;
  }

  protected RequestBuilder buildRequest(String root) {
    RequestBuilder builder = new RequestBuilder(root)
      .addElement("count", count);

    return builder;
  }

}
