package com.braintreegateway;

public class BusinessRequest extends Request {
    private String dbaName;
    private String legalName;
    private BusinessAddressRequest address;
    private String taxId;
    private MerchantAccountRequest parent;

    public BusinessRequest(MerchantAccountRequest parent) {
        this.parent = parent;
    }

    public BusinessRequest dbaName(String dbaName) {
        this.dbaName = dbaName;
        return this;
    }

    public BusinessRequest legalName(String legalName) {
        this.legalName = legalName;
        return this;
    }

    public BusinessAddressRequest address() {
        this.address = new BusinessAddressRequest(this);
        return address;
    }

    public BusinessRequest taxId(String taxId) {
        this.taxId = taxId;
        return this;
    }

    public MerchantAccountRequest done() {
        return this.parent;
    }

    @Override
    public String toQueryString() {
        return toQueryString("business");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("business").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
                .addElement("dbaName", dbaName)
                .addElement("legalName", legalName)
                .addElement("addressRequest", address)
                .addElement("taxId", taxId);
    }
}
