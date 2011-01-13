package com.braintreegateway;

public class TransactionDescriptorRequest extends DescriptorRequest {
    private TransactionRequest parent;

    public TransactionDescriptorRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionDescriptorRequest name(String name) {
        super.name(name);
        return this;
    }

    public TransactionDescriptorRequest phone(String phone) {
        super.phone(phone);
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }
}
