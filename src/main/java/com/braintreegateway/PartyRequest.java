package com.braintreegateway;

import java.util.Calendar;

public class PartyRequest extends Request {
    private String accountReferenceNumber;
    private String accountReferenceNumberType;
    private PartyAddressRequest address;
    private Calendar dateOfBirth;
    private String firstName;
    private String lastName;
    private String middleName;
    private String taxId;
    private TransferRequest parent;
    private String tagName = "party";

    public PartyRequest(TransferRequest parent, String tagName) {
        this.parent = parent;
        this.tagName = tagName;
    }

    public PartyRequest(TransferRequest parent) {
        this.parent = parent;
    }

    public PartyRequest accountReferenceNumber(String accountReferenceNumber) {
       this.accountReferenceNumber = accountReferenceNumber;
       return this;
    }

    public PartyRequest accountReferenceNumberType(String accountReferenceNumberType) {
       this.accountReferenceNumberType = accountReferenceNumberType;
       return this;
    }

    public PartyAddressRequest address() {
       address = new PartyAddressRequest(this);
       return address;
    }

    public PartyRequest dateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public PartyRequest firstName(String firstName) {
       this.firstName = firstName;
       return this;
    }

    public PartyRequest lastName(String lastName) { 
       this.lastName = lastName;
       return this;
    }

    public PartyRequest middleName(String middleName) {
       this.middleName = middleName;
       return this;
    }

    public PartyRequest taxId(String taxId) {
       this.taxId = taxId;
       return this;
    }

    public TransferRequest done() { 
       return parent;
    }

    @Override
    public String toXML() {
        return buildRequest(tagName).toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString(tagName);
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);
        if (accountReferenceNumber != null) {
            builder.addElement("accountReferenceNumber", accountReferenceNumber);
        }
        if (accountReferenceNumberType != null) {
            builder.addElement("accountReferenceNumberType", accountReferenceNumberType);
        }
        if (address != null) {
            builder.addElement("address", address);
        }
        if (dateOfBirth != null) {
            builder.addElement("dateOfBirth", dateOfBirth);
        }
        if (firstName != null) {
            builder.addElement("firstName", firstName);
        }
        if (lastName != null) {
            builder.addElement("lastName", lastName);
        }
        if (middleName != null) {
            builder.addElement("middleName", middleName);
        }
        if (taxId != null) {
            builder.addElement("taxId", taxId);
        }
        return builder;
    }
}
