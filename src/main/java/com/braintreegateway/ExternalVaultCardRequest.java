package com.braintreegateway;

public class ExternalVaultCardRequest extends Request {
    private CreditCardRequest parent;
    private String networkTransactionId;

    public ExternalVaultCardRequest(CreditCardRequest parent) {
        this.parent = parent;
    }

    public ExternalVaultCardRequest networkTransactionId(String networkTransactionId) {
        this.networkTransactionId = networkTransactionId;
        return this;
    }

    public CreditCardRequest done(){
      return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("externalVault").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("networkTransactionId", networkTransactionId);
    }
}
