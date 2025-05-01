package com.braintreegateway;

public class TransactionSubMerchantInternationalPhoneRequest extends AddressInternationalPhoneRequest {
    private TransactionSubMerchantAddressRequest parent;
    private String countryCode;
    private String nationalNumber;

    public TransactionSubMerchantInternationalPhoneRequest() {}
    

    public TransactionSubMerchantInternationalPhoneRequest(TransactionSubMerchantAddressRequest parent) {
        super();
        this.parent = parent;
    }

    public TransactionSubMerchantAddressRequest done() {
        return parent;
    }

    public TransactionSubMerchantInternationalPhoneRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public TransactionSubMerchantInternationalPhoneRequest nationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("internationalPhone").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("countryCode", countryCode)
            .addElement("nationalNumber", nationalNumber);
    }
}