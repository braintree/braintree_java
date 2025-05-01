package com.braintreegateway;

public class TransactionSubMerchant extends Request {
    private TransactionSubMerchantAddressRequest address;
    private String legalName;
    private PaymentFacilitatorRequest parent;
    private String referenceNumber;
    private String taxId;

    public TransactionSubMerchant(PaymentFacilitatorRequest parent) {
        this.parent = parent;
    }

    public TransactionSubMerchantAddressRequest address() {
        address = new TransactionSubMerchantAddressRequest(this);
        return this.address;
    }

    public TransactionSubMerchant legalName(String legalName) {
        this.legalName = legalName;
        return this;
    }

    public TransactionSubMerchant referenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public TransactionSubMerchant taxId(String taxId) {
        this.taxId = taxId;
        return this;
    }

    public PaymentFacilitatorRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("subMerchant").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("subMerchant");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("address", address)
            .addElement("legalName", legalName)
            .addElement("referenceNumber", referenceNumber)
            .addElement("taxId", taxId);

    }
}