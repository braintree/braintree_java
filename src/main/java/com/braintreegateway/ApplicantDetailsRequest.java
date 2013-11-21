package com.braintreegateway;

public class ApplicantDetailsRequest extends Request {
    private String companyName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private ApplicantDetailsAddressRequest address;
    private String dateOfBirth;
    private String ssn;
    private String taxId;
    private String routingNumber;
    private String accountNumber;
    private MerchantAccountRequest parent;

    public ApplicantDetailsRequest(MerchantAccountRequest parent) {
        this.parent = parent;
    }

    public ApplicantDetailsRequest companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

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

    public ApplicantDetailsRequest phone(String phone) {
        this.phone = phone;
        return this;
    }

    public ApplicantDetailsAddressRequest address() {
        this.address = new ApplicantDetailsAddressRequest(this);
        return address;
    }

    public ApplicantDetailsRequest dateOfBirth(String dob) {
        this.dateOfBirth = dob;
        return this;
    }

    public ApplicantDetailsRequest ssn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public ApplicantDetailsRequest taxId(String taxId) {
        this.taxId = taxId;
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
        return this.parent;
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
                addElement("companyName", companyName).
                addElement("firstName", firstName).
                addElement("lastName", lastName).
                addElement("email", email).
                addElement("phone", phone).
                addElement("addressRequest", address).
                addElement("dateOfBirth", dateOfBirth).
                addElement("ssn", ssn).
                addElement("taxId", taxId).
                addElement("routingNumber", routingNumber).
                addElement("accountNumber", accountNumber);
    }
}
