package com.braintreegateway;

public abstract class DescriptorRequest extends Request {

    protected String name;
    protected String phone;

    public DescriptorRequest() {
        super();
    }
    
    public DescriptorRequest name(String name) {
        this.name = name;
        return this;
    }

    public DescriptorRequest phone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("descriptor").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("name", name).
            addElement("phone", phone);
    }
}