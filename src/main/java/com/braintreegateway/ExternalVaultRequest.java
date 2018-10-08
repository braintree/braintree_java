package com.braintreegateway;

public class ExternalVaultRequest extends Request {
    private TransactionRequest parent;
    private ExternalVault.Status status;
    private String previousNetworkTransactionId;

    public ExternalVaultRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public ExternalVaultRequest willVault() {
        this.status = ExternalVault.Status.WILL_VAULT;
        return this;
    }

    public ExternalVaultRequest vaulted() {
        this.status = ExternalVault.Status.VAULTED;
        return this;
    }

    public ExternalVaultRequest previousNetworkTransactionId(String previousNetworkTransactionId) {
        this.previousNetworkTransactionId = previousNetworkTransactionId;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("externalVault").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("status", status).
            addElement("previousNetworkTransactionId", previousNetworkTransactionId);
    }
}
