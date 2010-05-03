package com.braintreegateway;

public class TransactionAddressRequest extends NestedAddressRequest<TransactionRequest> {

    public TransactionAddressRequest(TransactionRequest parent, String tagName) {
        super(parent);
        this.tagName = tagName;
    }

}
