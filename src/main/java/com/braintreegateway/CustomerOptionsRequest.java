package com.braintreegateway;

public class CustomerOptionsRequest extends Request {
    private CustomerRequest parent;
    private CustomerOptionsPayPalRequest customerOptionsPayPalRequest;

    public CustomerOptionsRequest() {

    }

    public CustomerOptionsRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public CustomerRequest done() {
        return parent;
    }

    public CustomerOptionsPayPalRequest paypal() {
        customerOptionsPayPalRequest = new CustomerOptionsPayPalRequest(this);
        return customerOptionsPayPalRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("paypal", customerOptionsPayPalRequest);
    }
}
