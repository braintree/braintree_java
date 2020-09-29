package com.braintreegateway.testhelpers;

import com.braintreegateway.Request;
import com.braintreegateway.RequestBuilder;

public class ThreeDSecureRequestForTests extends Request {

    private String expirationMonth;
    private String expirationYear;
    private String number;
    protected String tagName;

    public ThreeDSecureRequestForTests() {
        this.tagName = "threeDSecureVerification";
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
        return new RequestBuilder(root)
            .addElement("expirationMonth", expirationMonth)
            .addElement("expirationYear", expirationYear)
            .addElement("number", number);
    }
}
