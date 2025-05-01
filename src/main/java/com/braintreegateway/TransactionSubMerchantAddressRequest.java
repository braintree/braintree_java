package com.braintreegateway;


public class TransactionSubMerchantAddressRequest extends AddressRequest {

    private TransactionSubMerchantInternationalPhoneRequest internationalPhoneRequest; 
    private TransactionSubMerchant parent; 

    public TransactionSubMerchantAddressRequest(TransactionSubMerchant parent) {
        this.parent = parent;
        this.tagName = "address";
    }

    @Override
    public TransactionSubMerchantAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public TransactionSubMerchantAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public TransactionSubMerchantAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public TransactionSubMerchantAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public TransactionSubMerchantAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public TransactionSubMerchantAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    } 

    @Override
    public TransactionSubMerchantInternationalPhoneRequest internationalPhone() {
        internationalPhoneRequest = new TransactionSubMerchantInternationalPhoneRequest(this);
        return this.internationalPhoneRequest;
    }

    public TransactionSubMerchant done() {
        return parent;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        RequestBuilder requestBuilder = super.buildRequest(root);
        if (internationalPhoneRequest != null) {
            requestBuilder = requestBuilder.addElement("internationalPhone", internationalPhoneRequest);
        }
        return requestBuilder;
    }
}