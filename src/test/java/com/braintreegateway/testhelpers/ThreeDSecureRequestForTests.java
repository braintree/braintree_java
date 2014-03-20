package com.braintreegateway.testhelpers;

import com.braintreegateway.Request;
import com.braintreegateway.RequestBuilder;

public class ThreeDSecureRequestForTests extends Request {

    private String publicId;
    private String expirationMonth;
    private String expirationYear;
    private String number;
    private String status;
    protected String tagName;

    public ThreeDSecureRequestForTests() {
        this.tagName = "cardinal_verification";
    }

    public ThreeDSecureRequestForTests publicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public ThreeDSecureRequestForTests expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public ThreeDSecureRequestForTests expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public ThreeDSecureRequestForTests number(String number) {
        this.number = number;
        return this;
    }

    public ThreeDSecureRequestForTests status(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toQueryString() {
        return toQueryString(this.tagName);
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest(this.tagName).toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("publicId", publicId).
            addElement("status", status).
            addElement("expirationMonth", expirationMonth).
            addElement("expirationYear", expirationYear).
            addElement("number", number);
    }
}
