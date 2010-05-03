package com.braintreegateway;

public class CreditCardAddressRequest extends NestedAddressRequest<CreditCardRequest> {

    public CreditCardAddressRequest(CreditCardRequest parent) {
        super(parent);
        this.tagName = "billingAddress";
    }
}
