package com.braintreegateway;

public class IndividualRequest extends Request {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private IndividualAddressRequest address;
    private String dateOfBirth;
    private String ssn;
    private MerchantAccountRequest parent;

    public IndividualRequest(MerchantAccountRequest parent) {
        this.parent = parent;
    }

    public IndividualRequest firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public IndividualRequest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public IndividualRequest email(String email) {
        this.email = email;
        return this;
    }

    public IndividualRequest phone(String phone) {
        this.phone = phone;
        return this;
    }

    public IndividualAddressRequest address() {
        this.address = new IndividualAddressRequest(this);
        return address;
    }

    public IndividualRequest dateOfBirth(String dob) {
        this.dateOfBirth = dob;
        return this;
    }

    public IndividualRequest ssn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public MerchantAccountRequest done() {
        return this.parent;
    }

    @Override
    public String toQueryString() {
        return toQueryString("individual");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("individual").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
                addElement("firstName", firstName).
                addElement("lastName", lastName).
                addElement("email", email).
                addElement("phone", phone).
                addElement("addressRequest", address).
                addElement("dateOfBirth", dateOfBirth).
                addElement("ssn", ssn);
    }
}
