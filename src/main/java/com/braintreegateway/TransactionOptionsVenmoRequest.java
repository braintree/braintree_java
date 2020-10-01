package com.braintreegateway;

public class TransactionOptionsVenmoRequest extends Request {
    private TransactionOptionsRequest parent;
    private String profileId;

    public TransactionOptionsVenmoRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsVenmoRequest profileId(String profileId) {
        this.profileId = profileId;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("venmo").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("venmo");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("profile-id", profileId);
    }
}
