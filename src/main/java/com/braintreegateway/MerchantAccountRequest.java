package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link MerchantAccount MerchantAccounts}.
 */
public class MerchantAccountRequest extends Request {

    private ApplicantDetailsRequest applicantDetails;
    private boolean tosAccepted;
    private String masterMerchantAccountId;
    private String id;

    public MerchantAccountRequest id(String id) {
        this.id = id;
        return this;
    }

    public ApplicantDetailsRequest applicantDetails() {
        applicantDetails = new ApplicantDetailsRequest(this);
        return applicantDetails;
    }

    public MerchantAccountRequest masterMerchantAccountId(String masterMerchantAccountId) {
        this.masterMerchantAccountId = masterMerchantAccountId;
        return this;
    }

    public MerchantAccountRequest tosAccepted(boolean accepted) {
        this.tosAccepted = accepted;
        return this;
    }

    @Override
    public String toQueryString() {
        return toQueryString("merchant_account");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("merchant_account").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
                addElement("applicantDetails", applicantDetails).
                addElement("tosAccepted", tosAccepted).
                addElement("masterMerchantAccountId", masterMerchantAccountId).
                addElement("id", id);
    }

}
