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

    public class ApplicantDetailsRequest extends Request {
        private String firstName;
        private String lastName;
        private String email;
        private AddressRequest addressRequest;
        private String dateOfBirth;
        private String ssn;
        private String routingNumber;
        private String accountNumber;

        public ApplicantDetailsRequest firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ApplicantDetailsRequest lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ApplicantDetailsRequest email(String email) {
            this.email = email;
            return this;
        }

        public ApplicantDetailsRequest address(AddressRequest addressRequest) {
            this.addressRequest = addressRequest;
            return this;
        }

        public ApplicantDetailsRequest dateOfBirth(String dob) {
            this.dateOfBirth = dob;
            return this;
        }

        public ApplicantDetailsRequest ssn(String ssn) {
            this.ssn = ssn;
            return this;
        }

        public ApplicantDetailsRequest routingNumber(String routingNumber) {
            this.routingNumber = routingNumber;
            return this;
        }

        public ApplicantDetailsRequest accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public MerchantAccountRequest done() {
            return MerchantAccountRequest.this;
        }

        @Override
        public String toQueryString() {
            return toQueryString("applicant_details");
        }

        @Override
        public String toQueryString(String root) {
            return buildRequest(root).toQueryString();
        }

        @Override
        public String toXML() {
            return buildRequest("applicant_details").toXML();
        }

        protected RequestBuilder buildRequest(String root) {
            return new RequestBuilder(root).
                    addElement("firstName", firstName).
                    addElement("lastName", lastName).
                    addElement("email", email).
                    addElement("addressRequest", addressRequest).
                    addElement("dateOfBirth", dateOfBirth).
                    addElement("ssn", ssn).
                    addElement("routingNumber", routingNumber).
                    addElement("accountNumber", accountNumber);
        }
    }

    public ApplicantDetailsRequest applicantDetails() {
        applicantDetails = new ApplicantDetailsRequest();
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
